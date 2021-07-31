package com.nuchange.informatics.repository;



import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nuchange.informatics.model.UrlEntity;

@Repository
public interface UrlRepository extends PagingAndSortingRepository<UrlEntity, Integer>{


	/*
	 * Using method Optional<UrlEntity> findByEmployeeName(@Param("name") String
	 * name);
	 * 
	 * @Query(value = "SELECT u from UrlEntity u where u.url =:url ", nativeQuery =
	 * true) @ using @query with native Optional<UrlEntity> findByUrl(@Param("url")
	 * String url);
	 */
	
    @Query("SELECT u from UrlEntity u where u.url =:url ")
    Optional<UrlEntity> findByUrl(@Param("url") String url);
    
}