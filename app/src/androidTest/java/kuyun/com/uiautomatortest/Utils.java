package kuyun.com.uiautomatortest;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Debug;
import android.os.Environment;
import android.util.Log;

import com.jaredrummler.android.processes.AndroidProcesses;
import com.jaredrummler.android.processes.ProcessManager;
import com.jaredrummler.android.processes.models.AndroidAppProcess;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Utils {
    public static String kuyun_packagename = "com.kuyun.common.androidtv";

    public static boolean sameAs(String path1, String path2) throws FileNotFoundException {
        FileInputStream fis1 = new FileInputStream(path1);
        Bitmap bitmap1 = BitmapFactory.decodeStream(fis1);

        FileInputStream fis2 = new FileInputStream(path2);
        Bitmap bitmap2 = BitmapFactory.decodeStream(fis2);

        return sameAs(bitmap1, bitmap2);
    }

    public static boolean sameAs(String path1, String path2, double percent) throws FileNotFoundException {
        FileInputStream fis1 = new FileInputStream(path1);
        Bitmap bitmap1 = BitmapFactory.decodeStream(fis1);

        FileInputStream fis2 = new FileInputStream(path2);
        Bitmap bitmap2 = BitmapFactory.decodeStream(fis2);

        return sameAs(bitmap1, bitmap2, percent);

    }

    public static boolean sameAs(Bitmap bitmap1, Bitmap bitmap2, double percent) {
        if (bitmap1.getHeight() != bitmap2.getHeight())
            return false;

        if (bitmap1.getWidth() != bitmap2.getWidth())
            return false;

        if (bitmap1.getConfig() != bitmap2.getConfig())
            return false;

        int width = bitmap1.getWidth();
        int height = bitmap2.getHeight();

        int numDiffPixels = 0;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (bitmap1.getPixel(x, y) != bitmap2.getPixel(x, y)) {
                    numDiffPixels++;
                }
            }
        }
        double numberPixels = height * width;
        double diffPercent = numDiffPixels / numberPixels;
        return percent <= 1.0D - diffPercent;
    }

    public static boolean sameAs(Bitmap bmp1, Bitmap bmp2) throws FileNotFoundException {
        return bmp1.sameAs(bmp2);
    }

    public static Bitmap getSubImage(String path, int x, int y, int width, int height) throws FileNotFoundException {
        FileInputStream fis = new FileInputStream(path);
        Bitmap bitmap = BitmapFactory.decodeStream(fis);
        return Bitmap.createBitmap(bitmap, x, y, width, height);
    }

    public static Bitmap getSubImage(String path, Rect rect) throws FileNotFoundException {
        FileInputStream fis = new FileInputStream(path);
        Bitmap bitmap = BitmapFactory.decodeStream(fis);
        return Bitmap.createBitmap(bitmap, rect.left, rect.top, rect.width(), rect.height());
    }

    public static void saveBitMapToSdcard(Bitmap bitmap, String newName) {
        String dir = String.format("%s/%s", Environment.getExternalStorageDirectory().getPath(), "test-screenshots");
        File theDir = new File(dir);
        if (!theDir.exists()) {
            theDir.mkdir();
        }
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(new File(theDir.getPath(), newName));
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String getScreenShootPath() {
        return Environment.getExternalStorageDirectory().getPath() + "/test-screenshots";
    }

    private static float getSimilarity(float[] fingerPrint, float[] print) {
        int length = fingerPrint.length;
        if (length != print.length)
            return 0;
        int total = 0;
        for (int j = 0; j < length; j++) {
            if (fingerPrint[j] == print[j]) {
                total++;
            }
        }
        return total * 1f / length;
    }

    public static float[] getFingerPrint(String fileName) {
        Bitmap bm_check;
        FileInputStream is = null;
        try {
            is = new FileInputStream(fileName);
            bm_check = BitmapFactory.decodeStream(is);
            int width = bm_check.getWidth();
            int height = bm_check.getHeight();
            int[] pixels = new int[width * height];
            float[] srcPixels = new float[width * height];
            bm_check.getPixels(pixels, 0, width, 0, 0, width, height);
            long total = 0;
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < width; j++) {
                    int pos = width * i + j;
                    int color = pixels[pos];
                    float gray = Color.red(color) * 0.3f + Color.green(color) * 0.59f + Color.blue(color) * 0.11f;
                    srcPixels[pos] = gray;
                    total += gray;
                }
            }
            float ave = total / srcPixels.length;
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < width; j++) {
                    int pos = width * i + j;
                    if (srcPixels[pos] > ave) {
                        srcPixels[pos] = 1;
                    } else {
                        srcPixels[pos] = 0;
                    }
                }
            }
            return srcPixels;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static boolean similarAs(String path1, String path2, double percent) throws FileNotFoundException {
        float[] fingerPrint1 = getFingerPrint(path1);
        float[] fingerPrint2 = getFingerPrint(path2);

        float similarity = getSimilarity(fingerPrint1, fingerPrint2);
        return similarity >= percent;
    }

    public static float getSimilarity(String path1, String path2) {
        float[] fingerPrint1 = getFingerPrint(path1);
        float[] fingerPrint2 = getFingerPrint(path2);
        return getSimilarity(fingerPrint1, fingerPrint2);
    }


    /*public static void logKuyunMemory() {
         CommandExecution.CommandResult commandResult = CommandExecution.execCommand("dumpsys meminfo com.kuyun.common.androidtv", false);
         UiAutomationLog(commandResult.successMsg);
         UiAutomationLog(commandResult.errorMsg);
     }*/

    /**
     * 在log文件中打印kuyun主程序内存信息
     *
     * @param msg     附加信息
     * @param context
     */
    public static void logKuyunMemory(String msg, Context context) {
        if (Build.VERSION.SDK_INT < 21) {
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> procInfo = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : procInfo) {
                System.out.println(runningAppProcessInfo.processName + String.format(",pid = %d", runningAppProcessInfo.pid));
                if (runningAppProcessInfo.processName.indexOf(kuyun_packagename) != -1) {
                    int pids[] = {runningAppProcessInfo.pid};
                    Debug.MemoryInfo self_mi[] = am.getProcessMemoryInfo(pids);
                    StringBuffer strbuf = new StringBuffer();
                    strbuf.append(" proccess Name:").append(runningAppProcessInfo.processName)
                            .append("\n pid:").append(runningAppProcessInfo.pid)
                            .append("\n dalvikPrivateDirty:").append(self_mi[0].dalvikPrivateDirty)
                            .append("\n dalvikPss:").append(self_mi[0].dalvikPss)
                            .append("\n dalvikSharedDirty:").append(self_mi[0].dalvikSharedDirty)
                            .append("\n nativePrivateDirty:").append(self_mi[0].nativePrivateDirty)
                            .append("\n nativePss:").append(self_mi[0].nativePss)
                            .append("\n nativeSharedDirty:").append(self_mi[0].nativeSharedDirty)
                            .append("\n otherPrivateDirty:").append(self_mi[0].otherPrivateDirty)
                            .append("\n otherPss:").append(self_mi[0].otherPss)
                            .append("\n otherSharedDirty:").append(self_mi[0].otherSharedDirty)
                            .append("\n TotalPrivateDirty:").append(self_mi[0].getTotalPrivateDirty())
                            .append("\n TotalPss:").append(self_mi[0].getTotalPss())
                            .append("\n TotalSharedDirty:").append(self_mi[0].getTotalSharedDirty());
                    UiAutomationLog(msg + (strbuf.toString()));
                }
            }
        } else {
            List<AppEntity> androidProcess = Utils.getAndroidProcess(context);
            boolean isKuyunProcessOn = false;
            if (androidProcess != null) {
                for (AppEntity app : androidProcess) {
                    if (kuyun_packagename.equals(app.getPackageName())) {
                        isKuyunProcessOn = true;
                        Utils.UiAutomationLog(msg + ":" + app.getPackageName() + ",pss=" + app.getPss());
                    }
                }
            }
            if (!isKuyunProcessOn) {
                UiAutomationLog(msg + ":" + "kuyun主程序未启动");
            }
        }

    }

   /* public static void logMemory(String msg) throws IOException, InterruptedException {
        CommandExecution.CommandResult commandResult = CommandExecution.execCommand("/system/xbin/procrank", true);
        if (commandResult != null) {
            Log.d(msg, commandResult.successMsg + "");
            Log.d(msg, commandResult.errorMsg + "");
        }
       *//* String[] cmd = {"/system/xbin/procrank"};
        Process process = new ProcessBuilder()
                .command(cmd)
                .redirectErrorStream(true)
                .start();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(process.getInputStream()));
        String line = "";
        while ((line = in.readLine()) != null) {
            UiAutomationLog("logMemory" + "Line=" + line);
        }
        process.waitFor();*//*
    }*/


    public static String m_logpathString = "/PerformanceLog.txt";

    /**
     * 打log
     * 文件地址/mnt/sdcard/PerformanceLog.txt
     */
    public static void UiAutomationLog(String str) {
        // 取得当前时间
        String logPath = Environment.getExternalStorageDirectory().getAbsolutePath() + m_logpathString;
        File file = new File(logPath);
        Log.d("path", logPath);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        String datestr = calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND) + ":" + calendar.get(Calendar.MILLISECOND) + ":";

        FileWriter fwlog = null;
        try {
            fwlog = new FileWriter(logPath, true);
            fwlog.write(datestr + str + "\r\n");
            System.out.println(datestr + str);
            fwlog.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fwlog != null) {
                    fwlog.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String sudoForResult(String... strings) {
        String res = "";
        DataOutputStream outputStream = null;
        InputStream response = null;
        try {
            Process su = Runtime.getRuntime().exec("su");
            outputStream = new DataOutputStream(su.getOutputStream());
            response = su.getInputStream();

            for (String s : strings) {
                outputStream.writeBytes(s + "\n");
                outputStream.flush();
            }

            outputStream.writeBytes("exit\n");
            outputStream.flush();
            try {
                su.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            res = readFully(response);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return res;
    }

    public static String readFully(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length = 0;
        while ((length = is.read(buffer)) != -1) {
            baos.write(buffer, 0, length);
        }
        return baos.toString("UTF-8");
    }

    public static List<AppEntity> getAndroidProcess(Context context) {
        List<AppEntity> resule = new ArrayList<AppEntity>();
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        PackageManager pm = context.getPackageManager();
        AppUtils proutils = new AppUtils(context);
        List<AndroidAppProcess> listInfo = AndroidProcesses.getRunningAppProcesses();
        if (listInfo.isEmpty() || listInfo.size() == 0) {
            return null;
        }
        for (AndroidAppProcess info : listInfo) {
            ApplicationInfo app = proutils.getApplicationInfo(info.name);
            // 过滤自己当前的应用
            if (app == null || context.getPackageName().equals(app.packageName)) {
                continue;
            }
            // 过滤系统的应用
            if ((app.flags & app.FLAG_SYSTEM) > 0) {
                continue;
            }
            AppEntity ent = new AppEntity();
            ent.setAppIcon(app.loadIcon(pm));//应用的图标
            ent.setAppName(app.loadLabel(pm).toString());//应用的名称
            ent.setPackageName(app.packageName);//应用的包名
            // 计算应用所占内存大小
            int[] myMempid = new int[]{info.pid};
            Debug.MemoryInfo[] memoryInfo = am.getProcessMemoryInfo(myMempid);
            double memSize = memoryInfo[0].dalvikPrivateDirty;
            int totalPss = memoryInfo[0].getTotalPss();
            ent.setPss(totalPss);
            int temp = (int) (memSize * 100);
            memSize = temp / 100.0;
            ent.setMemorySize(memSize);//应用所占内存的大小

            resule.add(ent);
        }
        return resule;
    }
}