package co.com.api.mongo.team;

import co.com.api.mongo.cyclist.CyclistDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;

public interface TeamMongoDBRepository extends ReactiveMongoRepository<TeamDocument, String>, ReactiveQueryByExampleExecutor<TeamDocument> {
}

