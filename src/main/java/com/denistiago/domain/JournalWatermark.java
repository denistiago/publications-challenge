package com.denistiago.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class JournalWatermark extends Watermark {

    public static final String CONTENT = "journal";

    @JsonCreator
    public JournalWatermark(@JsonProperty("title") String title,
                            @JsonProperty("author") String author) {
        super(title, author, "journal");
    }

}
