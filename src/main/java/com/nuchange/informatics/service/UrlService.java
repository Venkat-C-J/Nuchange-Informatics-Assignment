package com.nuchange.informatics.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
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

	//Helper method to get the UrlEntity by url
	public Optional<UrlEntity> getUrl(String theUrl) {

		//get current hibernate session
		Session currentSession =
				entitymanager.unwrap(Session.class);

		//get the url by imposing the Criteria
		@SuppressWarnings("deprecation")
		Criteria criteria = currentSession.createCriteria(UrlEntity.class)
				.add(Restrictions.eq("url", theUrl));

		// Convenience method to return a single instance that matches
		// the query, or null if the query returns no results.
		Object result = criteria.uniqueResult();
		
		//if result is not null then return the result by converting to URL
		if(result != null) {
			
			UrlEntity url = (UrlEntity) result;
			
			return Optional.of(url);
		}
		
		return Optional.empty();

	}

	public List<UrlEntity> getAllUrls(Integer pageNo, Integer pageSize, String sortBy) {
		
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
		
		Optional<UrlEntity> url = getUrl(entity.getUrl());

		//save the url if url does not exit and return the url
		if(! url.isPresent()) {
			
			entity = repository.save(entity);
			
			return entity;
			
		}
		
		//return the existing url
		UrlEntity newEntity = url.get();
		newEntity.setUrl(entity.getUrl());
		newEntity.setUsageCount(entity.getUsageCount());
		
		return newEntity;
	}

	//method to get the unique short key of the url
	@Transactional
	public int getUrlId(String theUrl) throws RecordNotFoundException{

		//get current hibernate session
		Session currentSession =
				entitymanager.unwrap(Session.class);

		//get the url
		Optional<UrlEntity> url = getUrl(theUrl);

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
		Optional<UrlEntity> url = getUrl(theUrl);
		
		if(url.isPresent()) {
			return url.get().getUsageCount();
		}else {
			throw new RecordNotFoundException("Url not found : " +theUrl);
		}
	}
	
}
