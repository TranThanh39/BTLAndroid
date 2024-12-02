package com.haruma.app.service;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;

public class UriHelper {

    public static String resolveUri(Context context, Uri uri) {
        String result = null;
        String provider = uri.getAuthority();
        String path = uri.getPath();
        if (path != null) {
            path = Uri.decode(path);
        }

        if ("com.android.externalstorage.documents".equals(provider)) {
            if (path != null) {
                if (path.startsWith("/document/primary:")) {
                    result = path.substring("/document/primary:".length());
                    result = queryExternalStoragePath() + (result.isEmpty() ? "" : "/" + result);
                } else if (path.startsWith("/tree/primary:")) {
                    result = path.substring("/tree/primary:".length());
                    result = queryExternalStoragePath() + (result.isEmpty() ? "" : "/" + result);
                }
            }
        } else if ("me.zhanghai.android.files.file_provider".equals(provider)) {
            if (path != null && path.startsWith("/file://")) {
                result = Uri.parse(path.substring(1)).getPath();
            }
        } else if ("com.speedsoftware.rootexplorer.fileprovider".equals(provider)) {
            if (path != null && path.startsWith("/root/")) {
                result = path.substring("/root".length());
            }
        } else if ("pl.solidexplorer2.files".equals(provider) ||
                "bin.mt.plus.fp".equals(provider) ||
                "in.mfile.files".equals(provider)) {
            result = path;
        } else {
            if (path != null && path.startsWith("/") && exist(path)) {
                result = path;
            }
        }

        return result;
    }

    private static String queryExternalStoragePath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    private static boolean exist(String path) {
        return new java.io.File(path).exists();
    }
}

