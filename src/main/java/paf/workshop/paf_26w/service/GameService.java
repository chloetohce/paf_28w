package paf.workshop.paf_26w.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import paf.workshop.paf_26w.repository.GameRepository;

@Service
public class GameService {

    @Autowired
    private GameRepository repository;
    
    public JsonObject getGamesLimit(int limit, long offset) {
        List<Document> result = repository.getGamesLimit(limit, offset);
        JsonArrayBuilder gamesBuilder = Json.createArrayBuilder();
        for (Document d : result) {
            gamesBuilder.add(d.toJson());
        }
        JsonObjectBuilder response = Json.createObjectBuilder();
        response.add("games", gamesBuilder.build());
        response.add("offset", offset);
        response.add("limit", limit);
        response.add("total", repository.getCount());
        response.add("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        return response.build();
    }
}
