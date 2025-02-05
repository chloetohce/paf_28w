package paf.workshop.paf_27w.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class GameReviewsController {
    @Autowired
    private MongoTemplate template;

    
}
