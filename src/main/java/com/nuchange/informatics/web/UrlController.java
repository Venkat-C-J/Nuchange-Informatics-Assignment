package com.nuchange.informatics.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nuchange.informatics.model.UrlEntity;
import com.nuchange.informatics.service.UrlService;

@RestController
@RequestMapping("/urls")
public class UrlController {
	
    @Autowired
    UrlService service;
 
    @GetMapping("/list")
    public ResponseEntity<List<UrlEntity>> getAllUrls(
                        @RequestParam(defaultValue = "0") Integer page,
                        @RequestParam(defaultValue = "10") Integer size,
                        @RequestParam(defaultValue = "id") String sortBy)
    {
        List<UrlEntity> list = service.getAllUrls(page, size, sortBy);
 
        return new ResponseEntity<List<UrlEntity>>(list, new HttpHeaders(), HttpStatus.OK);
    }

}
