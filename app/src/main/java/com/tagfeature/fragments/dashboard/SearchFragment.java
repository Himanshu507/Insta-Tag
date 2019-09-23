package com.tagfeature.fragments.dashboard;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.tagfeature.R;
import com.tagfeature.adapters.SomeOneAdapter;
import com.tagfeature.interfaces.SomeOneClickListener;
import com.tagfeature.models.SomeOne;
import com.tagfeature.utilities.SomeOneData;

import java.util.ArrayList;

public class SearchFragment extends Fragment implements SomeOneClickListener {

    private RecyclerView mRecyclerViewSomeOne;
    private EditText mEditSearchForSomeOne;
    private SomeOneAdapter mSomeOneAdapter;
    private final ArrayList<SomeOne> mSomeOnes = new ArrayList<>();

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        mRecyclerViewSomeOne = rootView.findViewById(R.id.rv_some_one);
        mEditSearchForSomeOne = rootView.findViewById(R.id.search_for_a_person);
        mEditSearchForSomeOne.addTextChangedListener(textWatcher);

        mSomeOnes.addAll(SomeOneData.getDummySomeOneList());
        mSomeOneAdapter = new SomeOneAdapter(mSomeOnes, getActivity(), this);
        mRecyclerViewSomeOne.setAdapter(mSomeOneAdapter);
        mRecyclerViewSomeOne.setLayoutManager(new LinearLayoutManager(getActivity()));

        return rootView;
    }

    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (mEditSearchForSomeOne.getText().toString().trim().equals("")) {
                mSomeOnes.clear();
                mSomeOnes.addAll(SomeOneData.getDummySomeOneList());
                mSomeOneAdapter.notifyDataSetChanged();
            } else {
                mSomeOnes.clear();
                mSomeOnes.addAll(SomeOneData.
                        getFilteredUser(mEditSearchForSomeOne.getText().toString().trim()));
                mSomeOneAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    @Override
    public void onSomeOneClicked(final SomeOne someOne, int position) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(),
                        someOne.getFullName(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
