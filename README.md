
# [jjdxm_download][project] #
## Introduction ##

## Features ##

## Screenshots ##

<img src="https://raw.githubusercontent.com/jjdxmashl/jjdxm_download/master/screenshots/icon01.png" width="300"> 
<img src="https://raw.githubusercontent.com/jjdxmashl/jjdxm_download/master/screenshots/icon02.png" width="300"> 
 
## Download ##

[demo apk下载][downapk]

[下载最新版本aar][lastaar]

[下载最新版本jar][lastjar]

Download or grab via Maven:

	<dependency>
	  <groupId>com.dou361.download</groupId>
	  <artifactId>jjdxm-download</artifactId>
	  <version>x.x.x</version>
	</dependency>

or Gradle:

	compile 'com.dou361.download:jjdxm-download:x.x.x'


jjdxm-download requires at minimum Java 15 or Android 4.0.

## Get Started ##

## 操作步骤 ##
将code目录里面的文件复制项目的eclipse项目的根目录中或者对应目录中
## 使用说明 ##
### 1复制以下代码到Application中的oncreate方法中 ###

    SqliteManager manager=null;

	    @Override
	    public void onCreate() {
	    super.onCreate();

	    //添加下载状态列表
	    manager=SqliteManager.getInstance(getApplicationContext());
	    List<DownloadModel> models=manager.getAllDownloadInfo();
	    DownloadManager.getInstance(getApplicationContext()).addStateMap(models);

    }
### 2在Activity中加入以下代码 ###

	DownloadManager manager=null;

    ArrayList<String> tags=null;

    ListView listview=null;
    MyAdapter adapter=null;

    Handler handler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            View v=listview.findViewWithTag(msg.obj.toString());
            if (v==null) {
                return ;
            }
            switch (msg.what) {
                case ParamsManager.State_NORMAL:
                    ((TextView) v).setText("0");
                    break;
                case ParamsManager.State_DOWNLOAD:
                    Bundle bundle= msg.getData();
                    ((TextView) v).setText(bundle.getString("percent")+"=="+bundle.getString("loadSpeed"));
                    break;
                case ParamsManager.State_FINISH:
                    ((TextView) v).setText("100");
                    break;
                case ParamsManager.State_PAUSE:
                    ((TextView) v).setText("pause");
                    break;
                case ParamsManager.State_WAIT:
                    ((TextView) v).setText("wait");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        manager=DownloadManager.getInstance(getApplicationContext());
        manager.setHandler(handler);

        tags=new ArrayList<String>();

        tags.add("http://gdown.baidu.com/data/wisegame/c841c436b812cd9d/nvshengpai_5.apk");
        tags.add("http://gdown.baidu.com/data/wisegame/6e301de74f680eea/PhotoWonder_130.apk");
        tags.add("http://gdown.baidu.com/data/wisegame/a2d7ebceadb2ddc2/meipai_170.apk");
        tags.add("http://gdown.baidu.com/data/wisegame/0c843031eaaaa6cd/MoXiuLauncher_459.apk");
        tags.add("http://gdown.baidu.com/data/wisegame/25d0855f1586ee81/anzhuobizhi_108.apk");
        tags.add("http://gdown.baidu.com/data/wisegame/7047d6e19b654426/kechenggezi_86.apk");
        tags.add("http://gdown.baidu.com/data/wisegame/be40eed2397d8b10/DouguoRecipe_128.apk");
        tags.add("http://gdown.baidu.com/data/wisegame/de2852bb17d081d9/FingerScanner_64.apk");
        tags.add("http://gdown.baidu.com/data/wisegame/d8748549c0c6995c/baobaozhidao_40.apk");
        tags.add("http://gdown.baidu.com/data/wisegame/a3e60ba4ddf7f50e/wumi_2002001.apk");
        tags.add("http://gdown.baidu.com/data/wisegame/cc41739db6a47fac/suopingjingling_3330.apk");
        tags.add("http://gdown.baidu.com/data/wisegame/083b553642f64881/aipinche_280.apk");
        tags.add("http://gdown.baidu.com/data/wisegame/1a8214b93bcfe24f/babaqunaerqinzibaodian_100.apk");

        listview=(ListView) findViewById(R.id.listview);
        adapter=new MyAdapter(MyActivity.this, tags);
        listview.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        manager.setHandler(null);
    }

### 3对应的Adapter中的getView加入以下代码 ###

		final String tag=tags.get(i);
        final TextView t=holder.adapter_textview;
        t.setTag(tag);
        switch (DownloadManager.getInstance(context).state(tag)) {
            case ParamsManager.State_NORMAL:
                t.setText("0");
                break;
            case ParamsManager.State_WAIT:
                t.setText("wait");
                break;
            case ParamsManager.State_PAUSE:
                t.setText("pause");
                break;
            case ParamsManager.State_FINISH:
                t.setText("100");
                break;
            case ParamsManager.State_DOWNLOAD:
                t.setText("download");
                break;
        }
        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DownloadManager.getInstance(context).isDownloading(tag)) {
                    DownloadManager.getInstance(context).pauseDownload(tag);
                }
                else {
                    DownloadManager.getInstance(context).startDownload(tag);
                }
            }
        });


## More Actions ##

## ChangeLog ##

## About Author ##

#### 个人网站:[http://www.dou361.com][web] ####
#### GitHub:[jjdxmashl][github] ####
#### QQ:316988670 ####
#### 交流QQ群:548545202 ####


## License ##

    Copyright (C) dou361, The Framework Open Source Project
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
     	http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

## (Frequently Asked Questions)FAQ ##
## Bugs Report and Help ##

If you find any bug when using project, please report [here][issues]. Thanks for helping us building a better one.




[web]:http://www.dou361.com
[github]:https://github.com/jjdxmashl/
[project]:https://github.com/jjdxmashl/jjdxm_download/
[issues]:https://github.com/jjdxmashl/jjdxm_download/issues/new
[downapk]:https://raw.githubusercontent.com/jjdxmashl/jjdxm_download/master/apk/app-debug.apk
[lastaar]:https://raw.githubusercontent.com/jjdxmashl/jjdxm_download/master/release/jjdxm-download-1.0.0.aar
[lastjar]:https://raw.githubusercontent.com/jjdxmashl/jjdxm_download/master/release/jjdxm-download-1.0.0.jar
[icon01]:https://raw.githubusercontent.com/jjdxmashl/jjdxm_download/master/screenshots/icon01.png
[icon02]:https://raw.githubusercontent.com/jjdxmashl/jjdxm_download/master/screenshots/icon02.png