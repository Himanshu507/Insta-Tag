package com.tagfeature.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.tagfeature.R;
import com.tagfeature.fragments.DashBoardFragment;
import com.tagfeature.fragments.dashboard.ViewPagerFragmentForDashBoard;


public class DashBoardActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragment = new DashBoardFragment();
        fragmentTransaction.add(R.id.dash_board_content,
                fragment,
                ViewPagerFragmentForDashBoard.DashBoardFragments.HOME);
        fragmentTransaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
