package co.com.api.usecase.findallcyclist.team;

import co.com.api.model.team.Team;
import co.com.api.model.team.gateways.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import reactor.core.publisher.Mono;

@Log
@RequiredArgsConstructor
public class FindTeamByIdUseCase {

    private final TeamRepository teamRepository;

    public Mono<Team> findTeamById(String teamId){
        return teamRepository.findTeamById(teamId);
    }

}
