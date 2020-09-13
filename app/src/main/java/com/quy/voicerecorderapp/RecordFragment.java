package com.quy.voicerecorderapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class RecordFragment extends Fragment implements View.OnClickListener {

    private NavController navController;
    private ImageButton btn_record_list,btn_record;
    private TextView tv_record_file_name;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private boolean isRecording = false;
    private final int PERMISSION_RECORD_AUDIO_CODE = 1;
    private MediaRecorder mediaRecorder;

    private String fileRecord;
    private Chronometer ct_record_timer;

    public RecordFragment() {

    }

    /*public static RecordFragment newInstance(String param1, String param2) {
        RecordFragment fragment = new RecordFragment();
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
        Log.d("onCreate","onCreate");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("onCreateView","onCreateView");
        return inflater.inflate(R.layout.fragment_record, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        btn_record_list = view.findViewById(R.id.btn_record_list);
        btn_record = view.findViewById(R.id.btn_record);
        ct_record_timer = view.findViewById(R.id.ct_record_timer);
        tv_record_file_name = view.findViewById(R.id.tv_record_file_name);
        Log.d("onViewCreated","onViewCreated");
        btn_record_list.setOnClickListener(this);
        btn_record.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_record_list:
                navController.navigate(R.id.action_recordFragment_to_audioListFragment2);
                break;
            case  R.id.btn_record:
                if (isRecording){
                    stopRecording();
                    btn_record.setImageDrawable(getResources().getDrawable(R.drawable.ic_stop));
                    isRecording = false;
                }else{
                    if (checkPermission()){
                        try {
                            startRecording();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        btn_record.setImageDrawable(getResources().getDrawable(R.drawable.ic_start));
                        isRecording = true;
                    }

                }
        }
    }

    private void startRecording() throws IOException {
        ct_record_timer.setBase(SystemClock.elapsedRealtime());
        ct_record_timer.start();
        String recordPath = getActivity().getExternalFilesDir("/").getAbsolutePath();
        Log.d("PATH",recordPath);
        SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss", Locale.CANADA);
        Date date = new Date();
        fileRecord = "Recording_"+ format.format(date);

        tv_record_file_name.setText("Recording, File Name: "+fileRecord);

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile(recordPath + "/" + fileRecord);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.prepare();
        mediaRecorder.start();
    }

    private void stopRecording() {
        ct_record_timer.stop();
        mediaRecorder.stop();
        tv_record_file_name.setText("Recording Stopped, File Saved: "+fileRecord);
        mediaRecorder.release();
        mediaRecorder = null;
    }

    private boolean checkPermission() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)
            return true;
        else{
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.RECORD_AUDIO},PERMISSION_RECORD_AUDIO_CODE);
            return false;
        }
    }
}