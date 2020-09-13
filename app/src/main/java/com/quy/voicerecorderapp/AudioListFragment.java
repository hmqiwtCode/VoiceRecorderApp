package com.quy.voicerecorderapp;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.File;


public class AudioListFragment extends Fragment implements AudioListAdapter.OnItemListClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private BottomSheetBehavior bottomSheetBehavior;
    private RecyclerView cv_list_audio;
    private ConstraintLayout player_sheet;
    private AudioListAdapter audioListAdapter;
    private MediaPlayer mediaPlayer;

    private boolean isPlay = false;
    private boolean isCollapsed = false;

    File[] allFiles;
    File fileAudioPlay;

    private ImageButton btn_play_audio;
    private TextView tv_fileName;
    private TextView tv_status;

    private SeekBar player_seekbar;
    private Runnable updateSeekBar;
    private Handler seekBarHandler;


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
        Log.e("LOG", "CREATE");
        player_sheet = view.findViewById(R.id.player_sheet);
        cv_list_audio = view.findViewById(R.id.cv_list_audio);

        btn_play_audio = view.findViewById(R.id.btn_player_play);
        tv_fileName = view.findViewById(R.id.tv_file_name);
        tv_status = view.findViewById(R.id.player_header_title);
        player_seekbar = view.findViewById(R.id.player_seekbar);

        String recordPath = getActivity().getExternalFilesDir("/").getAbsolutePath();
        File file = new File(recordPath);
        allFiles = file.listFiles();
        audioListAdapter = new AudioListAdapter(allFiles, this);

        cv_list_audio.setHasFixedSize(true);
        cv_list_audio.setLayoutManager(new LinearLayoutManager(getContext()));
        cv_list_audio.setAdapter(audioListAdapter);


        bottomSheetBehavior = BottomSheetBehavior.from(player_sheet);

        player_sheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isCollapsed) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    isCollapsed = false;
                } else {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    isCollapsed = true;
                }
            }
        });

        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        btn_play_audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPlay){
                    pauseAudio();
                }else{
                    if (fileAudioPlay != null)
                        resumeAudio();
                }
            }
        });
    }

    private void pauseAudio(){
        mediaPlayer.pause();
        btn_play_audio.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_play_arrow_24));
        isPlay = false;
    }

    private void resumeAudio(){
        mediaPlayer.start();
        btn_play_audio.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_pause_24));
        isPlay = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.e("LOG", "DESTROY");
    }

    @Override
    public void onClickListener(File file, int position) {
        Log.e("ERROR", file.getName());
        fileAudioPlay = file;
        if (isPlay) {
            stopAudio();
            playAudio(fileAudioPlay);
        } else {
            playAudio(fileAudioPlay);
        }
    }

    private void stopAudio() {
        isPlay = false;
        tv_status.setText("Not Playing");
       // mediaPlayer.release();
        mediaPlayer.stop();
    }

    private void playAudio(File fileAudioPlay) {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(fileAudioPlay.getAbsolutePath());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                btn_play_audio.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_play_arrow_24));
                tv_status.setText("Not Playing");
            }
        });
        btn_play_audio.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_pause_24));
        tv_fileName.setText(fileAudioPlay.getName());
        tv_status.setText("Playing");


        isPlay = true;

        player_seekbar.setMax(mediaPlayer.getDuration());
        seekBarHandler = new Handler();
        updateSeekBar = new Runnable() {
            @Override
            public void run() {
                player_seekbar.setProgress(mediaPlayer.getCurrentPosition());
                seekBarHandler.postDelayed(this,500);
            }
        };
        seekBarHandler.postDelayed(updateSeekBar,0);

    }
}