package com.webapp.controller;


import java.io.StringWriter;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.webapp.entity.TestEntity;
import com.webapp.service.TestEntityService;

import org.springframework.context.ApplicationContext;



@RestController

public class RestControllerProva {

    private static final Logger LOG = LogManager.getLogger(RestControllerProva.class);

//
   @Autowired
   private ApplicationContext applicationContext;
   @Autowired
   private TestEntityService testEntityService;
   
   @GetMapping(path="/prova")
   public String prova() {
	   return "ciao";
   }

    /*
     * SpringBoot Path
     * curl -X GET http://localhost:8080/endpoint/list
     * 
     * Spring Path
     * curl -X GET http://localhost:8080/WebProjectSpringFrameWork/endpoint/list
     */
	@GetMapping(path="/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public Collection<String> getLogs() {
		LOG.info("return all DB elements");
        List<TestEntity> list = testEntityService.getTestEntityList();
        Collection<String> collection =  list.stream().filter(o -> o instanceof TestEntity)
        		.map(o -> (String) o.getDescription()).collect(Collectors.toList());
		return collection;
    }
	
	/*
     * SpringBoot Path
     * curl -X GET http://localhost:8080/endpoint/listj
     * 
     * Spring Path
     * curl -X GET http://localhost:8080/WebProjectSpringFrameWork/endpoint/listj
     */
	@GetMapping(path="/listj", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getLogsj() {
		LOG.info("return all DB elements");
		ObjectMapper mapper = new ObjectMapper();
		ArrayNode arrayNode = mapper.createArrayNode();
        List<TestEntity> list = testEntityService.getTestEntityList();
        for (Iterator<TestEntity> iterator = list.iterator(); iterator.hasNext();) {
			TestEntity testEntity = (TestEntity) iterator.next();
			ObjectNode objectNode = mapper.createObjectNode();
			objectNode.put("id", testEntity.getId());
			objectNode.put("description", testEntity.getDescription());
			arrayNode.add(objectNode);
		}
       
		return arrayNode.toString();
    }
	
	/*
     * SpringBoot Path
     * curl -X GET http://localhost:8080/endpoint/listx
     * 
     * Spring Path
     * curl -X GET http://localhost:8080/WebProjectSpringFrameWork/endpoint/listx
     */
	@GetMapping(path="/listx", produces = MediaType.APPLICATION_XML_VALUE)
    public String getLogsx() {
		LOG.info("return all DB elements");
	    StringWriter returnValue = new StringWriter();

		

		return returnValue.toString();
    }


	//curl -X POST --header "Content-Type: application/json" http://localhost:8080/WebProjectSpringFrameWork/endpoint/add --data 'test'
	@PostMapping(path= "/add", consumes = "application/json", produces = "application/json")
	public ResponseEntity<String> addLog(@RequestBody String param) {
		LOG.info("add element to DB");
		TestEntity value = new TestEntity();
		value.setDescription(param);
		testEntityService.saveTestEntity(value);
		return ResponseEntity.ok("added new row");

	}


	//curl -X DELETE --header "Content-Type: application/json" http://localhost:8080/WebProjectSpringFrameWork/endpoint/remove/test
	@DeleteMapping(path= "/remove/{param}", consumes = "application/json", produces = "application/json")
	public ResponseEntity<String> deleteLog(@PathVariable String param) {
		LOG.info("remove element from DB");
        List<TestEntity> list = testEntityService.getTestEntityList();
		for (Iterator<TestEntity> iterator = list.iterator(); iterator.hasNext();) {
			TestEntity testEntity = (TestEntity) iterator.next();
			if (testEntity.getDescription().toLowerCase().equalsIgnoreCase(param)) {
				testEntityService.deleteTestEntity(testEntity.getId());
			}			
		}
		return ResponseEntity.ok("row removed");

	}
	
	
	// https://localhost:8443/WebProjectSpringFrameWork/endpoint/system/system
	@GetMapping(path="/system/system", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getSystemDate() {
		LOG.info("return system DateTime");
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode objectNode = mapper.createObjectNode();
		objectNode.put("startupTime", java.time.LocalDateTime.now().toString());
		return objectNode.toString();
    }
	
	
	


}
