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

~~~~
public class MainActivity implements View.OnClickListener, UpdateListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = (TextView) findViewById(R.id.textview);

        Gumbo.setAppKey("AppKey");

        Gumbo.setUpdateUrl("UpdateUrl");

        Gumbo gumbo = new Gumbo(this);
        gumbo.setListener(this);

        textView.setText("version " + BuildConfig.VERSION_CODE);

        findViewById(R.id.button).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        mGumbo.checkUpdate();
    }

    @Override
    public void onUpdate(UpdateInfo info) {
        // Ask user to update.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(info.getTitle())
                .setMessage(info.getUpdateLog())
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // User is sure to install the new apk.
                        mGumbo.install();
                    }
                })
                .setNegativeButton("Cancel",null)
                .show();
    }

    @Override
    public void onLatest() {
        // Currnet version is the latest version.Nothing to do.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("It's the latest version.")
                .setPositiveButton("OK",null)
                .show();
    }

    @Override
    public void onLoading() {
        // Show the loading tips.
    }

    @Override
    public void onFailed() {
        // Failed to check update.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Failed to check update.")
                .setPositiveButton("OK",null)
                .show();
    }
}
~~~~

Enjoy!:\)

