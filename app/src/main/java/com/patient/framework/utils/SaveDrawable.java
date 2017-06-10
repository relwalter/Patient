package com.patient.framework.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SaveDrawable {

    public static void saveDrawableById(Resources res, int id, String filePath) {
        Drawable drawable = idToDrawable(res,id);
        Bitmap bitmap = drawableToBitmap(drawable);
        saveBitmap(bitmap, filePath, Bitmap.CompressFormat.JPEG);
    }

    /**
     * 将资源ID转化为Drawable
     */
    public static Drawable idToDrawable(Resources res,int id) {
        return res.getDrawable(id);
    }

    /**
     * 将Drawable转化为Bitmap
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        if(drawable == null)
            return null;
        return ((BitmapDrawable)drawable).getBitmap();
    }

    /**
     * 将Bitmap以指定格式保存到指定路径
     */
    public static void saveBitmap(Bitmap bitmap, String dir, Bitmap.CompressFormat format) {
        // 创建一个位于SD卡上的文件
        File file = new File(dir);
        if(file.exists()){
            Log.d("file","already exists");
            return;
        }else{
            FileOutputStream out = null;
            try{
                // 打开指定文件输出流
                out = new FileOutputStream(file);
                // 将位图输出到指定文件
                bitmap.compress(format, 90, out);
                out.close();
                Log.d("file","output complete");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
