package com.example.deem.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.example.deem.R;

public class FirstPageFragment extends Fragment {

    private FrameLayout main_layout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        main_layout = (FrameLayout)inflater.inflate(R.layout.fragment_first_page, container, false);

        LinearLayout layout = main_layout.findViewById(R.id.test_begin);
        getLayoutInflater().inflate(R.layout.layout_info_person, layout);
        getLayoutInflater().inflate(R.layout.layout_info_person, layout);

        return main_layout;
    }
}