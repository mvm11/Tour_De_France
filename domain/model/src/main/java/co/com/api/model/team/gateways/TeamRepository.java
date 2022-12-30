package co.com.api.model.team.gateways;

import co.com.api.model.team.Team;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TeamRepository {

    Flux<Team> findAllTeams();
    Flux<Team> findAllTeamsByCountry(String country);
    Mono<Team> saveTeam(Team team);
    Mono<Team> updateTeamById(String teamId, Team team);
    Mono<Void> deleteTeamById(String teamId);
}
