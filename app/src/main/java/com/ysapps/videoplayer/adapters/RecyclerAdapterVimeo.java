package com.ysapps.videoplayer.adapters;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.vimeo.networking.model.Video;
import com.vimeo.networking.model.VideoList;
import com.ysapps.videoplayer.R;
import com.ysapps.videoplayer.Utils;
import com.ysapps.videoplayer.activities.MainActivity;
import com.ysapps.videoplayer.activities.VimeoPlayerActivity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Map;

import uk.breedrapps.vimeoextractor.OnVimeoExtractionListener;
import uk.breedrapps.vimeoextractor.VimeoExtractor;
import uk.breedrapps.vimeoextractor.VimeoVideo;

import static com.ysapps.videoplayer.activities.MainActivity.CODE_STORAGE_PERMISSION;
import static com.ysapps.videoplayer.activities.VimeoPlayerActivity.EXTRA_LINK;

/**
 * Created by B.E.L on 01/09/2016.
 */

public class RecyclerAdapterVimeo extends RecyclerView.Adapter<RecyclerAdapterVimeo.ViewHolder> {

    private VideoList videoList;
    private WeakReference<Activity> activityWeakReference;

    public RecyclerAdapterVimeo(Activity activity, VideoList videoList) {
        this.videoList = videoList;
        activityWeakReference = new WeakReference<>(activity);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
            , View.OnTouchListener, AdapterView.OnItemSelectedListener {

        ImageView thumbnail;
        Spinner spinner;
        TextView textView;
        private boolean firstSpinnerClick = true;

        public ViewHolder(View itemView) {
            super(itemView);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            spinner = (Spinner) itemView.findViewById(R.id.spinner_down);
            textView = (TextView) itemView.findViewById(R.id.text_content);
            itemView.setOnClickListener(this);
            spinner.setOnTouchListener(this);
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
            }

        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN && firstSpinnerClick) {
                VimeoVideo video = (VimeoVideo) spinner.getTag();
                String[] array = video.getStreams().keySet().toArray(new String[video.getStreams().keySet().size()]);
                final ArrayAdapter<String> adapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_dropdown_item, array);
                spinner.setAdapter(adapter);
                spinner.performClick();
                spinner.setOnItemSelectedListener(this);
                return true;
            }
            return false;
        }

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            if (firstSpinnerClick){
                firstSpinnerClick = false;
                return;
            }
            VimeoVideo video = (VimeoVideo) spinner.getTag();
            if (video != null) {
                boolean permission = ContextCompat.checkSelfPermission(itemView.getContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
                if (permission) {
                    Map<String, String> strems = video.getStreams();
                    MainActivity.pathId = video.getTitle().replaceAll("[.]", "") + ".mp4";
                    MainActivity.downId =  Utils.downloadFile(view.getContext(), video.getStreams().get(new ArrayList(strems.keySet()).get(i)), MainActivity.pathId);
                } else {
                    Activity activity = activityWeakReference.get();
                    if (activity != null) {
                        Toast.makeText(activity, "You need to allow writing to memory", Toast.LENGTH_SHORT).show();

                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CODE_STORAGE_PERMISSION);
                    }
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_video_vimeo, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Context context = holder.itemView.getContext();
        Video video = videoList.data.get(position); // just an example of getting the first video
        String html = video.embed != null ? video.embed.html : null;
        holder.itemView.setTag(html);
        String url = video.pictures.pictureForWidth(150).link;
        Picasso.with(holder.itemView.getContext())
                .load(url)
                .into(holder.thumbnail);
        final String content = video.name;
        holder.textView.setText(content);
        String id = Uri.parse(video.uri).getLastPathSegment();
        String[] array = {" "};
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, array);
        holder.spinner.setAdapter(adapter);
        VimeoExtractor.getInstance().fetchVideoWithIdentifier(id, null, new OnVimeoExtractionListener() {
            @Override
            public void onSuccess(VimeoVideo video) {
                holder.spinner.setTag(video);
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
}
