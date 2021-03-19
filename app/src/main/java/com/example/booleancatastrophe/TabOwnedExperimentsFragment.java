package com.example.booleancatastrophe;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class TabOwnedExperimentsFragment extends Fragment {

    View view;

    public TabOwnedExperimentsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tab_owned_experiments, container,
                false);
        return view;
    }
}