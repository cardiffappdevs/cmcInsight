package com.example.eugein.cmc_insights.Model;

import java.io.Serializable;

/**
 * Created by Eugein on 4/3/18.
 */

public class Post implements Serializable{
    private String id;
    private String title;
    private String slug;
    private String brief_content;
    private String extended_content;
    private String image;
    private String youtube;
    private String youtube_id;
    private String author;
    private String link;
    private String date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getBrief_content() {
        return brief_content;
    }

    public void setBrief_content(String brief_content) {
        this.brief_content = brief_content;
    }

    public String getExtended_content() {
        return extended_content;
    }

    public void setExtended_content(String extended_content) {
        this.extended_content = extended_content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getYoutube() {
        return youtube;
    }

    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }

    public String getYoutube_id() {
        return youtube_id;
    }

    public void setYoutube_id(String youtube_id) {
        this.youtube_id = youtube_id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
