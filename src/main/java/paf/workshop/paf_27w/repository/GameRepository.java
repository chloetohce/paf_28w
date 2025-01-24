package paf.workshop.paf_27w.repository;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
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
        q.fields()
            .include("name");
        return template.find(q, Document.class, "games");
    }

    public long getCount() {
        return template.count(new Query(), "games");
    }

    public List<Document> getGamesByRank(int limit, long offset) {
        Query q = new Query()
            .with(Sort.by(Sort.Direction.ASC, "ranking"))
            .limit(limit)
            .skip(offset);
        q.fields()
            .include("name", "ranking");
        return template.find(q, Document.class, "games");
    }

    public Document getGameById(int id) {
        Criteria criteria = Criteria
            .where("_id").is(id);
        Query q = new Query(criteria);
        q.fields()
            .include("_id", "name", "year", "ranking", "average", "users_rated", "url", "thumbnail");
        return template.findOne(q, Document.class, "games");
    }
}
