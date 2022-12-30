package co.com.api.mongo.cyclist;

import co.com.api.model.cyclist.Cyclist;
import co.com.api.model.team.Team;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import reactor.core.publisher.Flux;

public interface CyclistMongoDBRepository extends ReactiveMongoRepository<CyclistDocument, String>, ReactiveQueryByExampleExecutor<CyclistDocument> {

    Flux<CyclistDocument> findAllCyclistByNationality(String nationality);
    Flux<CyclistDocument> findAllCyclistByTeamCode(String teamCode);
}
