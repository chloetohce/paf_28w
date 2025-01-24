package paf.workshop.paf_27w.controller;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import paf.workshop.paf_27w.model.Comment;
import paf.workshop.paf_27w.service.CommentService;


@RestController
@RequestMapping("")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping(path = "/review", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Submit a review", description = "Accepts review data in x-www-form-urlencoded format", requestBody = @RequestBody(content = @Content(mediaType = MediaType.APPLICATION_FORM_URLENCODED_VALUE, schema = @Schema(implementation = Comment.class), encoding = {
            @Encoding(name = "user", contentType = "application/x-www-form-urlencoded"),
            @Encoding(name = "rating", contentType = "application/x-www-form-urlencoded"),
            @Encoding(name = "comment", contentType = "application/x-www-form-urlencoded"),
            @Encoding(name = "gid", contentType = "application/x-www-form-urlencoded"),
            @Encoding(name = "posted", contentType = "application/x-www-form-urlencoded")
    })))
    public ResponseEntity<String> review(@ModelAttribute Comment comment) {
        try {
            Document d = commentService.addComment(comment);
            return ResponseEntity.ok(d.toJson());
        } catch (Exception e) {
            return new ResponseEntity<>("{\"message\": \"%s\"}".formatted(e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/review/{id}")
    @Operation(requestBody = @RequestBody(
        content=@Content(
            mediaType=MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            schema=@Schema(implementation=Comment.class),
            encoding={
                @Encoding(name="rating",contentType="application/x-www-form-urlencoded"),
                @Encoding(name="comment", contentType = "application/x-www-form-urlencoded")
            }
        )
    ))
    public ResponseEntity<String> update(@PathVariable String id, @ModelAttribute Comment comment) {
        try {
            commentService.updateComment(comment, id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return new ResponseEntity<>("{\"message\": \"%s\"}".formatted(e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

}
