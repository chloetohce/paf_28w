package paf.workshop.paf_28w.repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AddFieldsOperation;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.UnwindOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

@Repository
public class GameReviewsRepository {
    @Autowired
    private MongoTemplate template;

    public Document getGameReviews(int id) {
        MatchOperation matchId = Aggregation.match(Criteria.where("_id").is(id));
        LookupOperation lookup = LookupOperation.newLookup()
            .from("comments")
            .localField("_id")
            .foreignField("gid")
            .pipeline(Aggregation.project("rating", "_id")
                .and("_id").as("link"))
            .as("reviews");
        
        UnwindOperation unwind = Aggregation.unwind("$reviews");

        GroupOperation group = Aggregation.group("_id")
            .first("name").as("name")
            .first("year").as("year")
            .first("ranking").as("ranking")
            .avg("reviews.rating").as("average")    
            .first("users_rated").as("users_rated")
            .first("url").as("url")
            .first("image").as("image")
            .push("$reviews.link")
                .as("reviews");
        
        AddFieldsOperation timestamp = Aggregation.addFields()
            .addFieldWithValue("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
            .build();
        
        Aggregation pipeline = Aggregation.newAggregation(matchId, lookup, unwind, group, timestamp);
        Document d = template.aggregate(pipeline, "games", Document.class).getMappedResults().getFirst();

        // Change reviews link
        List<String> links = d.getList("reviews", String.class);
        links.replaceAll(i -> "/review/" + i);
        d.replace("reviews", links);
        return d;
    }
}
