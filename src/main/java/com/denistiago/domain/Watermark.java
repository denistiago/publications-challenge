package com.denistiago.domain;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "content",
        visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = BookWatermark.class, name = BookWatermark.CONTENT),
        @JsonSubTypes.Type(value = JournalWatermark.class, name = JournalWatermark.CONTENT)})
public abstract class Watermark {

    @Id
    private String id;
    @NotNull
    private String title;
    @NotNull
    private String author;
    @NotNull
    private String content;
    private String documentId;
    private Status status = Status.PENDING;


    public Watermark(String title, String author, String content) {
        this.title = title;
        this.author = author;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getId() {
        return id;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void addDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Watermark{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", content='" + content + '\'' +
                ", documentId='" + documentId + '\'' +
                ", status=" + status +
                '}';
    }
}