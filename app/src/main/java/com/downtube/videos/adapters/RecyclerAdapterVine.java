package com.downtube.videos.adapters;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.downtube.videos.R;
import com.downtube.videos.Utils;
import com.downtube.videos.activities.MainActivity;
import com.downtube.videos.vine.VineVideo;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by B.E.L on 01/09/2016.
 */

public class RecyclerAdapterVine extends RecyclerView.Adapter<RecyclerAdapterVine.ViewHolder> {

    private List<VineVideo> videoList;
    private String contentPlaceHolder;

    public RecyclerAdapterVine(Activity activity, List<VineVideo> videoList) {
        this.videoList = videoList;
        contentPlaceHolder = activity.getString(R.string.vimeo_video_placeholder);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView thumbnail;
        ImageView downBtn;
        TextView textViewContent, textViewLabel, videoLength;

        ViewHolder(View itemView) {
            super(itemView);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            downBtn = (ImageView) itemView.findViewById(R.id.spinner_down);
            textViewContent = (TextView) itemView.findViewById(R.id.text_content);
            textViewLabel = (TextView)itemView.findViewById(R.id.text_label);
            videoLength = (TextView)itemView.findViewById(R.id.video_duration);
            itemView.setOnClickListener(this);
            downBtn.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            VineVideo vineVideo = (VineVideo)itemView.getTag();
            if (vineVideo == null) {
                return;
            }
            if (view.equals(itemView)) {
                String html = vineVideo.getVineStreamUrl();
                if (html != null) {
                    if (!html.startsWith("http://") && !html.startsWith("https://"))
                        html = "http://" + html;
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(html));
                    itemView.getContext().startActivity(intent);
                }
            } else if (view.equals(downBtn)){
                MainActivity.pathId = vineVideo.getId()  + ".mp4";
                Log.d("TAG", "downloading: " + MainActivity.pathId );
                MainActivity.downId = Utils.downloadFile(itemView.getContext(), vineVideo.getVineStreamUrl(), MainActivity.pathId);
            }
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_video_vimeo, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        VineVideo video = videoList.get(position); // just an example of getting the first video
        holder.itemView.setTag(video);
        Picasso.with(holder.itemView.getContext())
                .load(video.getVineThumbUrl())
                .fit()
                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        if (videoList == null){
            return 0;
        }
        return videoList.size();
    }




    public void setVideoList(List<VineVideo> videoList) {
        this.videoList = videoList;
        notifyDataSetChanged();
    }

}
