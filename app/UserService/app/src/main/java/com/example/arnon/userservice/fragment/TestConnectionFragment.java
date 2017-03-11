package com.example.arnon.userservice.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.*;

import com.example.arnon.userservice.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by arnon on 27/6/2559.
 */
public class TestConnectionFragment extends Fragment {

    TextView tvServerStatus;
    TextView tvPcStatus;

    EditText etUsername;
    EditText etPassword;
    EditText etSocketNumber;

    Button btnConnect;
    String sUsername;
    String sPassword;
    String dstAddr;
    String username;
    String password;
    String socketNumber;
    int dstPort;

    SharedPreferences sharedPreferences;

    public static TestConnectionFragment newInstance(){
        TestConnectionFragment fragment = new TestConnectionFragment();
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("DEBUG", "TestConnectionFragment onCreateView");
        View rootView = inflater.inflate(R.layout.fragment_test_connection, container, false);
        initInstance(rootView);
        return rootView;
    }
    @Override
    public void onPause() {
        Log.d("DEBUG", "TestConnectionFragment onPause");
        super.onPause();
    }

    @Override
    public void onResume() {
        Log.d("DEBUG", "TestConnectionFragment onResume");
        super.onResume();
    }
    @Override
    public void onDestroy() {
//        Log.d("DEBUG", "TestConnectionFragment ondestroy");
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
//        Log.d("DEBUG", "TestConnectionFragment onDestroyView");
        super.onDestroy();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
//        Log.d("DEBUG", "TEST CONNECTION onCreate");

        super.onCreate(savedInstanceState);
    }


    private void initInstance(View v){
        Log.d("DEBUG", "initInstance");

        sharedPreferences = getActivity().getSharedPreferences("connection", Context.MODE_PRIVATE);
        //penta
        username = sharedPreferences.getString("username", "");
        password = sharedPreferences.getString("password", "");
        socketNumber = sharedPreferences.getString("socketNumber", "");
        //server
        dstAddr = sharedPreferences.getString("dstAddr", "192.168.5.53");
        dstPort = sharedPreferences.getInt("dstPort", 8000);
        sUsername = sharedPreferences.getString("sUsername", "root");
        sPassword = sharedPreferences.getString("sPassword", "dd");

        tvServerStatus = (TextView) v.findViewById(R.id.tvServerStatus);
        tvServerStatus.setText("Server : Disconnect");
        tvPcStatus = (TextView) v.findViewById(R.id.tvPcStatus);
        tvPcStatus.setText("Penta : Disconnect");

        etUsername = (EditText) v.findViewById(R.id.etUsername);
        etPassword = (EditText) v.findViewById(R.id.etPassword);
        etSocketNumber = (EditText) v.findViewById(R.id.etSocketNumber);
        btnConnect = (Button) v.findViewById(R.id.btnConnect);

        etUsername.setText(username);
        etPassword.setText(password);
        etSocketNumber.setText(socketNumber);

        btnConnect.setOnClickListener(connectListener);
    }

    View.OnClickListener connectListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //penta
            username = etUsername.getText().toString();
            password = etPassword.getText().toString();
            socketNumber = etSocketNumber.getText().toString();
            //server
            dstAddr = sharedPreferences.getString("dstAddr", "192.168.5.53");
            dstPort = sharedPreferences.getInt("dstPort", 8000);
            sUsername = sharedPreferences.getString("sUsername", "root");
            sPassword = sharedPreferences.getString("sPassword", "dd");

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("username", username);
            editor.putString("password", password);
            editor.putString("socketNumber", socketNumber);
            editor.commit();

            Log.d("SERER_VAR", sUsername + " " + sPassword + " " + dstAddr + " " + dstPort);
            TestConnectTask testConnectTask = new TestConnectTask(getActivity());
            testConnectTask.execute();

        }
    };

    public class TestConnectTask extends AsyncTask<Void, String, String> {

        private ProgressDialog progressDialog;
        private Context context;

        TestConnectTask(Context context){
            this.context = context;
            progressDialog = new ProgressDialog(this.context);
        }

        @Override
        protected void onPreExecute() {
//            super.onPreExecute();
            progressDialog.setMessage("Start Connecting");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {

            Socket socket = null;
            JSONObject json = null;
            String result = null;
            try {
                publishProgress("connecting to server");
                socket = new Socket(dstAddr, dstPort);
                if(socket.isConnected()) {
                    publishProgress("connected to server");
                }
                PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
                JSONObject js = new JSONObject();

                Log.d("sPassword", sPassword);

                js.put("username", username);
                js.put("password", password);
                js.put("sUsername", sUsername);
                js.put("sPassword", sPassword);
                js.put("fileName", "connect.sh");
                js.put("socketNumber", socketNumber);
                js.put("timeout", "3");
                pw.write(js.toString());
                pw.flush();

                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String line = null;

                ArrayList<String> fileList = new ArrayList<>();

                while((line = br.readLine()) != null){
                    if(line.equals("Invalid email or password") || line.equals("Unknown error"))
                        return line;
                    line = line.replace("_", " ").replace(".sh", "");
                    fileList.add(line);
                    Log.d("Line: ", line);
                    result = line;
                }

                if(!fileList.isEmpty()) {
                    fileList.remove(0); //remove run fileName.sh
                    fileList.remove(fileList.size() - 1); // remove server process result
                }

                Set<String> fileSet = new TreeSet<>(fileList);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putStringSet("fileSet", fileSet);
                editor.commit();
                Log.d("Result: ", result);
                Log.d("DEBUG", "AFTER SAVE fileSet");


            } catch (IOException e) {
                e.printStackTrace();
                publishProgress("can not connect to server");
                return "can not connect to server";
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            if(values[0].equals("connected to server"))
                tvServerStatus.setText("Server : Connected");
            else if(values[0].equals("can not connect to server"))
                tvServerStatus.setText("Server : Error");
            progressDialog.setMessage(values[0]);
            progressDialog.show();

        }


        @Override
        protected void onPostExecute(String s) {

            boolean isConnect = true;

            if(progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            if(s.equals("Connected, Recieved script's list")){

                tvPcStatus.setText("Penta : Connected");
                Toast.makeText(getActivity().getApplicationContext(), "connected, save change", Toast.LENGTH_SHORT).show();
                ((PagerFragment)getParentFragment()).setCurrentItem(1, true);

            }
            else{
                isConnect = false;
                tvPcStatus.setText("Penta : " + s);
                Toast.makeText(getActivity().getApplicationContext(), s, Toast.LENGTH_SHORT).show();
            }
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isConnect", isConnect);
            Log.d("IS_CONNECT", "" + isConnect);
            editor.commit();
        }
    }
}
