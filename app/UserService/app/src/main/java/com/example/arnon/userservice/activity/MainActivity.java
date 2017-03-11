package com.example.arnon.userservice.activity;
//

import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.arnon.userservice.MainInterface;
import com.example.arnon.userservice.R;
import com.example.arnon.userservice.adapter.MyPagerAdapter;
import com.example.arnon.userservice.fragment.MainFragment;
import com.example.arnon.userservice.fragment.PagerFragment;
import com.example.arnon.userservice.fragment.SettingsFragment;
import com.example.arnon.userservice.fragment.TestConnectionFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    PagerFragment pager = PagerFragment.newInstance();
    SettingsFragment settings = SettingsFragment.newInstance();
    DrawerLayout drawer;
    NavigationView navigationView;

    String socketNumber;
    String timeout;
    String dstAddr;

    SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initInstance();
        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, PagerFragment.newInstance())
                    .commit();
        }

    }

    @Override
    protected void onStop() {
        Log.d("STATE", "MAIN_ACTIVITY onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d("STATE", "MAIN_ACTIVITY onDestroy");
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isConnect", false);
//        editor.putBoolean("isProcess", false);
        editor.commit();
        super.onDestroy();
    }

    public void initInstance(){

        sharedPreferences = getSharedPreferences("connection", this.MODE_PRIVATE);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.main_menu){
            callSettings();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.nav_settings) {
            callSettings();
        }
        else if(item.getItemId() == R.id.nav_about) {
            Toast.makeText(this, "About", Toast.LENGTH_SHORT).show();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    public void setVar(String dstAddr, String socketNumber){
        Log.d("DEBUG", "MAIN_ACTIVITY SET SOCKET_NUMBER: " + this.socketNumber + "DST_ADDR: " + this.dstAddr);
        this.socketNumber = socketNumber;
        this.dstAddr = dstAddr;
    }
    public String getSocketNumber(){
        return this.socketNumber;
    }

    public void callSettings(){
        Bundle bb = new Bundle();

        if(getSupportFragmentManager().findFragmentById(R.id.container) instanceof SettingsFragment )
            Toast.makeText(this, "you are in Settings", Toast.LENGTH_SHORT).show();
        else {
//            bb.putString("socketNumber", socketNumber);
//            bb.putString("dstAddr", dstAddr);
//            settings.setArguments(bb);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, settings)
                    .addToBackStack(null)
                    .commit();
        }
    }

}
