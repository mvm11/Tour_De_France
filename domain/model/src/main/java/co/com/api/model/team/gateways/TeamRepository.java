package co.com.api.model.team.gateways;

import co.com.api.model.team.Team;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TeamRepository {

    Flux<Team> findAllTeams();
    Mono<Team> findTeamById(String teamId);
    Flux<Team> findTeamByCountry(String country);
    Mono<Team> saveTeam(Team team);
    Mono<Team> updateTeamById(String teamId, Team team);
    Mono<Void> deleteTeamById(String teamId);
}
