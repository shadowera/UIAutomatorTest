package kuyun.com.uiautomatortest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class Utils {

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
}  