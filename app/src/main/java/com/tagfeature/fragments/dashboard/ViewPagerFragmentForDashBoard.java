package com.tagfeature.fragments.dashboard;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tagfeature.R;
import com.tagfeature.fragments.DashBoardFragment;


public class ViewPagerFragmentForDashBoard extends Fragment {

    private static final String TYPE = "TYPE";
    private FragmentManager fragmentManager;
    private DashBoardFragment dashBoardFragment;

    public static ViewPagerFragmentForDashBoard newInstance(String type,
                                                            DashBoardFragment dashBoardFragment) {
        ViewPagerFragmentForDashBoard viewPagerFragmentForDashBoard = new ViewPagerFragmentForDashBoard();
        Bundle extras = new Bundle();
        extras.putString(TYPE, type);
        viewPagerFragmentForDashBoard.setArguments(extras);
        viewPagerFragmentForDashBoard.dashBoardFragment = dashBoardFragment;
        return viewPagerFragmentForDashBoard;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_pager_for_dashboard,
                container, false);
        fragmentManager = getChildFragmentManager();
        Bundle extras = getArguments();
        String type = null;
        type = extras.getString(TYPE, "");
        Fragment fragment;
        switch (type) {
            case DashBoardFragments.HOME:
                fragment = new HomeFragment();
                switchFragment(fragment);
                break;
            case DashBoardFragments.TAG_PHOTO:
                fragment = new TagPhotoFragment();
                switchFragment(fragment);
                break;
           /* case DashBoardFragments.SEARCH:
                fragment = new SearchFragment();
                switchFragment(fragment);
                break;
            case DashBoardFragments.MY_PROFILE:
                fragment = new MyProfileFragment();
                switchFragment(fragment);
                break;*/
            default:
                fragment = new HomeFragment();
                switchFragment(fragment);
                break;
        }
        return view;
    }

    public void switchFragment(Fragment fragment) {
        fragmentManager.beginTransaction().replace(R.id.dashboard, fragment).commit();
    }

    public interface DashBoardFragments {
        String HOME = "HOME";
        String TAG_PHOTO = "MY_TEAM";
        /*String SEARCH = "SEARCH";
        String MY_PROFILE = "MY_PROFILE";*/
    }

    public void setHomeAsSelectedTab() {
        dashBoardFragment.setHomeAsSelectedTab();
    }

}