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
            int empieza = xml.indexOf("<title>");
            int fin = xml.indexOf("</title>", empieza);
            setTitle(xml.substring(empieza+7, fin));


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
