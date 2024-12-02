package com.haruma.app.service;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;

import androidx.annotation.RequiresApi;

public class FileHelper {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static Intent saveFile() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, Uri.parse("content://com.android.externalstorage.documents/document/primary%3A" + Uri.encode("")));
        return intent;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static Intent pickFile() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, Uri.parse("content://com.android.externalstorage.documents/document/primary%3A" + Uri.encode("")));
        intent.setType("*/*");
        return intent;
    }

}
