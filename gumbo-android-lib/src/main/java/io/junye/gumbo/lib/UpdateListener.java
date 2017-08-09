package io.junye.gumbo.lib;

/**
 * Created by Junye on 2017/3/23 0023.
 */

public interface UpdateListener {

    void onUpdate(Gumbo gumbo, UpdateInfo info);

    void onLatest();

    void onFailed();
}
