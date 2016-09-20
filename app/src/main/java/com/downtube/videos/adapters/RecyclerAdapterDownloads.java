package com.downtube.videos.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.downtube.videos.Folder;
import com.downtube.videos.R;
import com.downtube.videos.fragments.FragmentDownloaded;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import static com.downtube.videos.fragments.FragmentDownloaded.SHOW_FOLDER_CONTENT;

/**
 * Created by B.E.L on 04/09/2016.
 */

public class RecyclerAdapterDownloads extends RecyclerView.Adapter<RecyclerAdapterDownloads.ViewHolder> {

    private final String countPlaceHolder;
    private ArrayList<Folder> folderList;
    private WeakReference<FragmentDownloaded> weakReference;

    public RecyclerAdapterDownloads(ArrayList<Folder> videoList, FragmentDownloaded fragment) {
        this.folderList = videoList;
        this.weakReference = new WeakReference<>(fragment);
        this.countPlaceHolder = fragment.getContext().getString(R.string.count_place_holder);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {

        TextView folderLabel, folderCount;

        public ViewHolder(View itemView) {
            super(itemView);
            folderLabel = (TextView) itemView.findViewById(R.id.folder_label);
            folderCount = (TextView)itemView.findViewById(R.id.folder_count);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Folder folder = (Folder) itemView.getTag();
            if (folder != null) {
                FragmentDownloaded fragment = weakReference.get();
                if (fragment != null) {
                    fragment.setRecyclerView(SHOW_FOLDER_CONTENT, folderList.indexOf(folder));
                }
            }

        }


    }

    @Override
    public RecyclerAdapterDownloads.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.bucket_row, parent, false);
        return new RecyclerAdapterDownloads.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerAdapterDownloads.ViewHolder holder, int position) {
        Folder folder = folderList.get(position);
        holder.folderLabel.setText(folder.getFolderName());
        holder.folderCount.setText(String.format(countPlaceHolder, folder.getVideos().size()));
        holder.itemView.setTag(folder);
    }

    @Override
    public int getItemCount() {
        return folderList.size();
    }


}
