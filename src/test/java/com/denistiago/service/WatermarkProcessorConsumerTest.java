package com.denistiago.service;

import com.denistiago.domain.Document;
import com.denistiago.domain.JournalWatermark;
import com.denistiago.domain.Status;
import com.denistiago.repository.DocumentRepository;
import com.denistiago.repository.WatermarkRepository;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class WatermarkProcessorConsumerTest {

    private WatermarkProcessorConsumer consumer;

    private WatermarkRepository watermarkRepositoryMock;
    private DocumentRepository documentRepositoryMock;

    @Before
    public void setUpMocks() {
        watermarkRepositoryMock = mock(WatermarkRepository.class);
        documentRepositoryMock = mock(DocumentRepository.class);
        consumer = new WatermarkProcessorConsumer(watermarkRepositoryMock, documentRepositoryMock);
    }

    @Test
    public void whenProcessingWatermarkShouldUpdateDocument() {

        when(watermarkRepositoryMock.findOne("1"))
                .thenReturn(new JournalWatermark("title", "author"));

        Document document = new Document("id", "title", "author");
        when(documentRepositoryMock.findOne(anyString())).thenReturn(document);

        consumer.processWatermark("1");

        verify(documentRepositoryMock, times(1)).save(any(Document.class));
        assertNotNull(document.getWatermark());
        assertEquals(Status.PROCESSED, document.getWatermark().getStatus());
    }



}
