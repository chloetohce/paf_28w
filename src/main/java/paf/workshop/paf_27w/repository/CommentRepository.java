package paf.workshop.paf_27w.repository;

import java.util.Date;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.mongodb.client.result.UpdateResult;

import jakarta.json.Json;
import paf.workshop.paf_27w.model.Comment;

@Repository
public class CommentRepository {
    @Autowired
    private MongoTemplate template;

    public Document getCommentById(String id) {
        Criteria criteria = Criteria.where("_id").is(id);
        Query q = new Query(criteria);
        return template.findOne(q, Document.class, "comments");
    }

    public Document addComment(Comment comment) {
        Document insertC = new Document()
            .append("user", comment.getUser())
            .append("rating", comment.getRating())
            .append("gid", comment.getGid())
            .append("posted", comment.getPosted())
            .append("c_text", comment.getComment());
        Document inserted = template.insert(insertC, "comments");
        return inserted;
    }
    
    public UpdateResult updateComment(Comment comment, String id) {
        Document old = getCommentById(id);
        Query q = Query.query(Criteria.where("_id").is(id));
        Update update = new Update()
            .set("rating", 
                Optional.ofNullable(comment.getRating())
                    .orElse(old.getInteger("rating")))
            .set("c_text",
                Optional.ofNullable(comment.getComment())
                    .orElse(old.getString("c_text")))
            .set("posted", new Date())
            .push("edited")
                .each(
                    Json.createObjectBuilder()
                        .add("comment", old.getString("c_text"))
                        .add("rating", old.getInteger("rating"))
                        .add("posted", old.getOrDefault("posted", new Date()).toString())
                        .build()
                        .toString()
                );
        return template.updateFirst(q, update, Document.class, "comments");
    }
}
