package com.example.arnon.userservice.fragment;//import android.app.Fragment;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arnon.userservice.MainInterface;
import com.example.arnon.userservice.R;
import com.example.arnon.userservice.activity.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by arnon on 27/6/2559.
 */
public class MainFragment extends Fragment {

//    MethodConsoleFragment methodConsoleFragment;


    MethodTask methodTask;
    ArrayAdapter<String> arrayAdapter;

    LinearLayout mainLayout;
    ListView lv;

    String username;
    String password;
    String sUsername;
    String sPassword;
    String socketNumber;
    String timeout;
    String dstAddr;
    String dstMethod;
    int dstPort;

    SharedPreferences sharedPreferences;

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d("DEBUG", "MainFragment onCreateView");
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        initInstance(rootView);
        return rootView;
    }

    @Override
    public void onResume() {
        Log.d("DEBUG", "MainFragment onResume");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.d("DEBUG", "MainFragment onPause");
        super.onPause();
    }

    public void initInstance(View v) {
//        methodConsoleFragment = (MethodConsoleFragment) (getActivity().getSupportFragmentManager().findFragmentByTag("methodConsole"));
//        if(methodConsoleFragment != null){
//            Log.d("initInstance", "same methodConsole");
////            methodConsoleFragment = (MethodConsoleFragment) (getActivity().getSupportFragmentManager().findFragmentByTag("methodConsole"));
//        } else {
//            Log.d("initInstance", "new methodConsole");
//            methodConsoleFragment = new MethodConsoleFragment();
//        }

//        methodTask = new MethodTask(getContext());

        sharedPreferences = getActivity().getSharedPreferences("connection", Context.MODE_PRIVATE);

        username = sharedPreferences.getString("username", "");
        password = sharedPreferences.getString("password", "");
        sUsername = sharedPreferences.getString("sUsername", "");
        sPassword = sharedPreferences.getString("sPassword", "");
        socketNumber = sharedPreferences.getString("socketNumber", "");
        timeout = sharedPreferences.getString("timeout", "");
        dstAddr = sharedPreferences.getString("dstAddr", "");
        dstMethod = sharedPreferences.getString("dstMethod", "method0");
        dstPort = sharedPreferences.getInt("dstPort", 8000);

        mainLayout = (LinearLayout) v.findViewById(R.id.mainLayout);
        lv = (ListView) v.findViewById(R.id.methodList);
        lv.setOnItemClickListener(onItemClickListener);

        createList();
        setEnableAllView(false);

    }

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//            boolean isProcess = sharedPreferences.getBoolean("isProcess", false);
            String fileName = lv.getItemAtPosition(position).toString();
            String processFile = sharedPreferences.getString("fileName", "");

            fileName = fileName.replace(" ", "_") + ".sh";


            Log.d("METHOD", "fileName: " + fileName);

            if(methodTask == null){
                Log.d("CHECK_ALIVE", "DEAD");
                methodTask = MethodTask.getInstance(getContext());
            }
            Log.d("FUCKING", "" + methodTask.getStatus());
            if(methodTask.getStatus() == AsyncTask.Status.RUNNING){
                if(fileName.equals(processFile)){
                    //process running, same file
                    Log.d("FUCK_TASK", "on process running, same file");
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, MethodConsoleFragment.newInstance(methodTask), "methodConsole")
                            .addToBackStack(null)
                            .commit();

                }else{
                    //process running, other file
                    Log.d("FUCK_TASK", "on process running, other file");
                    Toast.makeText(getActivity(), "process is running", Toast.LENGTH_SHORT).show();
                }
            }else{//process stop
                Log.d("FUCK_TASK", "on process stop");
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("fileName", fileName);
                editor.commit();

                getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MethodConsoleFragment.newInstance(methodTask), "methodConsole")
                    .addToBackStack(null)
                    .commit();
                methodTask.execute();
                methodTask = null;
            }



        }





//            if (fileName.equals(processFile)) {
//
//                if (!isProcess) {
//
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    editor.putBoolean("isProcess", true);
//                    editor.putString("fileName", fileName);
//                    editor.commit();
//
//                }else{
////                    methodConsoleFragment.onCreate(null);
//                }
//
//
//                MethodTask methodTask = new MethodTask(getContext());
//                getActivity().getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.container, MethodConsoleFragment.newInstance(methodTask), "methodConsole")
//                        .addToBackStack(null)
//                        .commit();
//
//
//
//            } else {
//
//                if(!isProcess){
//
//                    Toast.makeText(getActivity(), "not the same file, process stop", Toast.LENGTH_SHORT).show();
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    editor.putBoolean("isProcess", true);
//                    editor.putString("fileName", fileName);
//                    editor.commit();
//
//                    getActivity().getSupportFragmentManager().beginTransaction()
//                            .replace(R.id.container, methodConsoleFragment, "methodConsole")
//                            .addToBackStack(null)
//                            .commit();
//
//                }else{
//                    Toast.makeText(getActivity(), "not the same file, process run", Toast.LENGTH_SHORT).show();
//                }
//
//
//                }
//            }
        };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
//        Log.d("CHECK_onSave", "MainFragment Saved");
        outState.putString("username", username);
        outState.putString("password", password);
        outState.putString("socketNumber", socketNumber);
        outState.putString("timeout", timeout);
        outState.putString("dstAddr", dstAddr);
        outState.putInt("dstPort", dstPort);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState != null){
            username = savedInstanceState.getString("username");
            password = savedInstanceState.getString("password");
            socketNumber = savedInstanceState.getString("socketNumber");
            timeout = savedInstanceState.getString("timeout");
            dstAddr = savedInstanceState.getString("dstAddr");
            dstPort = savedInstanceState.getInt("dstPort");
        }
    }



    public android.app.AlertDialog showResult(JSONObject jsonObject) throws JSONException {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        try {
            builder.setTitle((CharSequence) jsonObject.get("result"))
                    .setMessage(jsonObject.get("values").toString())
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return builder.create();
    }

    public void showSomething(){
        Toast.makeText(getActivity(), "you can do something with it Bro XD", Toast.LENGTH_SHORT).show();
    }
    public void createList(){
        boolean isConnect = sharedPreferences.getBoolean("isConnect", false);
        Set<String> s = new TreeSet<>();
        Set<String> fileSet = new HashSet<String>();
        s.add("can not do this yet, please check your connection");

        Log.d("DEBUG", "CHECK createList");
        if(isConnect) {
            fileSet = sharedPreferences.getStringSet("fileSet", s);
        }else{
            fileSet = s;
        }
        Log.d("DEBUG", "CHECK fileSet" + fileSet);
        String[] fileString = fileSet.toArray(new String[fileSet.size()]);
        Arrays.sort(fileString);
        arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_method, fileString);
        lv.setAdapter(arrayAdapter);
        setEnableAllView(isConnect);
    }

    public void setEnableAllView(boolean set){
        for(int i=0; i < mainLayout.getChildCount(); i++) {
            View child = mainLayout.getChildAt(i);
            child.setEnabled(set);
        }
    }

}
