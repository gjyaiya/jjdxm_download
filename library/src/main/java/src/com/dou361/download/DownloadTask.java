package src.com.dou361.download;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

/**
 * ========================================
 * <p/>
 * 版 权：深圳市晶网电子科技有限公司 版权所有 （C） 2015
 * <p/>
 * 作 者：陈冠明
 * <p/>
 * 个人网站：http://www.dou361.com
 * <p/>
 * 版 本：1.0
 * <p/>
 * 创建日期：2015-10-6 下午3:31:09
 * <p/>
 * 描 述：下载异步任务
 * <p/>
 * <p/>
 * 修订历史：
 * <p/>
 * ========================================
 */
public class DownloadTask extends AsyncTask<Void, Integer, Integer> {

	/** 二级打印tag */
	private String secondTag = DownloadTask.class.getCanonicalName()
			+ "|<-->|>>";
	/** 一次缓存的大小 */
	final static int BUFFER_SIZE = 1024 * 8;
	/** 上下文 */
	Context context = null;
	/** 下载链接 */
	String url = "";
	/** 下载保存的文件对象 */
	File file = null;
	/** 下载状态监听 */
	DownloadTaskListener listener = null;
	/** 当前下载量 */
	long downloadSize = 0;
	/** 之前断点续传总完成下载量 */
	long previousFileSize = 0;
	/** 文件大小 */
	long totalSize = 0;
	/** 下载进度 */
	int downloadPercent = 0;
	/** 下载速度 */
	int networkSpeed = 0;
	/** 开始时间 */
	long previousTime = 0;
	/** 下载使用总时间 */
	long totalTime = 0;
	/** 下载请求对象 */
	AndroidHttpClient client = null;
	/** 是否因为失败而中断 */
	boolean interrupt = false;
	/** 是否取消 */
	boolean isCancel = false;

	/** 写文件监听写的进度 */
	private class ProgressReportingRandomAccessFile extends RandomAccessFile {

		/** 当前写文件的进度 */
		private int process = 0;

		/** 写文件的对象和模式 */
		public ProgressReportingRandomAccessFile(File file, String mode)
				throws FileNotFoundException {
			super(file, mode);
		}

		@Override
		public void write(byte[] buffer, int byteOffset, int byteCount)
				throws IOException {
			super.write(buffer, byteOffset, byteCount);
			process += byteCount;
			publishProgress(process);
		}
	};

	/**
	 * @param url
	 *            下载链接如（http://baidu.com/picture/201510060001.jpg）
	 * @param path
	 *            保存文件的路径如（/mnt/sdcard/包名/image/）
	 * @param listener
	 *            下载状态监听
	 * */
	public DownloadTask(Context context, String url, String path,
			DownloadTaskListener listener) throws IOException {
		super();

		this.context = context;
		this.url = url;
		this.listener = listener;
		String fileName = url.substring(url.lastIndexOf("/") + 1, url.length());
		this.file = new File(path, fileName);
		if (!this.file.exists()) {
			file.createNewFile();
		}
	}

	/**
	 * 下载链接
	 * 
	 * @return
	 */
	public String getDownloadUrl() {
		return url;
	}

	/**
	 * 已完成进度
	 * 
	 * @return
	 */
	public long getDownloadPercent() {
		return downloadPercent;
	}

	/**
	 * 返回下载文件总大小
	 * 
	 * @return
	 */
	public long getTotalSize() {
		return totalSize;
	}

	/**
	 * 返回下载速度
	 * 
	 * @return
	 */
	public long getDownloadSpeed() {
		return networkSpeed;
	}

	/**
	 * 返回总下载时间
	 * 
	 * @return
	 */
	public long getTotalTime() {
		return totalTime;
	}

	@Override
	protected Integer doInBackground(Void... voids) {
		return download();
	}

	@Override
	protected void onPostExecute(Integer integer) {
		super.onPostExecute(integer);
		if (isCancel) {
			return;
		}
		if (listener != null) {
			if (integer != ParamsManager.ERROR_NONE) {
				listener.errorDownload(this, integer);
			} else {
				listener.finishDownload(this);
			}
		}
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		// 设置开始下载时间
		previousTime = System.currentTimeMillis();
		if (listener != null) {
			listener.preDownload(this);
		}
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
		// 开始下载
		if (values.length > 1) {

		} else {
			totalTime = System.currentTimeMillis() - previousTime;
			downloadSize = values[0];
			downloadPercent = (int) ((downloadSize + previousFileSize) * 100 / totalSize);
			networkSpeed = (int) (downloadSize / totalTime);
			if (listener != null && !isCancel) {
				listener.updateProcess(this);
			}
		}
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
		isCancel = true;
		interrupt = true;
		if (listener != null) {
			listener.cancelDownload(this);
		}
	}

	/** 开始下载文件 */
	private int download() {
		RandomAccessFile outputStream = null;
		InputStream input = null;
		client = AndroidHttpClient.newInstance("DownloadTask");
		HttpGet get = new HttpGet(url);
		try {
			HttpResponse response = client.execute(get);
			totalSize = response.getEntity().getContentLength();
			SqliteManager.getInstance(context).updateDownloadData(url,
					ParamsManager.State_DOWNLOAD, "" + totalSize);
			/** 文件已经下载完成 */
			if (file.length() > 0 && totalSize > 0
					&& totalSize == file.length()) {
				Log.e(ParamsManager.tag, secondTag + "文件已经下载完成");
				return ParamsManager.ERROR_NONE;
			}
			/** 文件没有完成下载 */
			else if (totalSize > 0 && totalSize > file.length()) {
				get.addHeader("Range", "bytes=" + file.length() + "-");
				previousFileSize = file.length();
				client.close();
				client = AndroidHttpClient.newInstance("DownloadTask");
				response = client.execute(get);
				if (CommonUtils.getAvailableStorage() < totalSize
						- file.length()) {
					interrupt = true;
					client.close();
					return ParamsManager.ERROR_SD_NO_MEMORY;
				} else {
					outputStream = new ProgressReportingRandomAccessFile(file,
							"rw");
					/** 准备开始下载 */
					publishProgress(0, (int) totalSize);
					input = response.getEntity().getContent();
					copy(input, outputStream);
					if (interrupt) {
						return ParamsManager.ERROR_BLOCK_INTERNET;
					}
					return ParamsManager.ERROR_NONE;
				}
			} else {
				/** 其他异常 */
				return ParamsManager.ERROR_UNKONW;
			}
		} catch (IOException e) {
			Log.e(ParamsManager.tag, secondTag, e);
			return ParamsManager.ERROR_UNKONW;
		} finally {
			if (client != null) {
				client.close();
				client = null;
			}
		}
	}

	/** 写文件 */
	private int copy(InputStream input, RandomAccessFile out) {
		/** 本次下载总量 */
		int count = 0;
		byte[] buffer = new byte[BUFFER_SIZE];
		BufferedInputStream in = new BufferedInputStream(input, BUFFER_SIZE);
		try {
			out.seek(out.length());
			int n = 0;
			while (!interrupt) {
				n = in.read(buffer, 0, BUFFER_SIZE);
				if (n == -1) {
					break;
				}
				out.write(buffer, 0, n);
				count += n;
			}

		} catch (IOException e) {
			Log.e(ParamsManager.tag, secondTag, e);
			/** 下载过程中出现异常 */
			interrupt = true;
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				Log.e(ParamsManager.tag, secondTag, e);
			}
			try {
				in.close();
			} catch (IOException e) {
				Log.e(ParamsManager.tag, secondTag, e);
			}
			try {
				input.close();
			} catch (IOException e) {
				Log.e(ParamsManager.tag, secondTag, e);
			}
		}
		return count;
	}

	/** 取消下载 */
	public void cancel() {
		onCancelled();
	}
}
