package com.example.arnon.userservice.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arnon.userservice.R;
import com.example.arnon.userservice.activity.MainActivity;

/**
 * Created by arnon on 4/7/2559.
 */
public class SettingsFragment extends Fragment{

    EditText etUsername;
    EditText etPassword;
    EditText etAddr;
    EditText etPort;
    EditText etTimeout;
    Button btnSave;

    String sUsername;
    String sPassword;
    String timeout;
    String dstAddr;
    int dstPort;

    SharedPreferences s;
    public static SettingsFragment newInstance(){
        return new SettingsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        initInstance(rootView);
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
//        outState.putString("socketNumber", socketNumber);
        super.onSaveInstanceState(outState);

    }

    public void initInstance(View v){

        s = getActivity().getSharedPreferences("connection", Context.MODE_PRIVATE);

        dstAddr = s.getString("dstAddr", "192.168.5.53");
        dstPort = s.getInt("dstPort", 8000);
        timeout = s.getString("timeout", "3");
        sUsername = s.getString("sUsername", "");
        sPassword = s.getString("sPassword", "");

        etUsername = (EditText) v.findViewById(R.id.etUsername);
        etUsername.setText(sUsername);

        etPassword = (EditText) v.findViewById(R.id.etPassword);
        etPassword.setText(sPassword);

        etAddr = (EditText) v.findViewById(R.id.etAddr);
        etAddr.setText(dstAddr);

        etPort = (EditText) v.findViewById(R.id.etPort);
        etPort.setText("" + dstPort);

        etTimeout = (EditText) v.findViewById(R.id.etTimeout);
        etTimeout.setText(timeout);

        btnSave = (Button) v.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(saveListenner);
    }

    View.OnClickListener saveListenner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //get now values
            dstAddr = etAddr.getText().toString();
            dstPort = Integer.parseInt(etPort.getText().toString());
            sUsername = etUsername.getText().toString();
            sPassword = etPassword.getText().toString();
            timeout = etTimeout.getText().toString();

            SharedPreferences.Editor editor = s.edit();
            editor.putString("dstAddr", dstAddr);
            editor.putInt("dstPort", dstPort);
            editor.putString("sUsername", sUsername);
            editor.putString("sPassword", sPassword);
            editor.putString("timeout", timeout);
            editor.commit();

//            getActivity().getSupportFragmentManager().popBackStack();
            Toast.makeText(getActivity(), "SAVED", Toast.LENGTH_SHORT).show();
//            Log.d("DEBUG", "DATA SENT TO ACTIVITY");
        }
    };

}
