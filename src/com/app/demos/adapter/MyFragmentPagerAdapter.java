package com.app.demos.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.app.demos.R;
import com.app.demos.base.BaseApp;
import com.app.demos.base.BaseAuth;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> fragmentsList;
    private Context context = BaseApp.getContext();
    private final String[] TITLES = {context.getString(R.string.tab_1), context.getString(R.string.tab_2),
            context.getString(R.string.tab_3), context.getString(R.string.tab_4)};

    public MyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public MyFragmentPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
        super(fm);
        this.fragmentsList = fragments;
    }

    @Override
    public int getCount() {
        //return fragmentsList.size();
        return TITLES.length;
    }

    @Override
    public Fragment getItem(int arg0) {
        return fragmentsList.get(arg0);
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }

}
