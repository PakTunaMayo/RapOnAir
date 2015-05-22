package com.vicio.raponair;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by PAK on 20/05/2015.
 */
public class Tracks {

    final String TAG = "MainActivity"; //para los logs
    private ArrayList<Track> list;

    public Tracks(String xml) {
        this.list = new ArrayList();

        while (xml != "") {
            int start = xml.indexOf("<track>");
            if (start != -1) {
                int end = xml.indexOf("</track>", start);
                String trackXml = xml.substring(start, end + 8);
                Track track = new Track(trackXml);
                list.add(track);
                xml = xml.replace(trackXml, "");
            } else {
                xml = "";
            }
        }

    }

    public ArrayList<Track> getList() {
        return list;
    }

    public void setList(ArrayList<Track> list) {
        this.list = list;
    }
}
