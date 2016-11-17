package com.downtube.videos.vine;

import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import twitter4j.Status;
import twitter4j.URLEntity;
/**
 * Created by B.E.L on 16/11/2016.
 */

public class VineUtil {

    public static String findVineUrl(Status status) {

        String vineUrl;

        for (URLEntity url : status.getURLEntities()) {
            if (url.getExpandedURL().contains("vine.co/v/")) {
                vineUrl = url.getExpandedURL();
                return vineUrl;
            }
        }

        return null;
    }

    public static String parseDownloadUrl(String html) {

        // if 404 or the url suffix is missing from the html
        if (html == null || html.contains("Page not found") ){
            //	|| html.contains("<title>Vine</title>")) {
            return null;
        }

        // parses out the .mp4 file path
        try {
            String[] split = html.split("player:stream\" content=\"");
            split = split[1].split("\\?versionId");
            return split[0];

        } catch (ArrayIndexOutOfBoundsException e1) {
            return null;
        }
    }

    public static String parseThumbUrl(String html) {

        // if 404 or the url suffix is missing from the html
        if (html == null || html.contains("Page not found") ){
            //	|| html.contains("<title>Vine</title>")) {
            return null;
        }

        // parses out the .mp4 file path
        try {
            String[] split = html.split("twitter:image:src\" content=\"");
            split = split[1].split("\">");
            return split[0];

        } catch (ArrayIndexOutOfBoundsException e1) {
            return null;
        }
    }


    public static boolean downloadVine(File saveDirectory, long id,
                                       String downloadUrl) {
        try {
            File f = new File(saveDirectory,  id + ".mp4");
            FileUtils.copyURLToFile(new URL(downloadUrl), f);
            return true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private final static String USER_AGENT = "Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2049.0 Safari/537.36";

    public static String sendGet(String u) throws IOException {
        String url = u;
        URL obj = new URL(url);

        try {
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // handles the response code 301 redirect from vine URLs
            String redirect = con.getHeaderField("Location");
            if (redirect != null) {
                obj = new URL(redirect);
            } else {
                obj = new URL(u);
            }
            con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", USER_AGENT);

            int responseCode = con.getResponseCode();
            if (responseCode > 200) {
                return null;
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return response.toString();

        } catch (Exception e) {
            return null;
        }
    }
}