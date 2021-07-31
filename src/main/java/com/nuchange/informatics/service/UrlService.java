package com.nuchange.informatics.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.nuchange.informatics.exception.RecordNotFoundException;
import com.nuchange.informatics.model.UrlEntity;
import com.nuchange.informatics.repository.UrlRepository;

@Service
public class UrlService {

	@Autowired
	UrlRepository repository;

	public List<UrlEntity> getAllUrls(Integer pageNo, Integer pageSize, String sortBy) {
		Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

		Page<UrlEntity> pagedResult = repository.findAll(paging);

		if(pagedResult.hasContent()) {
			return pagedResult.getContent();
		} else {
			return new ArrayList<UrlEntity>();
		}
	}

}
