package co.com.api.usecase.findallcyclist.team;

import co.com.api.model.common.ex.BusinessException;
import co.com.api.model.cyclist.Cyclist;
import co.com.api.model.cyclist.gateways.CyclistRepository;
import co.com.api.model.team.Team;
import co.com.api.model.team.gateways.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Log
@RequiredArgsConstructor
public class FindAllTeamUseCase {

    private final TeamRepository teamRepository;

    public Flux<Team> findAllTeams(){
        return teamRepository
                .findAllTeams()
                .onErrorResume(error -> {
                    log.info("No teams have been found");
                    return Mono.empty();
                });
    }
}
