# gumbo
一个Android增量更新库

## 用法
使用之前，需要启动增量更新服务器，详见 [gumbo-server](https://github.com/panjunye/gumbo-server)

在gradle文件中添加下面配置
~~~~
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
~~~~

添加依赖
~~~~
dependencies {
    compile 'com.github.panjunye:gumbo:-SNAPSHOT'
}
~~~~

现在，让MainActivity实现UpdateListener接口，将AppKey和UpdateUrl替换成你的.UpdateUrl就是[gumbo-server](https://github.com/panjunye/gumbo-server)的更新api的地址。

~~~~ Java
public class MainActivity implements View.OnClickListener, UpdateListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Gumbo.setAppKey("AppKey");
        Gumbo.setUpdateUrl("UpdateUrl");

        Gumbo gumbo = new Gumbo(this);
        gumbo.setListener(this);
        findViewById(R.id.button).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        mGumbo.checkUpdate(); // 检测更新
    }

    @Override
    public void onUpdate(UpdateInfo info) {
        // 有新版本需要更新
    }

    @Override
    public void onLatest() {
        // 当前版本是最新的，无需更新
    }

    @Override
    public void onLoading() {
        // 正在请求服务器，可现实加载动画
    }

    @Override
    public void onFailed() {
        // 检测更新失败
    }
}
~~~~

Enjoy!:\)

