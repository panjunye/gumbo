package io.junye.gumbo.lib;


import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/14 0014.
 * 储存APP版本信息。
 */
public class UpdateInfo implements Serializable{

    private boolean downloaded;

    public UpdateInfo() {
    }

    /**
     * 是否需要更新
     */
    private boolean update;

    /**
     * 新版本的versionCode
     */
    private long versionCode;

    /**
     * 新版本的versionName
     */
    private String versionName;

    /**
     * 新版本的更新日志
     */
    private String updateLog;

    /**
     * 是否增量更新
     */
    private boolean delta;

    /**
     * 下载地址
     */
    private String apkUrl;

    /**
     * 全量更新的APK md5
     */
    private String newMd5;

    /**
     * 增量更新的patch文件的MD5
     */
    private String patchMd5;

    /**
     * 增量更新补丁的下载地址
     */
    private String patchUrl;

    /**
     * 全量更新的APK的文件大小
     */
    private long targetSize;

    /**
     * 增量更新的patch文件的大小
     */
    private long patchSize;

    private String title;

    /**
     * 在本地储存的APK文件的路径
     */
    private String apkPath;

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public long getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(long versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getUpdateLog() {
        return updateLog;
    }

    public void setUpdateLog(String updateLog) {
        this.updateLog = updateLog;
    }

    public boolean isDelta() {
        return delta;
    }

    public void setDelta(boolean delta) {
        this.delta = delta;
    }

    public String getApkUrl() {
        return apkUrl;
    }

    public void setApkUrl(String apkUrl) {
        this.apkUrl = apkUrl;
    }

    public String getNewMd5() {
        return newMd5;
    }

    public void setNewMd5(String newMd5) {
        this.newMd5 = newMd5;
    }

    public String getPatchMd5() {
        return patchMd5;
    }

    public void setPatchMd5(String patchMd5) {
        this.patchMd5 = patchMd5;
    }

    public String getPatchUrl() {
        return patchUrl;
    }

    public void setPatchUrl(String patchUrl) {
        this.patchUrl = patchUrl;
    }

    public long getTargetSize() {
        return targetSize;
    }

    public void setTargetSize(long targetSize) {
        this.targetSize = targetSize;
    }

    public long getPatchSize() {
        return patchSize;
    }

    public void setPatchSize(long patchSize) {
        this.patchSize = patchSize;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getApkPath() {
        return apkPath;
    }

    public void setApkPath(String apkPath) {
        this.apkPath = apkPath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UpdateInfo that = (UpdateInfo) o;

        if (versionCode != that.versionCode) return false;
        return versionName != null ? versionName.equals(that.versionName) : that.versionName == null;

    }

    @Override
    public String toString() {
        return "UpdateInfo{" +
                "downloaded=" + downloaded +
                ", update=" + update +
                ", versionCode=" + versionCode +
                ", versionName='" + versionName + '\'' +
                ", updateLog='" + updateLog + '\'' +
                ", delta=" + delta +
                ", apkUrl='" + apkUrl + '\'' +
                ", newMd5='" + newMd5 + '\'' +
                ", patchMd5='" + patchMd5 + '\'' +
                ", patchUrl='" + patchUrl + '\'' +
                ", targetSize=" + targetSize +
                ", patchSize=" + patchSize +
                ", title='" + title + '\'' +
                ", apkPath='" + apkPath + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        int result = (int) (versionCode ^ (versionCode >>> 32));
        result = 31 * result + (versionName != null ? versionName.hashCode() : 0);
        return result;
    }


    public void setDownloaded(boolean downloaded) {
        this.downloaded = downloaded;
    }

    public boolean isDownloaded() {
        return downloaded;
    }
}
