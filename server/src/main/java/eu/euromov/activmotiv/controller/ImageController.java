package eu.euromov.activmotiv.controller;

import java.io.File;
import java.nio.file.Path;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping(path="/image")
public class ImageController {
	
	private Log log = LogFactory.getLog(ImageController.class);
	
	private String workingDir = getClass().getResource("/").getPath();
	private Random rng = new Random();
	
	private File positiveDir = new File(workingDir + "/static/positive");
	private File sportDir = new File(workingDir + "/static/sport");
	
	private String getRandomImage(File dir) {
		File[] files = dir.listFiles();
		Path random = files[rng.nextInt(files.length)].toPath();
		
		return dir.toPath().resolve("..").relativize(random).toString();
	}
	@GetMapping("/positive")
	public View randomPositive() {
		return new RedirectView("/res/" + getRandomImage(positiveDir));
	}
	
	@GetMapping("/sport")
	public View  randomSport() {
		return new RedirectView("/res/" + getRandomImage(sportDir));
	}
}