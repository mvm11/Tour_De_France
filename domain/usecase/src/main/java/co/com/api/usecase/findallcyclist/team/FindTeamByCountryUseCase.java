package co.com.api.usecase.findallcyclist.team;

import co.com.api.model.team.Team;
import co.com.api.model.team.gateways.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import reactor.core.publisher.Flux;

@Log
@RequiredArgsConstructor
public class FindTeamByCountryUseCase {

    private final TeamRepository teamRepository;

    private final FindAllTeamUseCase findAllTeamUseCase;

    public Flux<Team> findAllTeamsByCountry(String country){
        return teamRepository.findTeamByCountry(country);
    }

}
