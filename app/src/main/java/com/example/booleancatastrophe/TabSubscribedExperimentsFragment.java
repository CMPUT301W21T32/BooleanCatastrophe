package com.example.booleancatastrophe;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class TabSubscribedExperimentsFragment extends Fragment {

    View view;

    public TabSubscribedExperimentsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tab_subscribed_experiments, container,
                false);
        return view;
    }
}