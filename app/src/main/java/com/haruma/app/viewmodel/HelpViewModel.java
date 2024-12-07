package com.haruma.app.viewmodel;

import android.app.Application;
import android.content.Intent;
import android.net.Uri;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.BaseObservable;

import com.haruma.app.model.Callback;

public class HelpViewModel extends BaseObservable {

    private final Application application;

    private final Callback onCall;

    public HelpViewModel(Application application, Callback onCall) {
        this.application = application;
        this.onCall = onCall;
    }

    public void onSMS() {
        Intent smsIntent = new Intent(android.content.Intent.ACTION_VIEW);
        smsIntent.setType("vnd.android-dir/mms-sms");
        smsIntent.putExtra("address","0123456789");
        smsIntent.putExtra("sms_body","your desired message");
        smsIntent.setFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
        this.application.startActivity(smsIntent);
    }

    public void onCall() {
        this.onCall.run();
    }

    public void onEmail() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"android8@nhom5.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Yêu cầu hỗ trợ");
        intent.setType("message/rfc822");
        intent.setFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
        this.application.startActivity(intent);
    }

}
