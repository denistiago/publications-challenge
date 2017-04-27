package com.denistiago.repository;

import com.denistiago.domain.Watermark;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface WatermarkRepository extends PagingAndSortingRepository<Watermark, String> {

    Watermark findByDocumentId(String id);
}
