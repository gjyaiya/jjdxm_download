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

