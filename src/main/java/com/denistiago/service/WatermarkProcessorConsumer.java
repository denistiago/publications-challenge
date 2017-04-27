package com.denistiago.service;

import com.denistiago.amqp.AmqpConfig;
import com.denistiago.domain.Document;
import com.denistiago.domain.Status;
import com.denistiago.domain.Watermark;
import com.denistiago.repository.DocumentRepository;
import com.denistiago.repository.WatermarkRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class WatermarkProcessorConsumer {

    private WatermarkRepository watermarkRepository;
    private DocumentRepository documentRepository;

    public WatermarkProcessorConsumer(WatermarkRepository watermarkRepository,
                                      DocumentRepository documentRepository) {
        this.watermarkRepository = watermarkRepository;
        this.documentRepository = documentRepository;
    }

    @RabbitListener(queues = AmqpConfig.WATERMARK_PROCESSING)
    public void processWatermark(String id) {

        Watermark watermark = watermarkRepository.findOne(id);
        addWatermarkToDocument(watermark);
        updateWatermarkToProcessed(watermark);

    }

    private void updateWatermarkToProcessed(Watermark watermark) {
        watermark.setStatus(Status.PROCESSED);
        watermarkRepository.save(watermark);
    }

    private void addWatermarkToDocument(Watermark watermark) {
        Document document = documentRepository.findOne(watermark.getDocumentId());
        document.addWatermark(watermark);
        documentRepository.save(document);
    }


}
