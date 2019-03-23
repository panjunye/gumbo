# gumbo

An Android delta update library.

## Usage

You also need the update server. See also [gumbo-server](https://github.com/panjunye/gumbo-server)

Add repository.
~~~~
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
~~~~

Add dependencies
~~~~
dependencies {
    compile 'com.github.panjunye:gumbo:-SNAPSHOT'
}
~~~~

Add the below code in AndroidManifest.xml

~~~~
<application>
    ...
    <receiver android:name="io.junye.gumbo.lib.DownloadReceiver">
        <intent-filter>
            <action android:name="android.intent.action.DOWNLOAD_COMPLETE"/>
        </intent-filter>
    </receiver>

    <service android:name="io.junye.gumbo.lib.BspatchService"/>
    ...
</application>
~~~~

Now,make your MainActivity implements UpdateListener and replace AppKey and UpdateUrl with yours.

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

