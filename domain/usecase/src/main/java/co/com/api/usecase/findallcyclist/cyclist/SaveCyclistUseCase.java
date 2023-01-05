package co.com.api.usecase.findallcyclist.cyclist;

import co.com.api.model.common.ex.BusinessException;
import co.com.api.model.cyclist.Cyclist;
import co.com.api.model.cyclist.gateways.CyclistRepository;
import co.com.api.model.team.Team;
import co.com.api.usecase.findallcyclist.team.FindAllTeamUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Objects;

import static java.util.Objects.isNull;

@Log
@RequiredArgsConstructor
public class SaveCyclistUseCase {

    private final CyclistRepository cyclistRepository;
    private final FindAllTeamUseCase findAllTeamUseCase;

    public Mono<Cyclist> saveCyclist(String teamCode, Cyclist cyclist){
        return findAllTeamUseCase.findAllTeams()
                .filter(team -> team.getTeamCode().equalsIgnoreCase(teamCode))
                .collectList()
                .flatMap(teams -> teams.isEmpty()
                        ? Mono.error(BusinessException.Type.TEAM_NOT_FOUND_BY_TEAM_CODE.build(teamCode))
                        : validateTeam(teams.stream().findFirst().orElse(null), cyclist));

    }

    private Mono<Cyclist> validateTeam(Team team, Cyclist cyclist) {
        return isNull(team)
                ? Mono.error(BusinessException.Type.INCOMPLETE_TEAM_INFORMATION.build(""))
                : validateTeamCyclistListSize(team, cyclist);
    }

    private Mono<Cyclist> validateTeamCyclistListSize(Team team, Cyclist cyclist) {
        return team.getCyclists().size() >= 8
                ? Mono.error(BusinessException.Type.TEAM_MAX_CYCLISTS.build(""))
                : validateCyclistFields(team, cyclist);
    }

    private Mono<Cyclist> validateCyclistFields(Team team, Cyclist cyclist) {
        return Arrays.stream(cyclist.cyclistFields())
                .anyMatch(Objects::isNull)
                || Arrays.stream(cyclist.cyclistFields())
                .anyMatch(field -> field.equalsIgnoreCase(""))
                ? Mono.error(BusinessException.Type.CYCLIST_WITH_EMPTY_FIELDS.build(""))
                : compareCyclistTeamCode(team, cyclist);
    }

    private Mono<Cyclist> compareCyclistTeamCode(Team team, Cyclist cyclist) {
        return cyclist.getTeamCode().equalsIgnoreCase(team.getTeamCode())
                ? compareTeamCyclistsNumber(team, cyclist)
                : Mono.error(BusinessException.Type.CYCLIST_WITH_DIFFERENT_TEAM_CODE.build(""));
    }

    private Mono<Cyclist> compareTeamCyclistsNumber(Team team, Cyclist cyclist) {
        return team.getCyclists().stream()
                .noneMatch(cyclist1 -> cyclist1.getCyclistNumber().equalsIgnoreCase(cyclist.getCyclistNumber()))
                ? saveNewCyclist(team, cyclist)
                : Mono.error(BusinessException.Type.CYCLIST_WITH_SAME_CYCLIST_NUMBER.build(""));
    }

    private Mono<Cyclist> saveNewCyclist(Team team, Cyclist cyclist) {
        return cyclistRepository.saveCyclist(team.getTeamCode(), cyclist);
    }

}
