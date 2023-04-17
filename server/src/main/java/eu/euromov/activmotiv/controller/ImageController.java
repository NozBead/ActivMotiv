package eu.euromov.activmotiv.controller;

import java.io.File;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eu.euromov.activmotiv.model.Photos;
import jakarta.websocket.server.PathParam;

@RestController
@RequestMapping(path="/image")
public class ImageController {
	private String workingDir = getClass().getResource("/").getPath();
	
	private Photos getImages(String type) {
		File dir = new File(workingDir + "/res/" + type);
		Photos photos = new Photos();
		List<String> images = Stream.of(dir.listFiles())
			.map(e -> e.getName())
			.toList();
		photos.setPhotos(images);
		return photos;
	}
	
	@GetMapping("/{type}")
	public Photos getPositives(@PathParam("type") String type) {
		return getImages(type);
	}
}