package com.denistiago.controller;

import com.denistiago.domain.Document;
import com.denistiago.service.DocumentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class DocumentController {

    private DocumentService service;

    public DocumentController(DocumentService service) {
        this.service = service;
    }

    @RequestMapping(path = "/documents", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Document create(@Validated @RequestBody Document document) {
        log.info("creating document {}", document);
        return service.create(document);
    }

    @RequestMapping(path = "/documents/{id}", method = RequestMethod.GET)
    public Document get(@PathVariable String id) {
        log.info("get document id={}", id);
        return service.get(id);
    }

    @RequestMapping(path = "/documents", method = RequestMethod.GET)
    public Page<Document> list(@RequestParam("page") Integer page,
                               @RequestParam("size") Integer size) {
        log.info("list documents page={} size={}", page, size);
        return service.list(new PageRequest(page, size));
    }


}
