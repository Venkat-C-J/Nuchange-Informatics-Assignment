package com.nuchange.informatics.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.query.Query;
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
	
	//define a field for entity manager
	private EntityManager entitymanager;

	//set up constructor injection
	@Autowired
	public UrlService(EntityManager theEntitymanager) {

		entitymanager = theEntitymanager;
	}

	public List<UrlEntity> getAllUrls(Integer pageNo, Integer pageSize) {
		
		//create the Pageable object to pass as a parameter to findAll method
		Pageable paging = PageRequest.of(pageNo, pageSize);

		//call the findAll method with above Pageable object which will return Page<UrlEntity>
		Page<UrlEntity> pagedResult = repository.findAll(paging);

		//return the results
		if(pagedResult.hasContent()) {
			return pagedResult.getContent();
		} else {
			return new ArrayList<UrlEntity>();
		}
	}

	public List<UrlEntity> getAllUrlsBySort(Integer pageNo, Integer pageSize, String sortBy) {
		
		//create the Pageable object to pass as a parameter to findAll method
		Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

		//call the findAll method with above Pageable object which will return Page<UrlEntity>
		Page<UrlEntity> pagedResult = repository.findAll(paging);
		

		//return the results
		if(pagedResult.hasContent()) {
			return pagedResult.getContent();
		} else {
			return new ArrayList<UrlEntity>();
		}
	}
	
	//method to save or update the url
	public UrlEntity createOrUpdateUrl(UrlEntity entity) {
		
		Optional<UrlEntity> url = repository.findByUrl(entity.getUrl());

		//save the url if url does not exit and return the url
		if(! url.isPresent()) {
			
			entity = repository.save(entity);
			
			return entity;
			
		}
		
		/*
		 * if url exists and the usage count of the url is not equal 
		 * to the usage count in DB save the url
		 */
		if((url.get().getUsageCount() != entity.getUsageCount()) && url.isPresent()) {
			
			UrlEntity newEntity = url.get();
            newEntity.setUrl(entity.getUrl());
            newEntity.setUsageCount(entity.getUsageCount());
            newEntity = repository.save(newEntity);
             
            return newEntity;
		}
		
		//else return the url from DB
		return url.get();
	}

	//method to get the unique short key of the url
	@Transactional
	public int getUrlId(String theUrl) throws RecordNotFoundException{

		//get current hibernate session
		Session currentSession =
				entitymanager.unwrap(Session.class);

		//get the url
		Optional<UrlEntity> url = repository.findByUrl(theUrl);

		//if url exist return the unique short key after incrementing the usage count.
		if(url.isPresent()) {
						
			@SuppressWarnings("rawtypes")
			Query query=currentSession.
					createQuery("UPDATE UrlEntity set usageCount =:usage WHERE url =:theUrl");
			query.setParameter("usage", url.get().getUsageCount()+1);
			query.setParameter("theUrl", theUrl);
			query.executeUpdate();
			
			return url.get().getId(); 

		}else {
			
			throw new RecordNotFoundException("Url not found : " +theUrl);
		}
	}
	
	
	public int getUrlUsageCount(String theUrl) throws RecordNotFoundException{
		
		//get the url and return the usage count
		Optional<UrlEntity> url = repository.findByUrl(theUrl);
		
		if(url.isPresent()) {
			return url.get().getUsageCount();
		}else {
			throw new RecordNotFoundException("Url not found : " +theUrl);
		}
	}
	
	public UrlEntity delete(String theUrl) throws RecordNotFoundException{
		
		//get the url
		Optional<UrlEntity> url = repository.findByUrl(theUrl);
		
		//if url is present delete the url
		if(url.isPresent()) {
			repository.delete(url.get());
			return url.get();
		}else {
			throw new RecordNotFoundException("Url not found : " +theUrl);
		}
	}
	
}
