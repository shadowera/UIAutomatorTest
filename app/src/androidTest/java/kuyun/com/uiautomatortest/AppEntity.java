package kuyun.com.uiautomatortest;

import android.graphics.drawable.Drawable;

public class AppEntity {
    public Drawable appIcon;
    private String appName;
    private String packageName;
    private double memorySize;
    private double pss;

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public double getMemorySize() {
        return memorySize;
    }

    public void setMemorySize(double memorySize) {
        this.memorySize = memorySize;
    }

    public double getPss() {
        return pss;
    }

    public void setPss(double pss) {
        this.pss = pss;
    }
}