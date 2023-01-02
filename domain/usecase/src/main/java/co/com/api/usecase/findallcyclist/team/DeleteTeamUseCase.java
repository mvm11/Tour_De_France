package co.com.api.usecase.findallcyclist.team;

import co.com.api.model.team.gateways.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import reactor.core.publisher.Mono;

@Log
@RequiredArgsConstructor
public class DeleteTeamUseCase {

    private final TeamRepository teamRepository;

    public Mono<Void> deleteTeam(String teamId){
        return teamRepository.deleteTeamById(teamId);
    }
}
