package paf.workshop.paf_28w.repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AddFieldsOperation;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationExpression;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation.AsBuilder;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.UnwindOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import com.mongodb.BasicDBObject;

@Repository
public class GameReviewsRepository {
    @Autowired
    private MongoTemplate template;

    /*
     *  db.games.aggregate([
     *      {: {'id': <id>}},
     *      {
     *          : {
     *              from: 'comments',
     *              localField: '_id',
     *              foreignField: 'gid',
     *              pipeline: [{: {rating:1, link: ''}}],
     *              as: 'reviews'
     *          }
     *      },
     *      {: ''},
     *      {
     *          : {
     *              _id: {: ''},
     *              name: {'': ''},
     *              year: {'': ''},
     *              ranking: {'': ''},
     *              avg: {'': '.rating'},
     *              users_rated: {'': ''},
     *              url: {'': ''},
     *              image: {'': ''},
     *              reviews: {: {'.link'}}
     *          }
     *      },
     *      {: {'timestamp': <date>}}
     *  ])
     */
    public Document getGameReviews(int id) {
        MatchOperation matchId = Aggregation.match(Criteria.where("_id").is(id));
        LookupOperation lookup = LookupOperation.newLookup()
            .from("comments")
            .localField("_id")
            .foreignField("gid")
            .pipeline(Aggregation.project("rating", "_id")
                .and("_id").as("link"))
            .as("reviews");
        
        UnwindOperation unwind = Aggregation.unwind("");

        GroupOperation group = Aggregation.group("_id")
            .first("name").as("name")
            .first("year").as("year")
            .first("ranking").as("ranking")
            .avg("reviews.rating").as("average")    
            .first("users_rated").as("users_rated")
            .first("url").as("url")
            .first("image").as("image")
            .push(".link")
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


    // db.games.aggregate([
    //     {: {
    //         from: 'comments',
    //         localField: '_id',
    //         foreignField: 'gid',
    //         pipeline: [{: {'rating': <asc or desc>}}],
    //         as: 'reviews'
    //     }}, 
    //     {: {
    //         _id: null,
    //         games: {
    //             : {
    //                 'name': '',
    //                 'rating': {: '.rating'},
    //                 'user': {: '.user'},
    //                 'comment': {: '.c_text'},
    //                 'review_id': {: '._id'}
    //             }
    //         }
    //     }},
    //     {project: {
    //         '_id': 0,
    //         'rating': <highest or lowest>,
    //         'timestamp': new Date(),
    //         'games': 1
    //     }}
    // ])
    public Document getHighestLowest(String order) {
        AsBuilder lookup = LookupOperation.newLookup()
            .from("comments")
            .localField("_id")
            .foreignField("gid");
        if (order.equals("lowest")) {
            lookup = lookup.pipeline(Aggregation.sort(Sort.Direction.ASC, "rating"));
        } else {
            lookup = lookup.pipeline(Aggregation.sort(Sort.Direction.DESC, "rating"));
        }
        LookupOperation lookupOperation = lookup.as("reviews");

        ProjectionOperation test = Aggregation.project("name")
            .and(ArrayOperators.First.firstOf("$reviews.rating")).as("rating")
            .and(ArrayOperators.First.firstOf("$reviews.user")).as("user")
            .and(ArrayOperators.First.firstOf("$reviews.c_text")).as("comment")
            .and(ArrayOperators.First.firstOf("$reviews._id")).as("review_id");

        GroupOperation groupAll = Aggregation.group("null")
            .push(new BasicDBObject()
                .append("name", "$name")
                .append("rating", "$rating")
                .append("user", "$user")
                .append("comment", "$comment")
                .append("review_id", "$review_id")
                )
            .as("games");
        
        AddFieldsOperation addFields = AddFieldsOperation
            .addField("rating").withValue(order)
            .addField("timestamp").withValue(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
            .build();
        
        ProjectionOperation noId = Aggregation.project("rating", "timestamp", "games")
            .andExclude("_id");
        
        Aggregation pipeline = Aggregation.newAggregation(lookupOperation,test, groupAll, addFields, noId);
        return template.aggregate(pipeline, "games", Document.class).getMappedResults().getFirst();
    }
}
