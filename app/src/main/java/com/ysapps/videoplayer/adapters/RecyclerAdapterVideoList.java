package com.ysapps.videoplayer.adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.ysapps.videoplayer.R;
import com.ysapps.videoplayer.Video;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by B.E.L on 04/09/2016.
 */

public class RecyclerAdapterVideoList extends RecyclerView.Adapter<RecyclerAdapterVideoList.ViewHolder> {

    private ArrayList<Video> videos;

    public RecyclerAdapterVideoList(ArrayList<Video> videos) {
        this.videos = videos;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {

        private TextView videoLabel, videoDuration;
        private ImageView thumbnail;

        public ViewHolder(View itemView) {
            super(itemView);
            videoLabel = (TextView)itemView.findViewById(R.id.video_label);
            videoDuration = (TextView)itemView.findViewById(R.id.video_duration);
            thumbnail = (ImageView)itemView.findViewById(R.id.thumbnail);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Video video = (Video) itemView.getTag();
            if (video != null) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(video.getVideoUri(), "video/mp4");
                itemView.getContext().startActivity(intent);
            }

        }


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_video, parent, false);
        return new RecyclerAdapterVideoList.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Video video = videos.get(position);
        Picasso
                .with(holder.itemView.getContext())
                .load(video.getVideoUri())
                .fit()
                .centerInside()
                .into(holder.thumbnail, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        videos.remove(video);
                        notifyDataSetChanged();
                    }
                });
        holder.videoLabel.setText(video.getVideoName());
        holder.videoDuration.setText(String.format(Locale.getDefault(), "%02d:%02d", ((video.getVideoLength() / 1000) % 3600) / 60 ,video.getVideoLength() /1000 %  60));
        holder.itemView.setTag(video);

    }

    @Override
    public int getItemCount() {
        return videos.size();
    }




}
