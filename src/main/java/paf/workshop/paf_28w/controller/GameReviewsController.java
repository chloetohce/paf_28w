package paf.workshop.paf_28w.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import paf.workshop.paf_28w.service.GameReviewsService;


@RestController
@RequestMapping("/game")
public class GameReviewsController {
    @Autowired
    private GameReviewsService service;
    
    @GetMapping(path="{id}/reviews", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getGameReviews(@PathVariable int id) {
        Optional<String> opt = service.getGameReviews(id).map(d -> d.toJson());
        return ResponseEntity.ok()
            .body(opt.orElse(
                Json.createObjectBuilder()
                    .add("message", "Game with id %d not found.".formatted(id))   
                    .build().toString()
            ));
    }
    
}
