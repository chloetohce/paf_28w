package paf.workshop.paf_26w.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.JsonObject;
import paf.workshop.paf_26w.service.GameService;


@RestController
@RequestMapping("/games")
public class GameController {

    @Autowired
    private GameService service;

    @GetMapping(path="", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getGamesLimit(@RequestParam(required=false, defaultValue="25") int limit,
            @RequestParam(required=false, defaultValue = "0") long offset) {

        JsonObject result = service.getGamesLimit(limit, offset);
        return new ResponseEntity<>(result.toString(), HttpStatus.OK);
    }

    @GetMapping(path="/rank", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getGamesByRank(@RequestParam(required=false, defaultValue="25") int limit,
            @RequestParam(required=false, defaultValue = "0") long offset) {

        JsonObject result = service.getGamesByRank(limit, offset);
        return new ResponseEntity<>(result.toString(), HttpStatus.OK);
    }

    @GetMapping(path="/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getGameById(@PathVariable String id) {
        JsonObject result = service.getGameById(Integer.parseInt(id));
        return new ResponseEntity<>(result.toString(), HttpStatus.OK);
    }
    
    
}
