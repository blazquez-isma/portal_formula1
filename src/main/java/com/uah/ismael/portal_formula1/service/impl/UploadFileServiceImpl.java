package com.uah.ismael.portal_formula1.service.impl;

import com.uah.ismael.portal_formula1.service.UploadFileService;
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

	private final static String UPLOADS_FOLDER = "uploads";

	@Override
	public Resource load(String filename) throws MalformedURLException {
		Path pathFoto = getPath(filename);
		LOG.debug("pathFoto: " + pathFoto);

		Resource recurso = new UrlResource(pathFoto.toUri());

		if (!recurso.exists() || !recurso.isReadable()) {
			throw new RuntimeException("Error: no se puede cargar la imagen: " + pathFoto.toString());
		}
		return recurso;
	}

	@Override
	public String copy(MultipartFile file) throws IOException {
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

		LOG.info("rootPath: " + rootPath);

		Files.copy(file.getInputStream(), rootPath);

		return uniqueFilename;
	}

	@Override
	public boolean delete(String filename) {
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
}
