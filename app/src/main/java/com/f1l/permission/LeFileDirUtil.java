package com.f1l.permission;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by songxudong
 * on 2016/1/8.
 * SD卡目录使用工具
 */
public class LeFileDirUtil {
    private static final String LE_BASE_DIR = File.separator+"lesports";

    public static final String MODEL_GALARY = File.separator+"galary";
    public static final String MODEL_FEED = File.separator+"feed";

    public static final String MODEL_GALARY_PIC = MODEL_GALARY+File.separator+"picture";
    public static final String MODEL_FEED_PIC = MODEL_FEED+File.separator+"tempupload";

    public static String getGalaryPicDir (Context context) {
        return getLeBaseDir(context)+MODEL_GALARY_PIC;
    }
    public static String getFeedUploadTempDir (Context context) {
        return getLeBaseDir(context)+MODEL_FEED_PIC;
    }

    public static String getLeBaseDir (Context context) {
        return getRootDir(context)+LE_BASE_DIR;
    }

    public static String getRootDir (Context context) {
        boolean hasSDCard = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if(hasSDCard)
        {
            return Environment.getExternalStorageDirectory().getPath();
        }
        else
        {
            return context.getCacheDir().getAbsolutePath();
        }
    }

}
