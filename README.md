
# [jjdxm_download][project] #
### Copyright notice ###

我在网上写的文章、项目都可以转载，但请注明出处，这是我唯一的要求。当然纯我个人原创的成果被转载了，不注明出处也是没有关系的，但是由我转载或者借鉴了别人的成果的请注明他人的出处，算是对前辈们的一种尊重吧！

虽然我支持写禁止转载的作者，这是他们的成果，他们有这个权利，但我不觉得强行扭转用户习惯会有一个很好的结果。纯属个人的观点，没有特别的意思。可能我是一个版权意识很差的人吧，所以以前用了前辈们的文章、项目有很多都没有注明出处，实在是抱歉！有想起或看到的我都会逐一补回去。

从一开始，就没指望从我写的文章、项目上获得什么回报，一方面是为了自己以后能够快速的回忆起曾经做过的事情，避免重复造轮子做无意义的事，另一方面是为了锻炼下写文档、文字组织的能力和经验。如果在方便自己的同时，对你们也有很大帮助，自然是求之不得的事了。要是有人转载或使用了我的东西觉得有帮助想要打赏给我，多少都行哈，心里却很开心，被人认可总归是件令人愉悦的事情。

站在了前辈们的肩膀上，才能走得更远视野更广。前辈们写的文章、项目给我带来了很多知识和帮助，我没有理由不去努力，没有理由不让自己成长的更好。写出好的东西于人于己都是好的，但是由于本人自身视野和能力水平有限，错误或者不好的望多多指点交流。

项目中如有不同程度的参考借鉴前辈们的文章、项目会在下面注明出处的，纯属为了个人以后开发工作或者文档能力的方便。如有侵犯到您的合法权益，对您造成了困惑，请联系协商解决，望多多谅解哈！若您也有共同的兴趣交流技术上的问题加入交流群QQ： 548545202

## Introduction ##

## Features ##

## Screenshots ##

<img src="https://raw.githubusercontent.com/jjdxmashl/jjdxm_download/master/screenshots/icon01.png" width="300"> 
<img src="https://raw.githubusercontent.com/jjdxmashl/jjdxm_download/master/screenshots/icon02.png" width="300"> 
 
## Download ##

[demo apk下载][downapk]

Download or grab via Maven:

	<dependency>
	  <groupId>com.dou361.download</groupId>
	  <artifactId>jjdxm-download</artifactId>
	  <version>x.x.x</version>
	</dependency>

or Gradle:

	compile 'com.dou361.download:jjdxm-download:x.x.x'


历史版本：

	compile 'com.dou361.download:jjdxm-download:1.0.0'

jjdxm-download requires at minimum Java 15 or Android 4.0.


[架包的打包引用以及冲突解决][jaraar]

## Proguard ##

根据你的混淆器配置和使用，您可能需要在你的proguard文件内配置以下内容：

	-keep com.dou361.download.** {
    *;
	}


[AndroidStudio代码混淆注意的问题][minify]

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
[jaraar]:https://github.com/jjdxmashl/jjdxm_ecodingprocess/blob/master/架包的打包引用以及冲突解决.md
[minify]:https://github.com/jjdxmashl/jjdxm_ecodingprocess/blob/master/AndroidStudio代码混淆注意的问题.md