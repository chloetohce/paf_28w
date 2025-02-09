package paf.workshop.paf_28w.service;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import paf.workshop.paf_28w.repository.GameReviewsRepository;

@Service
public class GameReviewsService {
    @Autowired
    private GameReviewsRepository repository;

    public Optional<Document> getGameReviews(int id) {
        try {
            return Optional.of(repository.getGameReviews(id));
        } catch (NoSuchElementException e) {
            return Optional.empty();
        }
    }
}
