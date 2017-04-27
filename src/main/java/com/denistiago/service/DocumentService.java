package com.denistiago.service;

import com.denistiago.domain.Document;
import com.denistiago.repository.DocumentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class DocumentService {

    private DocumentRepository repository;

    public DocumentService(DocumentRepository repository) {
        this.repository = repository;
    }

    public Document create(Document document) {
        return repository.save(document);
    }

    public Document get(String id) {
        return repository.findOne(id);
    }

    public Page<Document> list(PageRequest pageRequest) {
        return repository.findAll(pageRequest);
    }

}
