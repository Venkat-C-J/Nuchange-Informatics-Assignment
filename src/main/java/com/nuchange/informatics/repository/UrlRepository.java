package com.nuchange.informatics.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.nuchange.informatics.model.UrlEntity;

@Repository
public interface UrlRepository extends PagingAndSortingRepository<UrlEntity, Integer>{

	public int getByUrl(String theUrl);
}
