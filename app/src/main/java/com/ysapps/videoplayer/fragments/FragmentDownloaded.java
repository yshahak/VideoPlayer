package com.ysapps.videoplayer.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ysapps.videoplayer.DividerItemDecoration;
import com.ysapps.videoplayer.Folder;
import com.ysapps.videoplayer.R;
import com.ysapps.videoplayer.Utils;
import com.ysapps.videoplayer.Video;
import com.ysapps.videoplayer.adapters.RecyclerAdapterDownloads;
import com.ysapps.videoplayer.adapters.RecyclerAdapterVideoList;

import java.util.ArrayList;

import static com.ysapps.videoplayer.activities.MainActivity.CODE_STORAGE_PERMISSION;


/**
 * Created by B.E.L on 01/09/2016.
 */

public class FragmentDownloaded extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    public static final int SHOW_FOLDERS_GRID = 0;
    public static final int SHOW_FOLDER_CONTENT = 1;
    private ArrayList<Folder> folders;
    private RecyclerView recyclerView;
    private ImageView recyclerLabel;

    public static int state = SHOW_FOLDERS_GRID;
    public static Video removedVideo;
    private static int lastIndex = -1;

    public static FragmentDownloaded newInstance(int page){
        return new FragmentDownloaded();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_downlaods, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        recyclerLabel = (ImageView)view.findViewById(R.id.recycler_label);
        boolean permission = ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        if (permission) {
            folders = Utils.getRootFolders(getContext());
            setRecyclerView(state, lastIndex);
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CODE_STORAGE_PERMISSION);
        }
        return view;
    }

    public void setRecyclerView(int which, int position){
        state = which;
        lastIndex = position;
        if (recyclerView != null) {
            if (which ==  SHOW_FOLDERS_GRID){
                recyclerLabel.setImageResource(R.drawable.files_icon);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
                recyclerView.setAdapter(new RecyclerAdapterDownloads(folders, FragmentDownloaded.this));
            } else if(which == SHOW_FOLDER_CONTENT){
                recyclerLabel.setImageResource(R.drawable.downlaod_bar);
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
                if (removedVideo != null) {
                    folders.get(position).getVideos().remove(removedVideo);
                }
                FragmentDownloaded.removedVideo = null;
                recyclerView.setAdapter(new RecyclerAdapterVideoList(folders.get(position).getVideos()));
            }
        }
    }


}
