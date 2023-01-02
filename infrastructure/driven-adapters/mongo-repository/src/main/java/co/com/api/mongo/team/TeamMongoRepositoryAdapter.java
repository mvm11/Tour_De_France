package co.com.api.mongo.team;


import co.com.api.model.common.ex.BusinessException;
import co.com.api.model.common.ex.NotFoundException;
import co.com.api.model.team.Team;
import co.com.api.model.team.gateways.TeamRepository;
import co.com.api.mongo.helper.AdapterOperations;
import com.mongodb.DuplicateKeyException;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
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
                .switchIfEmpty(Mono.error(BusinessException.Type.TEAMS_NOT_FOUND.build("")))
                .map(this::convertToTeam);
    }

    @Override
    public Mono<Team> findTeamById(String teamId) {
        return repository.findById(teamId)
                .onErrorResume(error -> Mono.error(new RuntimeException("Error getting team from MongoDB" + error.getMessage())))
                .switchIfEmpty(Mono.error(BusinessException.Type.TEAM_NOT_FOUND.build("")))
                .map(this::convertToTeam);
    }

    @Override
    public Flux<Team> findTeamByCountry(String country) {
        return repository.findAll()
                .filter(teamDocument -> teamDocument.getCountry().equalsIgnoreCase(country))
                .map(this::convertToTeam)
                .onErrorResume(error -> Mono.error(new RuntimeException("Error getting teams from MongoDB" + error.getMessage())))
                .switchIfEmpty(Mono.error(new NotFoundException(NotFoundException.Type.TEAMS_NOT_FOUND_BY_COUNTRY, country)));
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
        return mongoTemplate.save(team)
                .onErrorResume(error -> error instanceof DuplicateKeyException ?
                        Mono.error(new BusinessException(BusinessException.Type.ERROR_TEAM, "An error occurred while creating the team due to ID duplicates: " + error.getMessage()))
                        : Mono.error(new RuntimeException("An error occurred while saving the team" +  error.getMessage())));
    }

    @Override
    public Mono<Team> updateTeamById(String teamId, Team team) {
        return null;
    }

    @Override
    public Mono<Void> deleteTeamById(String teamId) {
        return null;
    }
}
