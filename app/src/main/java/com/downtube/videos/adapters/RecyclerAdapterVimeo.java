package com.downtube.videos.adapters;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vimeo.networking.model.Video;
import com.vimeo.networking.model.VideoList;
import com.downtube.videos.R;
import com.downtube.videos.activities.DownloadDialogActivity;
import com.downtube.videos.activities.VimeoPlayerActivity;

import java.util.Locale;

import uk.breedrapps.vimeoextractor.OnVimeoExtractionListener;
import uk.breedrapps.vimeoextractor.VimeoExtractor;
import uk.breedrapps.vimeoextractor.VimeoVideo;

import static com.downtube.videos.activities.VimeoPlayerActivity.EXTRA_LINK;

/**
 * Created by B.E.L on 01/09/2016.
 */

public class RecyclerAdapterVimeo extends RecyclerView.Adapter<RecyclerAdapterVimeo.ViewHolder> {

    private final TypedArray ids;
    private VideoList videoList;
    private String contentPlaceHolder;

    public RecyclerAdapterVimeo(Activity activity, VideoList videoList) {
        this.videoList = videoList;
        ids = activity.getResources().obtainTypedArray(R.array.menu_ids);
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
            if (view.equals(itemView)) {
                String html = (String) itemView.getTag();
                if (html != null) {
                    Intent intent = new Intent(itemView.getContext(), VimeoPlayerActivity.class);
                    intent.putExtra(EXTRA_LINK, html);
                    itemView.getContext().startActivity(intent);
                }
            } else if (view.equals(downBtn)){
                VimeoVideo video = (VimeoVideo) downBtn.getTag();
                String[] arrayKeys = video.getStreams().keySet().toArray(new String[video.getStreams().keySet().size()]);
                String[] arrayValues = video.getStreams().values().toArray(new String[video.getStreams().keySet().size()]);

                Intent intent = new Intent(view.getContext(), DownloadDialogActivity.class);
                intent.putExtra(DownloadDialogActivity.EXTRA_VIDEOS_STREAMS_KEYS, arrayKeys);
                intent.putExtra(DownloadDialogActivity.EXTRA_VIDEOS_STREAMS_LINKS, arrayValues);

                intent.putExtra(DownloadDialogActivity.EXTRA_VIDEO_TITLE, video.getTitle().replaceAll("[.]", ""));
                view.getContext().startActivity(intent);
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
        Video video = videoList.data.get(position); // just an example of getting the first video
        String html = video.embed != null ? video.embed.html : null;
        holder.itemView.setTag(html);
        String url = video.pictures.pictureForWidth(150).link;
        Picasso.with(holder.itemView.getContext())
                .load(url)
                .fit()
                .into(holder.thumbnail);
        holder.textViewLabel.setText(video.name);
        holder.textViewContent.setText(String.format(contentPlaceHolder, video.playCount(), video.likeCount()));

//        holder.textViewContent.setText(content);
        String length = String.format(Locale.getDefault(), "%02d:%02d",
                ((video.duration ) % 3600) / 60, video.duration  % 60);
        holder.videoLength.setText(length);
        String id = Uri.parse(video.uri).getLastPathSegment();
        VimeoExtractor.getInstance().fetchVideoWithIdentifier(id, null, new OnVimeoExtractionListener() {
            @Override
            public void onSuccess(VimeoVideo video) {
                holder.downBtn.setTag(video);
            }

            @Override
            public void onFailure(Throwable throwable) {
                //Error handling here
            }
        });
    }

    @Override
    public int getItemCount() {
        if (videoList == null){
            return 0;
        }
        return videoList.data.size();
    }




    public void setVideoList(VideoList videoList) {
        this.videoList = videoList;
        notifyDataSetChanged();
    }

    private void showPopup(PopupMenu popupMenu, VimeoVideo video) {
        String[] array = video.getStreams().keySet().toArray(new String[video.getStreams().keySet().size()]);
        int id;
        for (int i = 0 ; i < array.length ; i++){
            id = ids.getResourceId(i, -1);
            popupMenu.getMenu().add(R.id.menuGroup, id, i, array[i]);
        }
        popupMenu.show();
    }
}
