package com.example.arnon.userservice.fragment;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arnon.userservice.activity.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by arnon on 15/7/2559.
 */
    public class MethodTask extends AsyncTask<Void, String, Void> {

//        public static MethodTask methodTask;

        public interface TaskCallbacks {
            void onPreExecute();
            void onProgressUpdate(String... values);
            void onCancelled();
            void onPostExecute(Void aVoid);
        }

        static MethodTask instance;
        SharedPreferences sharedPreferences;

        String dstAddr;
        int dstPort;
        String username;
        String password;
        String sUsername;
        String sPassword;
        String socketNumber;
        String fileName;
        String currentText = "";
        String timeout;

        private Context context;
        private TaskCallbacks callbacks = null;




        public MethodTask(Context context){
            this.context = context;
            sharedPreferences = this.context.getSharedPreferences("connection", Context.MODE_PRIVATE);

//            this.methodTask = new MethodTask(context);
        }

        public static MethodTask getInstance(Context context){
            if(instance == null)
                instance = new MethodTask(context);
            return instance;
        }

        public void setContext(Context context){
            this.context = context;
        }

        public void setCallback(TaskCallbacks newCallback) {
            callbacks = newCallback;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(callbacks != null)
                callbacks.onPreExecute();
            dstAddr = sharedPreferences.getString("dstAddr", "192.168.5.53");
            dstPort = sharedPreferences.getInt("dstPort", 8000);
            username = sharedPreferences.getString("username", "arnon");
            password = sharedPreferences.getString("password", "a.546548688465");
            socketNumber = sharedPreferences.getString("socketNumber", "22");
            fileName = sharedPreferences.getString("fileName", "connect.sh");
            sUsername = sharedPreferences.getString("sUsername", "root");
            sPassword = sharedPreferences.getString("sPassword", "dd");
            timeout = sharedPreferences.getString("timeout", "20");
        }

        @Override
        protected Void doInBackground(Void... params) {
            Socket socket = null;
            String result = null;
            try {
                publishProgress("connecting to server......");
                socket = new Socket(dstAddr, dstPort);
                if(socket.isConnected()) {
                    publishProgress("connected to server......");
                }
                PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
                JSONObject js = new JSONObject();
                js.put("username", username);
                js.put("password", password);
                js.put("sUsername", sUsername);
                js.put("sPassword", sPassword);
                js.put("fileName", fileName);
                js.put("socketNumber", socketNumber);
                js.put("timeout", "20");
                pw.write(js.toString());
                pw.flush();

                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String line = "";


                while((line = br.readLine()) != null){
                    Log.d("Line: ", line);
                    publishProgress(line);
                }

            } catch (IOException e) {
                publishProgress("..............can't connect to server");
                publishProgress("..............Failed to connect");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            Log.d("FUCKING_TASK", values[0]);
            currentText += "\n" + values[0];
            if(callbacks != null) {
                callbacks.onProgressUpdate(currentText);
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(callbacks != null)
                callbacks.onPostExecute(aVoid);
            instance = null;

        }
}
