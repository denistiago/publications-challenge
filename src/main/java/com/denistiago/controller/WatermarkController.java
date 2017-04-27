package com.denistiago.controller;

import com.denistiago.domain.Watermark;
import com.denistiago.service.WatermarkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class WatermarkController {

    private WatermarkService service;

    public WatermarkController(WatermarkService watermarkService) {
        this.service = watermarkService;
    }

    @RequestMapping(path = "/documents/{id}/watermark", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Watermark create(@PathVariable String id,
                       @Validated @RequestBody Watermark watermark) {
        log.info("create watermark {}", watermark);
        return service.create(id, watermark);
    }

    @RequestMapping(path = "/watermarks/{id}", method = RequestMethod.GET)
    public Watermark get(@PathVariable String id) {
        log.info("get watermark id={}", id);
        return service.get(id);
    }



}
