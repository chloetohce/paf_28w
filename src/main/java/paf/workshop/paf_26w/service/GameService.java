package paf.workshop.paf_26w.service;

import java.io.StringReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import paf.workshop.paf_26w.repository.GameRepository;

@Service
public class GameService {

    @Autowired
    private GameRepository repository;

    private JsonObject buildResponseGames(JsonArray arr, int limit, long offset) {
        JsonObjectBuilder response = Json.createObjectBuilder();
        response.add("games", arr);
        response.add("offset", offset);
        response.add("limit", limit);
        response.add("total", repository.getCount());
        response.add("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        return response.build();
    }
    
    public JsonObject getGamesLimit(int limit, long offset) {
        List<Document> result = repository.getGamesLimit(limit, offset);
        JsonArrayBuilder gamesBuilder = Json.createArrayBuilder();
        for (Document d : result) {
            JsonObject game = Json.createObjectBuilder()
                .add("game_id", d.getInteger("_id"))
                .add("name", d.getString("name"))
                .build();
            gamesBuilder.add(game);
        }
        
        return buildResponseGames(gamesBuilder.build(), limit, offset);
    }

    public JsonObject getGamesByRank(int limit, long offset) {
        List<Document> result = repository.getGamesByRank(limit, offset);
        JsonArrayBuilder gamesBuilder = Json.createArrayBuilder();
        for (Document d : result) {
            JsonObject game = Json.createObjectBuilder()
                .add("game_id", d.getInteger("_id"))
                .add("name", d.getString("name"))
                .add("ranking", d.getInteger("ranking"))
                .build();
            gamesBuilder.add(game);
        }
        return buildResponseGames(gamesBuilder.build(), limit, offset);
    }

    public JsonObject getGameById(int id) {
        Document result = repository.getGameById(id);
        return Json.createObjectBuilder(
            Json.createReader(new StringReader(result.toJson()
                .replace("_id", "game_id")))
            .readObject())
            .add("timestamp", LocalDateTime.now().toString())
            .build();
    }
}
