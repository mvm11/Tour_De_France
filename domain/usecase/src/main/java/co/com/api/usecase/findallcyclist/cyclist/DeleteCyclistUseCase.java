package co.com.api.usecase.findallcyclist.cyclist;

import co.com.api.model.common.ex.BusinessException;
import co.com.api.model.cyclist.gateways.CyclistRepository;
import co.com.api.model.team.Team;
import co.com.api.usecase.findallcyclist.team.FindAllTeamUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import reactor.core.publisher.Mono;

@Log
@RequiredArgsConstructor
public class DeleteCyclistUseCase {

    private final CyclistRepository cyclistRepository;

    private final FindAllTeamUseCase findAllTeamUseCase;

    public Mono<Void> deleteCyclist(String teamCode, String cyclistNumber){
        return findAllTeamUseCase.findAllTeams()
                .filter(team -> team.getTeamCode().equalsIgnoreCase(teamCode))
                .collectList()
                .flatMap(teams -> teams.isEmpty()
                        ? Mono.error(BusinessException.Type.TEAM_NOT_FOUND_BY_TEAM_CODE.build(teamCode))
                        : validateTeamCyclistListSize(teams.stream().findFirst().orElse(null), cyclistNumber));
    }

    private Mono<Void> validateTeamCyclistListSize(Team team, String cyclistNumber) {
        return !team.getCyclists().isEmpty()
                ? validateCyclistNumber(team, cyclistNumber)
                : Mono.error(BusinessException.Type.CANNOT_DELETE_CYCLIST.build(""));
    }

    private Mono<Void> validateCyclistNumber(Team team, String cyclistNumber) {
        return team.getCyclists().stream()
                .noneMatch(cyclist -> cyclist.getCyclistNumber().equalsIgnoreCase(cyclistNumber))
                ? Mono.error(BusinessException.Type.NO_FOUND_CYCLIST_CYCLIST_NUMBER.build(cyclistNumber))
                : cyclistRepository.deleteCyclist(team.getTeamCode(), cyclistNumber);
    }

}

