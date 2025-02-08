package paf.workshop.paf_28w.repository;

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

import paf.workshop.paf_28w.model.Comment;

@Repository
public class CommentRepository {
    @Autowired
    private MongoTemplate template;

    public Optional<Document> getCommentById(String id) {
        Criteria criteria = Criteria.where("_id").is(id);
        Query q = new Query(criteria);
        Document doc = template.findOne(q, Document.class, "comments");
        Optional<Document> result = Optional.ofNullable(doc);
        return result;
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
    
    public UpdateResult updateComment(Comment comment, String id) throws Exception {
        Document old = getCommentById(id).orElseThrow(() -> new Exception("Comment with id %s could not be found. ".formatted(id)));
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
                    new Document()
                        .append("comment", old.getString("c_text"))
                        .append("rating", old.getInteger("rating"))
                        .append("posted", old.getOrDefault("posted", new Date()))
                );
        return template.updateFirst(q, update, Document.class, "comments");
    }
}
