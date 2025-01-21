package paf.workshop.paf_26w.repository;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class GameRepository {
    @Autowired
    private MongoTemplate template;

    public List<Document> getGamesLimit(int limit, long offset) {
        Query q = new Query()
            .limit(limit)
            .skip(offset);
        return template.find(q, Document.class, "games");
    }
}
