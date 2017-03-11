package com.example.arnon.userservice.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arnon.userservice.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by arnon on 7/7/2559.
 */
public class MethodConsoleFragment extends Fragment implements MethodTask.TaskCallbacks{

//    private TaskFragment mTaskFragment;
    String dstAddr;
    int dstPort;
    String username;
    String password;
    String sUsername;
    String sPassword;
    String socketNumber;
    String currentText;
    String fileName;

    TextView tvConsole;
    ScrollView sv;
    Button btnSave;
    SharedPreferences sharedPreferences;

//    ProgressDialog progressDialog;
    static MethodTask methodTask;

    public MethodConsoleFragment(){

    }


    public static MethodConsoleFragment newInstance(MethodTask m){
        MethodConsoleFragment fragment = new MethodConsoleFragment();
        methodTask = m;
        methodTask.setCallback(fragment);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_method_console, container, false);
        initInstance(rootView);
        return rootView;
    }
    public void initInstance(View v){



        sharedPreferences = getActivity().getSharedPreferences("connection", Context.MODE_PRIVATE);

        currentText = "";

        sv = (ScrollView) v.findViewById(R.id.sv);

//        progressDialog = new ProgressDialog(getContext());

//        progressDialog = new ProgressDialog(getActivity());
        tvConsole = (TextView) v.findViewById(R.id.tvConsole);
        tvConsole.setText(currentText);

        btnSave = (Button) v.findViewById(R.id.btnSave);
        btnSave.setEnabled(false);
        btnSave.setOnClickListener(saveListener);

    }





    View.OnClickListener saveListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            String currentDateandTime = sdf.format(new Date()).replace(" ", "_").replace(":", "-").replace(".", "-");

            File path = getActivity().getExternalFilesDir(null);
            String file_name = "" + fileName + "_" + currentDateandTime + ".txt";


            File file = new File(path, file_name);

            FileOutputStream stream = null;
            try {
                stream = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                stream.write(currentText.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Toast.makeText(getActivity(), "File was created", Toast.LENGTH_SHORT).show();
        }
    };






    @Override
    public void onPreExecute() {
//        progressDialog.setMessage("Start Connecting");
//        progressDialog.show(getContext(), "","Start Connecting", true);
    }



    @Override
    public void onProgressUpdate(String... values) {

        tvConsole.setText(values[0]);
        sv.post(new Runnable() {
            @Override
            public void run() {
                sv.fullScroll(TextView.FOCUS_DOWN);
            }
        });
    }


    @Override
    public void onPostExecute(Void aVoid) {
        btnSave.setEnabled(true);
        Toast.makeText(getActivity(), "completed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCancelled() {

    }



//    this.context = context;
//        TestConnectTask(Context context){
//
//        private Context context;
//        private ProgressDialog progressDialog;

//    public class TestConnectTask extends AsyncTask<Void, String, Void> {
//            progressDialog = new ProgressDialog(this.context);
//        }
//
//        @Override
//        protected void onPreExecute() {
////            super.onPreExecute();
////            progressDialog.setMessage("Start Connecting");
////            progressDialog.show();
//        }
//
//        @Override
//        protected Void doInBackground(Void... params) {
//            Socket socket = null;
//            String result = null;
//            try {
//                publishProgress("connecting to server......");
//                socket = new Socket(dstAddr, dstPort);
//                if(socket.isConnected()) {
//                    publishProgress("connected to server......");
//                }
//                PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
//                JSONObject js = new JSONObject();
//                js.put("username", username);
//                js.put("password", password);
//                js.put("sUsername", sUsername);
//                js.put("sPassword", sPassword);
//                js.put("fileName", fileName);
//                js.put("socketNumber", socketNumber);
//                js.put("timeout", "3");
//                pw.write(js.toString());
//                pw.flush();
//
//                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//                String line = null;
//
//
//                while((line = br.readLine()) != null){
//                    Log.d("Line: ", line);
//                    publishProgress(line);
//                }
//
//            } catch (IOException e) {
//                publishProgress("..............can't connect to server");
//                publishProgress("..............Failed to connect");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onProgressUpdate(String... values) {
//            currentText += "\n" + values[0];
//            tvConsole.setText(currentText);
//            sv.post(new Runnable() {
//                @Override
//                public void run() {
//                    sv.fullScroll(TextView.FOCUS_DOWN);
//                }
//            });
//
//        }
//
//        @Override
//        protected void onPostExecute(Void v) {
//
//            btnSave.setEnabled(true);
//
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.putBoolean("isProcess", false);
//            editor.putString("fileName", "");
//            editor.commit();
//            Toast.makeText(getActivity(), "completed", Toast.LENGTH_SHORT).show();
//        }
//    }


}
