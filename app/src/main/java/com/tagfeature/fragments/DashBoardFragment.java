package com.tagfeature.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.tagfeature.R;
import com.tagfeature.activities.DashBoardActivity;
import com.tagfeature.fragments.dashboard.ViewPagerAdapterForDashBoard;
import com.tagfeature.utilities.CustomViewPager;

public class DashBoardFragment extends Fragment {
    public static final int OFFSCREEN_PAGE_LIMIT = 4;
    //    @BindView(R.id.bottom_navigation_view)
    BottomNavigationView bottomNavigationView;
    //    @BindView(R.id.dashboard_pager)
    CustomViewPager customViewPager;
    private View rootView;
    private DashBoardActivity dashBoardActivityContext;
    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.tab_home:
                    customViewPager.setCurrentItem(0, true);
                    break;
                case R.id.tab_tag_photo:
                    customViewPager.setCurrentItem(1, true);
                    break;
               /* case R.id.tab_search:
                    customViewPager.setCurrentItem(2, true);
                    break;
                case R.id.tab_my_profile:
                    customViewPager.setCurrentItem(3, true);
                    break;*/
            }
            return true;
        }
    };
    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            bottomNavigationView.getMenu().getItem(position).setChecked(true);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }

    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_dash_board, container, false);
        bottomNavigationView = rootView.findViewById(R.id.bottom_navigation_view);
        customViewPager = rootView.findViewById(R.id.dashboard_pager);
        initView();
        return rootView;
    }

    private void initView() {
        customViewPager.setAdapter(new ViewPagerAdapterForDashBoard(dashBoardActivityContext
                .getSupportFragmentManager(), this));
        customViewPager.setPagingEnabled(true);
        customViewPager.addOnPageChangeListener(onPageChangeListener);
        customViewPager.setOffscreenPageLimit(OFFSCREEN_PAGE_LIMIT);
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dashBoardActivityContext = (DashBoardActivity) context;
    }

    public void setHomeAsSelectedTab() {
        bottomNavigationView.setSelectedItemId(R.id.tab_home);
    }


}
