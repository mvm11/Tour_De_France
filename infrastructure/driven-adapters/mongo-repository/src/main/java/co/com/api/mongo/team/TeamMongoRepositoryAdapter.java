package co.com.api.mongo.team;


import co.com.api.model.team.Team;
import co.com.api.model.team.gateways.TeamRepository;
import co.com.api.mongo.helper.AdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class TeamMongoRepositoryAdapter extends AdapterOperations<Team, TeamDocument, String, TeamMongoDBRepository>
        implements TeamRepository {

    private final ReactiveMongoTemplate mongoTemplate;


    public TeamMongoRepositoryAdapter(TeamMongoDBRepository repository, ObjectMapper mapper, ReactiveMongoTemplate mongoTemplate) {
        super(repository, mapper, d -> mapper.map(d, Team.class));
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Flux<Team> findAllTeams() {
        return repository.findAll()
                .onErrorResume(error -> Mono.error(new RuntimeException("Error getting all teams from MongoDB" + error.getMessage())))
                .map(this::convertToTeam);
    }

    @Override
    public Mono<Team> findTeamById(String teamId) {
        return repository.findById(teamId)
                .onErrorResume(error -> Mono.error(new RuntimeException("Error getting team from MongoDB" + error.getMessage())))
                .map(this::convertToTeam);
    }

    @Override
    public Flux<Team> findTeamByCountry(String country) {
        return repository.findAll()
                .filter(teamDocument -> teamDocument.getCountry().equalsIgnoreCase(country))
                .map(this::convertToTeam)
                .onErrorResume(error -> Mono.error(new RuntimeException("Error getting teams from MongoDB" + error.getMessage())));
    }

    private Team convertToTeam(TeamDocument teamDocument) {
        return Team.builder()
                .id(teamDocument.getId())
                .teamName(teamDocument.getTeamName())
                .teamCode(teamDocument.getTeamCode())
                .country(teamDocument.getCountry())
                .cyclists(teamDocument.getCyclists())
                .build();
    }

    @Override
    public Mono<Team> saveTeam(Team team) {
        return mongoTemplate.insert(team)
                .onErrorResume(error -> Mono.error(new RuntimeException("An error occurred while saving the team" +  error.getMessage())));
    }

    @Override
    public Mono<Team> updateTeam(String teamId, Team team) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id")
                .is(teamId));

        Update update = new Update();
        update.set("teamName", team.getTeamName());
        update.set("teamCode", team.getTeamCode());
        update.set("country", team.getCountry());
        update.set("cyclists", team.getCyclists());

        return mongoTemplate.findAndModify(query,
                update, FindAndModifyOptions.options().returnNew(true), Team.class)
                        .onErrorResume(error -> Mono.error(new RuntimeException("An error occurred while updating the team" +  error.getMessage())));
    }

    @Override
    public Mono<Void> deleteTeamById(String teamId) {
        return repository.deleteById(teamId)
                .onErrorResume(error -> Mono.error(new RuntimeException("Error getting team from MongoDB" + error.getMessage())));
    }
}
