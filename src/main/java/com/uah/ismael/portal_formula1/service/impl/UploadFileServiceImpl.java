package com.uah.ismael.portal_formula1.service.impl;

import com.uah.ismael.portal_formula1.service.UploadFileService;
import com.uah.ismael.portal_formula1.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class UploadFileServiceImpl implements UploadFileService {

	private final Logger LOG = LoggerFactory.getLogger(UploadFileServiceImpl.class);

	private static String UPLOADS_FOLDER = Constants.UPLOADS_FOLDER;

	@Override
	public Resource load(String filename, String type) throws MalformedURLException {
		setPath(type);
		Path pathFoto = getPath(filename);
		Resource recurso = new UrlResource(pathFoto.toUri());

		if (!recurso.exists() || !recurso.isReadable()) {
			throw new RuntimeException("Error: no se puede cargar la imagen: " + pathFoto.toString());
		}
		return recurso;
	}

	@Override
	public String copy(MultipartFile file, String type) throws IOException {
		setPath(type);
		init();
		String originalFilename = file.getOriginalFilename();
		String uniqueFilename = originalFilename;
		Path rootPath = getPath(uniqueFilename);
		int counter = 1;

		while (Files.exists(rootPath)) {
			String name = originalFilename.substring(0, originalFilename.lastIndexOf('.'));
			String extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
			uniqueFilename = name + "(" + counter + ")" + extension;
			rootPath = getPath(uniqueFilename);
			counter++;
		}
		
		Files.copy(file.getInputStream(), rootPath);

		return uniqueFilename;
	}

	@Override
	public boolean delete(String filename, String type) {
		setPath(type);
		Path rootPath = getPath(filename);
		File archivo = rootPath.toFile();

		if (archivo.exists() && archivo.canRead()) {
			if (archivo.delete()) {
				return true;
			}
		}
		return false;
	}

	public Path getPath(String filename) {
		return Paths.get(UPLOADS_FOLDER).resolve(filename).toAbsolutePath();
	}

	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(Paths.get(UPLOADS_FOLDER).toFile());
	}

	@Override
	public void init() throws IOException {
		//crear directorio uploads solo si no existe
		Path path = Paths.get(UPLOADS_FOLDER);
		if (!Files.exists(path))
			Files.createDirectory(path);
	}

	private void setPath(String type){
		switch (type) {
			case Constants.NOTICIAS -> UPLOADS_FOLDER = Constants.NOTICIAS_PATH;
			case Constants.EQUIPOS -> UPLOADS_FOLDER = Constants.EQUIPOS_PATH;
			case Constants.PILOTOS -> UPLOADS_FOLDER = Constants.PILOTOS_PATH;
			case Constants.CIRCUITOS -> UPLOADS_FOLDER = Constants.CIRCUITOS_PATH;
			default -> UPLOADS_FOLDER = Constants.UPLOADS_FOLDER;
		}
	}

}
