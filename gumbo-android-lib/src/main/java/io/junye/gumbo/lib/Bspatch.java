package io.junye.gumbo.lib;

/**
 * Created by Junye on 2017/3/16 0016.
 *
 */

class Bspatch {
    static{
        System.loadLibrary("bspatch");
    }

    public native static int patch(String oldFile,String newFile,String patchFile);

}
