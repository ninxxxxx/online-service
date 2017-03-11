package com.example.arnon.userservice.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.arnon.userservice.MainInterface;
import com.example.arnon.userservice.R;
import com.example.arnon.userservice.adapter.MyPagerAdapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by arnon on 4/7/2559.
 */
public class PagerFragment extends Fragment implements MainInterface{

    SharedPreferences sharedPreferences;

    ViewPager viewPager;
    MyPagerAdapter adapter;
    ArrayAdapter arrayAdapter;
    TabLayout tl;
    ListView lv;

    List<Fragment> fragmentsList;
    MainFragment mainFragment = MainFragment.newInstance();
    TestConnectionFragment testConnectionFragment = TestConnectionFragment.newInstance();

    public static PagerFragment newInstance(){
        PagerFragment pager = new PagerFragment();
        return pager;
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("DEBUG", "PageFragment onCreateView");
        View rootView = inflater.inflate(R.layout.fragment_pager_main, container, false);
        initInstance(rootView);
        return rootView;
//        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @Override
    public void onPause() {
        Log.d("DEBUG", "PageFragment onPause");
        super.onPause();
    }

    @Override
    public void onResume() {
        Log.d("DEBUG", "PageFragment onResume");
        super.onResume();
    }

    @Override
    public void onDestroy() {
        Log.d("DEBUG", "PageFragment ondestroy");
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        Log.d("DEBUG", "PageFragment onDestroyView");
        super.onDestroy();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void initInstance(View v){
        viewPager = (ViewPager) v.findViewById(R.id.vpPager);
        fragmentsList = new ArrayList<Fragment>();
        fragmentsList.add(testConnectionFragment);
        fragmentsList.add(mainFragment);

        lv = (ListView) v.findViewById(R.id.methodList);
        sharedPreferences = getActivity().getSharedPreferences("connection", Context.MODE_PRIVATE);




        adapter = new MyPagerAdapter(getChildFragmentManager(), fragmentsList);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(position == 1){
                    mainFragment.createList();
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tl = (TabLayout) v.findViewById(R.id.tabSlide);
        tl.setupWithViewPager(viewPager);
    }

    public void setCurrentItem (int item, boolean smoothScroll) {
        viewPager.setCurrentItem(item, smoothScroll);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onConnectSucceeded(String username, String password, String socketNumber) {
    }

}
