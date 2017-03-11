package com.example.arnon.userservice;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.example.arnon.userservice.fragment.MainFragment;

/**
 * Created by arnon on 28/6/2559.
 */
public interface MainInterface {
    void onConnectSucceeded(String username, String password, String socketNumber);
}
