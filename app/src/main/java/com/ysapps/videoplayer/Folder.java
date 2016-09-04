package com.ysapps.videoplayer;

import java.util.ArrayList;

/**
 * Created by B.E.L on 04/09/2016.
 */

public class Folder {
    private String folderName;
    private int folderId;
    private ArrayList<Video> videos = new ArrayList<>();

    public Folder(String folderName, int folderId) {
        this.folderName = folderName;
        this.folderId = folderId;
    }

    public Folder(String folderName, int folderId, ArrayList<Video> videos) {
        this.folderName = folderName;
        this.folderId = folderId;
        this.videos = videos;
    }

    public String getFolderName() {
        return folderName;
    }

    public ArrayList<Video> getVideos() {
        return videos;
    }
}
