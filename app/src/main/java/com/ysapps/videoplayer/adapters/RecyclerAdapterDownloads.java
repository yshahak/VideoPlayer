package com.ysapps.videoplayer.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ysapps.videoplayer.Folder;
import com.ysapps.videoplayer.R;
import com.ysapps.videoplayer.fragments.DownloadedFragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import static com.ysapps.videoplayer.fragments.DownloadedFragment.SHOW_FOLDER_CONTENT;

/**
 * Created by B.E.L on 04/09/2016.
 */

public class RecyclerAdapterDownloads extends RecyclerView.Adapter<RecyclerAdapterDownloads.ViewHolder> {

    private ArrayList<Folder> folderList;
    private WeakReference<DownloadedFragment> weakReference;

    public RecyclerAdapterDownloads(ArrayList<Folder> videoList, DownloadedFragment fragment) {
        this.folderList = videoList;
        this.weakReference = new WeakReference<>(fragment);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {

        TextView folderLabel;

        public ViewHolder(View itemView) {
            super(itemView);
            folderLabel = (TextView) itemView.findViewById(R.id.folder_label);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Folder folder = (Folder) itemView.getTag();
            if (folder != null) {
                DownloadedFragment fragment = weakReference.get();
                if (fragment != null) {
                    fragment.setRecyclerView(SHOW_FOLDER_CONTENT, folderList.indexOf(folder));
                }
            }

        }


    }

    @Override
    public RecyclerAdapterDownloads.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.bucket_cell, parent, false);
        return new RecyclerAdapterDownloads.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerAdapterDownloads.ViewHolder holder, int position) {
        Folder folder = folderList.get(position);
        holder.folderLabel.setText(folder.getFolderName());
        holder.itemView.setTag(folder);
    }

    @Override
    public int getItemCount() {
        return folderList.size();
    }


}
