package com.quy.voicerecorderapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;

public class AudioListAdapter extends RecyclerView.Adapter<AudioListAdapter.AudioViewHolder> {
    private File[] allFiles;

    public AudioListAdapter(File[] allFiles) {
        this.allFiles = allFiles;
    }

    @NonNull
    @Override
    public AudioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_list_item, parent, false);
        return new AudioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AudioViewHolder holder, int position) {
        File file = allFiles[position];
        holder.setInfoFile(file);
    }

    @Override
    public int getItemCount() {
        return allFiles.length;
    }

    class AudioViewHolder extends RecyclerView.ViewHolder {
        ImageView ic_play_video;
        TextView tv_file_name_rv, tv_hour_happen;

        public AudioViewHolder(@NonNull View itemView) {
            super(itemView);
            ic_play_video = itemView.findViewById(R.id.ic_play_video);
            tv_file_name_rv = itemView.findViewById(R.id.tv_file_name_rv);
            tv_hour_happen = itemView.findViewById(R.id.tv_hour_happen);
        }

        public void setInfoFile(File file){
            tv_file_name_rv.setText(file.getName());
            tv_hour_happen.setText(file.lastModified()+"");
        }
    }
}
