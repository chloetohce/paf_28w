package paf.workshop.paf_28w;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import paf.workshop.paf_28w.repository.GameReviewsRepository;

@SpringBootApplication
public class Paf28wApplication implements CommandLineRunner {
	@Autowired
	private GameReviewsRepository repository;

	public static void main(String[] args) {
		SpringApplication.run(Paf28wApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println(repository.getGameReviews(12));
		Document d = repository.getGameReviews(12);
		List<String> list = d.getList("reviews", String.class);
		list.replaceAll(id -> "/reviews/" + id);
		System.out.println(d.replace("reviews", list));
	}

}
