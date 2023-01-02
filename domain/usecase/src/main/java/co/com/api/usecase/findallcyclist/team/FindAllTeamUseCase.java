package co.com.api.usecase.findallcyclist.team;

import co.com.api.model.team.Team;
import co.com.api.model.team.gateways.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import reactor.core.publisher.Flux;

@Log
@RequiredArgsConstructor
public class FindAllTeamUseCase {

    private final TeamRepository teamRepository;

    public Flux<Team> findAllTeams(){
        return teamRepository.findAllTeams();
    }
}
