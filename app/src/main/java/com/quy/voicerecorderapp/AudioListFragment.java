package com.quy.voicerecorderapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.File;


public class AudioListFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private BottomSheetBehavior bottomSheetBehavior;
    private RecyclerView cv_list_audio;
    private ConstraintLayout player_sheet;

    private boolean isCollapsed = false;
    File[] allFiles;


    public AudioListFragment() {

    }


    /*public static AudioListFragment newInstance(String param1, String param2) {
        AudioListFragment fragment = new AudioListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_audio_list, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        player_sheet = view.findViewById(R.id.player_sheet);
        cv_list_audio = view.findViewById(R.id.cv_list_audio);

        String recordPath = getActivity().getExternalFilesDir("/").getAbsolutePath();
        File file = new File(recordPath);
        allFiles = file.listFiles();

        bottomSheetBehavior = BottomSheetBehavior.from(player_sheet);

        player_sheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if (isCollapsed){
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                isCollapsed = false;
            }else{
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                isCollapsed = true;
            }
        }
    });

        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN){
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }
}