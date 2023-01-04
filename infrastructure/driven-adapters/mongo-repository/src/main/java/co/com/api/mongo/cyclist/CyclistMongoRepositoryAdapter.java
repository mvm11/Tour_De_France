package co.com.api.mongo.cyclist;

import co.com.api.model.common.ex.BusinessException;
import co.com.api.model.cyclist.Cyclist;
import co.com.api.model.cyclist.gateways.CyclistRepository;
import co.com.api.model.team.Team;
import co.com.api.mongo.helper.AdapterOperations;
import co.com.api.mongo.team.TeamMongoDBRepository;
import com.mongodb.DuplicateKeyException;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class CyclistMongoRepositoryAdapter extends AdapterOperations<Cyclist, CyclistDocument, String, CyclistMongoDBRepository>
implements CyclistRepository
{
    private final ReactiveMongoTemplate mongoTemplate;
    private final TeamMongoDBRepository teamMongoDBRepository;
    public CyclistMongoRepositoryAdapter(CyclistMongoDBRepository repository, ObjectMapper mapper, ReactiveMongoTemplate mongoTemplate, TeamMongoDBRepository teamMongoDBRepository) {
        super(repository, mapper, d -> mapper.map(d, Cyclist.class));
        this.mongoTemplate = mongoTemplate;
        this.teamMongoDBRepository = teamMongoDBRepository;
    }

    @Override
    public Flux<Cyclist> findAllCyclist() {
        return mongoTemplate.findAll(Team.class)
                .flatMapIterable(Team::getCyclists)
                .map(this::buildCyclist)
                .onErrorResume(this::getFindCyclistsError);
    }

    private Mono<Cyclist> getFindCyclistsError(Throwable error) {
        return Mono.error(new RuntimeException("Error getting all cyclist from MongoDB" + error.getMessage()));
    }

    private Cyclist buildCyclist(Cyclist cyclist) {
        return Cyclist.builder()
                .cyclistName(cyclist.getCyclistName())
                .cyclistNumber(cyclist.getCyclistNumber())
                .teamCode(cyclist.getTeamCode())
                .nationality(cyclist.getNationality())
                .build();
    }

    @Override
    public Flux<Cyclist> findAllCyclistByTeamCode(String teamCode) {
        return mongoTemplate.find(Query.query(Criteria.where("teamCode").is(teamCode)), Team.class)
                .flatMap(team -> Flux.fromIterable(team.getCyclists()))
                .onErrorResume(this::getFindCyclistsError);
    }

    @Override
    public Flux<Cyclist> findAllCyclistByNationality(String nationality) {
        return mongoTemplate.findAll(Team.class)
                .flatMapIterable(Team::getCyclists)
                .filter(cyclist -> cyclist.getNationality().equalsIgnoreCase(nationality))
                .map(this::buildCyclist)
                .onErrorResume(this::getFindCyclistsError);
    }
    @Override
    public Mono<Cyclist> findCyclistByCyclistNumber(String teamCode, String cyclistNumber) {
        return mongoTemplate.find(Query.query(Criteria.where("teamCode").is(teamCode)), Team.class)
                .flatMap(team -> Flux.fromIterable(team.getCyclists()))
                .filter(cyclist -> cyclist.getCyclistNumber().equalsIgnoreCase(cyclistNumber))
                .next()
                .onErrorResume(this::getFindCyclistError);
    }
    private Mono<Cyclist> getFindCyclistError(Throwable error) {
        return Mono.error(new RuntimeException("Error getting cyclist from MongoDB" + error.getMessage()));
    }

    @Override
    public Mono<Cyclist> saveCyclist(String teamCode, Cyclist cyclist) {

        return mongoTemplate.findOne(Query.query(Criteria.where("teamCode").is(teamCode)), Team.class)
                .map(foundTeam -> {
                    foundTeam.getCyclists().add(cyclist);
                    return foundTeam;
                })
                .flatMap(mongoTemplate::save)
                .map(savedTeam -> cyclist)
                .onErrorResume(error -> Mono.error(new RuntimeException("An error occurred while saving the cyclist" +  error.getMessage())));
    }

    @Override
    public Mono<Cyclist> updateCyclistById(String cyclistId, Cyclist cyclist) {
        return null;
    }

    @Override
    public Mono<Void> deleteCyclistById(String cyclistId) {
        return null;
    }
}
