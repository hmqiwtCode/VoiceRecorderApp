package com.quy.voicerecorderapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class AudioListAdapter extends RecyclerView.Adapter<AudioListAdapter.AudioViewHolder> {
    private File[] allFiles;
    OnItemListClickListener onItemListClickListener;

    public AudioListAdapter(File[] allFiles,OnItemListClickListener onItemListClickListener) {
        this.allFiles = allFiles;
        this.onItemListClickListener = onItemListClickListener;
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

    class AudioViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView ic_play_video;
        TextView tv_file_name_rv, tv_hour_happen;

        public AudioViewHolder(@NonNull View itemView) {
            super(itemView);
            ic_play_video = itemView.findViewById(R.id.ic_play_video);
            tv_file_name_rv = itemView.findViewById(R.id.tv_file_name_rv);
            tv_hour_happen = itemView.findViewById(R.id.tv_hour_happen);

            itemView.setOnClickListener(this);
        }

        public void setInfoFile(File file){
            tv_file_name_rv.setText(file.getName());
            tv_hour_happen.setText(timeAgo(file.lastModified()));
        }

        public String timeAgo(long lastTime){
            Date date = new Date();
            long seconds = TimeUnit.MILLISECONDS.toMillis(date.getTime() -  lastTime);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(date.getTime() -  lastTime);
            long hours = TimeUnit.MILLISECONDS.toHours(date.getTime() -  lastTime);
            long days = TimeUnit.MILLISECONDS.toDays(date.getTime() -  lastTime);

            if (seconds < 60)
                return "just now";
            else if(minutes == 1 )
                return "a minute ago";
            else if (minutes > 1 && minutes < 60)
                return minutes + " minutes ago";
            else if (hours == 1)
                return "an hour ago";
            else if (hours > 1 && hours < 24)
                return hours + " hours ago";
            else if (days == 1)
                return "a day ago";
            else
                return days + " days ago";

        }

        @Override
        public void onClick(View view) {
            onItemListClickListener.onClickListener(allFiles[getAdapterPosition()],getAdapterPosition());
        }
    }

    public interface OnItemListClickListener{
        void onClickListener(File file,int position);
    }
}
