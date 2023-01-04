package co.com.api.usecase.findallcyclist.cyclist;

import co.com.api.model.cyclist.Cyclist;
import co.com.api.model.cyclist.gateways.CyclistRepository;
import co.com.api.model.team.gateways.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import reactor.core.publisher.Flux;

@Log
@RequiredArgsConstructor
public class FindAllCyclistByTeamCodeUseCase {

    private final TeamRepository teamRepository;

    private final CyclistRepository cyclistRepository;

    public Flux<Cyclist> findAllCyclistByTeamCode(String teamCode){
        return cyclistRepository.findAllCyclistByTeamCode(teamCode);
    }
}
