package com.vicio.raponair;



/**
 * Created by PAK on 20/05/2015.
 */

public class Track {
    private String title, artits, dateofdiff, cover;
    private Integer playduratuion;

    public Track(String title, String artits, String dateofdiff, String cover, Integer playduratuion) {
        this.title = title;
        this.artits = artits;
        this.dateofdiff = dateofdiff;
        this.cover = cover;
        this.playduratuion = playduratuion;
    }
    public Track(String xml){
        try {
            int start = xml.indexOf("<title>");
            int end = xml.indexOf("</title>", start);
            setTitle(xml.substring(start+7, end));
            start = xml.indexOf("<artists>");
            end = xml.indexOf("</artists>", start);
            setArtits(xml.substring(start + 9, end));
            start = xml.indexOf("<cover>");
            end = xml.indexOf("</cover>", start);
            setCover(xml.substring(start + 7, end));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtits() {
        return artits;
    }

    public void setArtits(String artits) {
        this.artits = artits;
    }

    public String getDateofdiff() {
        return dateofdiff;
    }

    public void setDateofdiff(String dateofdiff) {
        this.dateofdiff = dateofdiff;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public Integer getPlayduratuion() {
        return playduratuion;
    }

    public void setPlayduratuion(Integer playduratuion) {
        this.playduratuion = playduratuion;
    }
}
