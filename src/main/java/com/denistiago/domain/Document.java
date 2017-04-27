package com.denistiago.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class Document {

    private String id;
    @NotNull
    private String title;
    @NotNull
    private String author;

    private Watermark watermark;

    @JsonCreator
    public Document(@JsonProperty("id") String id,
                    @JsonProperty("title") String title,
                    @JsonProperty("author") String author) {
        this.id = id;
        this.title = title;
        this.author = author;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public Watermark getWatermark() {
        return watermark;
    }

    public void addWatermark(Watermark watermark) {
        this.watermark = watermark;
    }

    public boolean hasWatermark() {
        return watermark != null;
    }

    @Override
    public String toString() {
        return "Document{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", watermark=" + watermark +
                '}';
    }
}