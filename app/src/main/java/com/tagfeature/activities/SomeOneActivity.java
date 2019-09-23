
package com.tagfeature.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.tagfeature.R;
import com.tagfeature.adapters.SomeOneAdapter;
import com.tagfeature.interfaces.SomeOneClickListener;
import com.tagfeature.models.SomeOne;
import com.tagfeature.utilities.SomeOneData;

public class SomeOneActivity extends AppCompatActivity implements SomeOneClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_some_one);
        RecyclerView mRecyclerViewSomeOne = findViewById(R.id.rv_some_one);
        SomeOneAdapter someOneAdapter =
                new SomeOneAdapter(SomeOneData.getDummySomeOneList(),
                        this, this);
        mRecyclerViewSomeOne.setAdapter(someOneAdapter);
        mRecyclerViewSomeOne.setLayoutManager(new LinearLayoutManager(this));
    }


    @Override
    public void onSomeOneClicked(final SomeOne someOne, int position) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(SomeOneActivity.this,
                        someOne.getFullName(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
