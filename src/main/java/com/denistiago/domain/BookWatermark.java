package com.denistiago.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class BookWatermark extends Watermark  {

    public static final String CONTENT = "book";

    @NotNull
    private String topic;

    @JsonCreator
    public BookWatermark(@JsonProperty("title") String title,
                         @JsonProperty("author") String author,
                         @JsonProperty("topic") String topic) {
        super(title, author, CONTENT);
        this.topic = topic;
    }

    public String getTopic() {
        return topic;
    }

    @Override
    public String toString() {
        return "BookWatermark{" +
                "topic='" + topic + '\'' +
                '}';
    }
}
