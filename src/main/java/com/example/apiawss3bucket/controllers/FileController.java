package com.example.apiawss3bucket.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ObjectListing;
import com.example.apiawss3bucket.services.FileService;

@RestController
@RequestMapping("/apiawss3bucket/file")
public class FileController {

	@Autowired
	private FileService fileService;

	@GetMapping("/healthcheck")
	public ResponseEntity<String> HelthCheck() {
		return ResponseEntity.ok("Ok!");
	}

	@GetMapping(path = "/getallbuckets")
	public List<Bucket> ListBuckets() {
		return fileService.getAllBuckets();
	}

	@GetMapping(path = "/getallobjects")
	public ObjectListing ListObjects() {
		return fileService.GetAllObjects();
	}

	@GetMapping(path = "/getobject")
	public String ListObjects(@RequestParam String id) {
		return fileService.GetObject(id);
	}

	@PostMapping(path = "/upload", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	public void SendFile(@RequestParam String nameFile) {
		fileService.SendFile(nameFile);
	}

}
