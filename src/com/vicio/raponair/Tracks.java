package com.vicio.raponair;

import java.util.ArrayList;

/**
 * Created by PAK on 20/05/2015.
 */
public class Tracks {

    private ArrayList<Track> list;

    public Tracks(String xml) {
        this.list = new ArrayList();

        while (xml != "") {
            int empieza = xml.indexOf("<track>");
            int fin = xml.indexOf("</track>", empieza);
            String trackXml = xml.substring(empieza, fin + 8);
            Track track = new Track(trackXml);
            list.add(track);
            xml.replace(trackXml, "");
        }

    }

    public ArrayList<Track> getList() {
        return list;
    }

    public void setList(ArrayList<Track> list) {
        this.list = list;
    }
}
