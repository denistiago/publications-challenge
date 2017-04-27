package com.denistiago.repository;

import com.denistiago.domain.Document;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface DocumentRepository extends PagingAndSortingRepository<Document, String> {

}
