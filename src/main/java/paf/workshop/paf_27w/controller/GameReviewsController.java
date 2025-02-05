package paf.workshop.paf_27w.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/game")
public class GameReviewsController {
    
    @GetMapping(path="/{id}/reviews", produces=MediaType.APPLICATION_JSON_VALUE)
    public String getMethodName(@PathVariable int id) {
        return new String();
    }
    
}
