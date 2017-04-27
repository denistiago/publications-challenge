package com.denistiago.service;

import com.denistiago.domain.Document;
import com.denistiago.domain.JournalWatermark;
import com.denistiago.domain.Watermark;
import com.denistiago.exception.ConflictException;
import com.denistiago.exception.NotFoundException;
import com.denistiago.repository.DocumentRepository;
import com.denistiago.repository.WatermarkRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.amqp.core.AmqpTemplate;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class WatermarkServiceTest {

    private WatermarkService service;

    private DocumentRepository documentRepositoryMock;
    private WatermarkRepository watermarkRepositoryMock;
    private AmqpTemplate amqpTemplateMock;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUpMocks() {

        this.documentRepositoryMock = mock(DocumentRepository.class);
        this.watermarkRepositoryMock = mock(WatermarkRepository.class);
        this.amqpTemplateMock = mock(AmqpTemplate.class);

        this.service = new WatermarkService(documentRepositoryMock, watermarkRepositoryMock, amqpTemplateMock);
    }

    @Test
    public void shouldThrownBadRequestIfDocumentDoesNotExists() {

        expectedException.expect(NotFoundException.class);
        expectedException.expectMessage("document not found");

        this.service.create("1", new JournalWatermark("title", "author"));
    }

    @Test
    public void shouldThrownConflictExceptionIfDocumentAlreadyHasWatermark() {

        expectedException.expect(ConflictException.class);
        expectedException.expectMessage("document already has watermark");

        Document document = new Document("id", "title", "author");
        document.addWatermark(new JournalWatermark("", ""));
        when(documentRepositoryMock.findOne(anyString()))
                .thenReturn(document);
        this.service.create("1", document.getWatermark());
    }

    @Test
    public void shouldThrownConflictExceptionIfPendingWatermarkExists() {

        expectedException.expect(ConflictException.class);
        expectedException.expectMessage("watermark already created");

        Document document = new Document("id", "title", "author");
        Watermark watermark = new JournalWatermark("title", "author");

        when(documentRepositoryMock.findOne(anyString()))
                .thenReturn(document);


        when(watermarkRepositoryMock.findByDocumentId(anyString()))
                .thenReturn(watermark);

        this.service.create("1", document.getWatermark());

    }

    @Test
    public void shouldCreateWatermark() {

        Document document = new Document("id", "title", "author");
        Watermark watermark = new JournalWatermark("title", "author");

        when(documentRepositoryMock.findOne(anyString()))
                .thenReturn(document);

        this.service.create(anyString(), watermark);

        assertNotNull(watermark.getDocumentId());
        verify(watermarkRepositoryMock, times(1)).save(watermark);
        verify(amqpTemplateMock, times(1)).convertAndSend(anyString(), any(Watermark.class));

    }

}
