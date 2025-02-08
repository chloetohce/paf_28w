package paf.workshop.paf_28w.service;

import java.util.Date;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mongodb.client.result.UpdateResult;

import paf.workshop.paf_28w.model.Comment;
import paf.workshop.paf_28w.repository.CommentRepository;
import paf.workshop.paf_28w.repository.GameRepository;

@Service
public class CommentService {
    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Transactional
    public Document addComment(Comment comment) throws Exception {
        int gid = comment.getGid();
        Document game = gameRepository.getGameById(gid);
        if (game == null) {
            throw new Exception("Game with id %d not found. ".formatted(gid));
        }

        return commentRepository.addComment(comment);
    }

    public boolean updateComment(Comment comment, String id) throws Exception {
        System.out.println("Old comment: " + commentRepository.getCommentById(id));
        UpdateResult result = commentRepository.updateComment(comment, id);
        System.out.println("New comment: " + commentRepository.getCommentById(id));
        if (result.getModifiedCount() == 0) {
            throw new Exception("Error updating comment with id %s".formatted(id));
        }
        return true;
    }

    public Document getCommentById(String id) throws Exception {
        Optional<Document> opt =  commentRepository.getCommentById(id)
            .map(d -> d.append("timestamp", new Date()));
        opt.ifPresent(d -> d.replace("edited", true));
        opt.ifPresent(d -> d.putIfAbsent("edited", false));
        return opt.orElseThrow(() -> new Exception("Could not find comment with id %s.".formatted(id)));
    }

    public Document getCommentHistory(String id) throws Exception {
        Optional<Document> opt =  commentRepository.getCommentById(id)
            .map(d -> d.append("timestamp", new Date()));
        return opt.orElseThrow(() -> new Exception("Could not find comment with id %s.".formatted(id)));
    }
}
