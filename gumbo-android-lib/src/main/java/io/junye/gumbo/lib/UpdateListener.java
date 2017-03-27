package io.junye.gumbo.lib;

/**
 * Created by Junye on 2017/3/23 0023.
 */

public interface UpdateListener {

    void onUpdate(UpdateInfo info);

    void onLatest();

    void onLoading();

    void onFailed();
}
