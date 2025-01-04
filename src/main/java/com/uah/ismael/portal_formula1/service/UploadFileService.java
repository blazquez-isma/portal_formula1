package com.uah.ismael.portal_formula1.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;

public interface UploadFileService {

	Resource load(String filename, String type) throws MalformedURLException;

	String copy(MultipartFile file, String type) throws IOException;

	boolean delete(String filename, String type);

	void deleteAll();

	void init() throws IOException;
}