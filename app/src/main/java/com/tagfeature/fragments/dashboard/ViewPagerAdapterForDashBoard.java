package com.tagfeature.fragments.dashboard;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


import com.tagfeature.fragments.DashBoardFragment;

import java.util.ArrayList;

public class ViewPagerAdapterForDashBoard extends FragmentStatePagerAdapter {
    private ArrayList<String> dashBoardFragments = new ArrayList<>();
    private DashBoardFragment dashBoardFragment;

    public ViewPagerAdapterForDashBoard(FragmentManager fm, DashBoardFragment dashBoardFragment) {
        super(fm);
        dashBoardFragments.add(ViewPagerFragmentForDashBoard.DashBoardFragments.HOME);
        dashBoardFragments.add(ViewPagerFragmentForDashBoard.DashBoardFragments.TAG_PHOTO);
        /*dashBoardFragments.add(ViewPagerFragmentForDashBoard.DashBoardFragments.SEARCH);
        dashBoardFragments.add(ViewPagerFragmentForDashBoard.DashBoardFragments.MY_PROFILE);*/
        this.dashBoardFragment = dashBoardFragment;
    }

    @Override
    public Fragment getItem(int position) {
        return ViewPagerFragmentForDashBoard
                .newInstance(dashBoardFragments.get(position), dashBoardFragment);
    }

    @Override
    public int getCount() {
        return dashBoardFragments.size();
    }

    public ArrayList<String> getDashBoardFragments() {
        return dashBoardFragments;
    }

    public void setDashBoardFragments(ArrayList<String> dashBoardFragments) {
        this.dashBoardFragments = dashBoardFragments;
    }
}
