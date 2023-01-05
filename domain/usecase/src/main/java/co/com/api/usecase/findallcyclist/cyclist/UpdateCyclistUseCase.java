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
import java.util.stream.Stream;

import static java.util.Objects.isNull;

@Log
@RequiredArgsConstructor
public class UpdateCyclistUseCase {

    private final CyclistRepository cyclistRepository;
    private final FindAllTeamUseCase findAllTeamUseCase;

    public Mono<Cyclist> updateCyclist(String teamCode, String cyclistNumber, Cyclist cyclist){
        return findAllTeamUseCase.findAllTeams()
                .filter(team -> team.getTeamCode().equalsIgnoreCase(teamCode))
                .collectList()
                .flatMap(teams -> teams.isEmpty()
                        ? Mono.error(BusinessException.Type.TEAM_NOT_FOUND_BY_TEAM_CODE.build(teamCode))
                        : validateTeam(teams.stream().findFirst().orElse(null), cyclistNumber, cyclist));

    }

    private Mono<Cyclist> validateTeam(Team team, String cyclistNumber, Cyclist cyclist) {
        return isNull(team)
                ? Mono.error(BusinessException.Type.INCOMPLETE_TEAM_INFORMATION.build(""))
                : validateCyclistFields(team, cyclistNumber, cyclist);
    }

    private Mono<Cyclist> validateCyclistFields(Team team, String cyclistNumber, Cyclist cyclist) {
        return Arrays.stream(cyclist.cyclistFields())
                .anyMatch(Objects::isNull)
                || Arrays.stream(cyclist.cyclistFields())
                .anyMatch(field -> field.equalsIgnoreCase(""))
                ? Mono.error(BusinessException.Type.CYCLIST_WITH_EMPTY_FIELDS.build(""))
                : compareCyclistTeamCode(team, cyclistNumber, cyclist);
    }

    private Mono<Cyclist> compareCyclistTeamCode(Team team, String cyclistNumber, Cyclist cyclist) {
        return cyclist.getTeamCode().equalsIgnoreCase(team.getTeamCode())
                ? compareTeamCyclistsNumber(team, cyclistNumber, cyclist)
                : Mono.error(BusinessException.Type.CYCLIST_WITH_DIFFERENT_TEAM_CODE.build(""));
    }

    private Mono<Cyclist> compareTeamCyclistsNumber(Team team, String cyclistNumber, Cyclist cyclist) {
        return filterCyclistWithCyclistNumber(team, cyclistNumber)
                .findFirst()
                .isEmpty()
                ? Mono.error(BusinessException.Type.NO_FOUND_CYCLIST_CYCLIST_NUMBER.build(cyclistNumber))
                : validateCyclistNumber(team, cyclistNumber, cyclist);
    }

    private Stream<Cyclist> filterCyclistWithCyclistNumber(Team team, String cyclistNumber) {
        return team.getCyclists().stream()
                .filter(cyclist1 -> cyclist1.getCyclistNumber().equalsIgnoreCase(cyclistNumber));
    }

    private Mono<Cyclist> validateCyclistNumber(Team team, String cyclistNumber, Cyclist cyclist) {

        return team.getCyclists().stream()
                .noneMatch(cyclist1 -> cyclist1.getCyclistNumber().equalsIgnoreCase(cyclist.getCyclistNumber()))
                ? updateNewCyclist(team, cyclistNumber, cyclist)
                : Mono.error(BusinessException.Type.DUPLICATE_CYCLIST_UPDATED.build(cyclistNumber));

    }
    private Mono<Cyclist> updateNewCyclist(Team team, String cyclistNumber, Cyclist cyclist) {
        return cyclistRepository.updateCyclist(team.getTeamCode(), cyclistNumber, cyclist);
    }

}
