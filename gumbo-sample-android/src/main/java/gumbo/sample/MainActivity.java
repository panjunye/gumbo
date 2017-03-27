package gumbo.sample;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import io.junye.gumbo.lib.Gumbo;
import io.junye.gumbo.lib.UpdateInfo;
import io.junye.gumbo.lib.UpdateListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, UpdateListener {

    private TextView mTextView;
    private Button mBtnCheckUpdate;
    private Gumbo mGumbo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(R.id.textview);
        mBtnCheckUpdate = (Button) findViewById(R.id.btn_check_update);

        Gumbo.setAppKey("8619455141c24387993493955a446484");
        Gumbo.setUpdateUrl("http://172.16.100.28/api/checkupdate/");
        mGumbo = new Gumbo(this);
        mGumbo.setListener(this);


        mTextView.setText("version:" + BuildConfig.VERSION_CODE);
        mBtnCheckUpdate.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        mGumbo.checkUpdate();
    }

    @Override
    public void onUpdate(UpdateInfo info) {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(info.getTitle())
                .setMessage(info.getUpdateLog())
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mGumbo.install();
                    }
                })
                .setNegativeButton("取消",null)
                .show();
    }

    @Override
    public void onLatest() {
        // 检测更新失败
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("已是最新版本")
                .setPositiveButton("确定",null)
                .show();
    }

    @Override
    public void onLoading() {
        // 显示加载提示
    }

    @Override
    public void onFailed() {
        // 检测更新失败
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("检测更新失败")
                .setPositiveButton("确定",null)
                .show();
    }



}
