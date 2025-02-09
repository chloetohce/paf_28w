package paf.workshop.paf_28w;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import paf.workshop.paf_28w.repository.GameReviewsRepository;

@SpringBootApplication
public class Paf28wApplication {
	@Autowired
	private GameReviewsRepository repository;

	public static void main(String[] args) {
		SpringApplication.run(Paf28wApplication.class, args);
	}
}
