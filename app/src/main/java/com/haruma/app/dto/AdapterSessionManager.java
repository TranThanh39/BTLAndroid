package com.haruma.app.dto;

import com.haruma.app.adapter.CustomAdapter;

public class AdapterSessionManager {

    private static AdapterSessionManager adapterSessionManager;

    private CustomAdapter customAdapter;

    private AdapterSessionManager() {
    }

    public static synchronized AdapterSessionManager getInstance() {
        if (adapterSessionManager == null) {
            adapterSessionManager = new AdapterSessionManager();
        }
        return adapterSessionManager;
    }


    public CustomAdapter getCustomAdapter() {
        return customAdapter;
    }

    public void setCustomAdapter(CustomAdapter customAdapter) {
        this.customAdapter = customAdapter;
    }
}
