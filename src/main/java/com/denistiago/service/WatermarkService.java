package com.denistiago.service;

import com.denistiago.amqp.AmqpConfig;
import com.denistiago.domain.Document;
import com.denistiago.domain.Watermark;
import com.denistiago.exception.ConflictException;
import com.denistiago.exception.NotFoundException;
import com.denistiago.repository.DocumentRepository;
import com.denistiago.repository.WatermarkRepository;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;


@Service
public class WatermarkService {

    private DocumentRepository documentRepository;
    private WatermarkRepository watermarkRepository;
    private AmqpTemplate amqpTemplate;

    public WatermarkService(DocumentRepository documentRepository,
                            WatermarkRepository watermarkRepository,
                            AmqpTemplate amqpTemplate) {
        this.documentRepository = documentRepository;
        this.watermarkRepository = watermarkRepository;
        this.amqpTemplate = amqpTemplate;
    }

    public Watermark create(String documentId, Watermark watermark) {
        Document document = documentRepository.findOne(documentId);
        validate(document);
        return saveAndProcess(document.getId(), watermark);
    }

    private void validate(Document document) {

        if (document == null) {
            throw new NotFoundException("document not found");
        }

        if (document.hasWatermark()) {
            throw new ConflictException("document already has watermark");
        }

        if (watermarkRepository.findByDocumentId(document.getId()) != null) {
            throw new ConflictException("watermark already created");
        }

    }

    private Watermark saveAndProcess(String documentId, Watermark watermark) {
        watermark.addDocumentId(documentId);
        watermarkRepository.save(watermark);
        amqpTemplate.convertAndSend(AmqpConfig.WATERMARK_PROCESSING, watermark.getId());
        return watermark;
    }

    public Watermark get(String watermarkId) {
        Watermark watermark = watermarkRepository.findOne(watermarkId);
        if (watermark == null) {
            throw new NotFoundException("watermark not found");
        }
        return watermark;
    }
}