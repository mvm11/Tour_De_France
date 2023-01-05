package co.com.api.usecase.findallcyclist.team;

import co.com.api.model.common.ex.BusinessException;
import co.com.api.model.cyclist.Cyclist;
import co.com.api.model.team.Team;
import co.com.api.model.team.gateways.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.isNull;

@Log
@RequiredArgsConstructor
public class SaveTeamUseCase {

    private final TeamRepository teamRepository;

    private final FindAllTeamUseCase findAllTeamUseCase;

    public Mono<Team> saveTeam(Team team){
        return Mono.just(team).flatMap(this::validateTeamFields);
    }

    private Mono<Team> validateTeamFields(Team team) {
        return Mono.just(Arrays.stream(team.teamFields())
                        .filter(this::getFieldsPredicate)
                        .collect(Collectors.toList()))
                .flatMap(validateTeamFieldsSize(team));
    }

    private boolean getFieldsPredicate(String field) {
        return !isNull(field) && !field.equalsIgnoreCase("");
    }

    private Function<List<String>, Mono<Team>> validateTeamFieldsSize(Team team) {
        return list -> (list.size() == team.teamFields().length)
                ? validateTeamCyclistFields(team)
                : Mono.error(BusinessException.Type.INCOMPLETE_TEAM_INFORMATION.build(""));
    }

    private Mono<Team> validateTeamCyclistFields(Team team) {
        return Mono.just(team)
                .flatMap(team1 -> filterCyclistListFields(team1)
                        .findAny()
                        .isEmpty()
                        ? validateTeamCyclistList(team)
                        : Mono.error(BusinessException.Type.CYCLIST_LIST_WITH_EMPTY_FIELDS.build("")));
    }

    private Stream<Cyclist> filterCyclistListFields(Team team1) {
        return team1.getCyclists()
                .stream()
                .filter(this::getCyclistListFieldsPredicate);
    }

    private boolean getCyclistListFieldsPredicate(Cyclist cyclist) {
        return Arrays.stream(cyclist.cyclistFields())
                .anyMatch(Objects::isNull)
                || Arrays.stream(cyclist.cyclistFields())
                .anyMatch(field -> field.equalsIgnoreCase(""));
    }

    private Mono<Team> validateTeamCyclistList(Team team) {
        return Mono.just(team)
                .flatMap(team1 -> team1.getCyclists().size() > 8
                        ? Mono.error(BusinessException.Type.CYCLIST_LIST.build(""))
                        : validateTeamCode(team1));
    }
    private Mono<Team> validateTeamCode(Team team) {
        return Mono.just(team)
                .flatMap(team1 -> team1.getTeamCode().length() > 3
                        ? Mono.error(BusinessException.Type.TEAM_CODE_EXCEPTION.build(""))
                        : compareTeamCodeWithCyclistList(team1));
    }

    private Mono<Team> compareTeamCodeWithCyclistList(Team team) {
        return Mono.just(team)
                .flatMap(team1 -> team1.getCyclists()
                        .stream()
                        .allMatch(cyclist -> cyclist.getTeamCode().equalsIgnoreCase(team.getTeamCode()))
                        ? validateTeamCyclistListCyclistNumber(team)
                        : Mono.error(BusinessException.Type.CYCLIST_LIST_CYCLIST_DISTINCT_TEAM_NUMBER.build("")));
    }

    private Mono<Team> validateTeamCyclistListCyclistNumber(Team team) {
        return Mono.just(team)
                .flatMap(team1 -> validateDuplicateCyclistNumber(team1)
                        ? Mono.error(BusinessException.Type.DUPLICATE_CYCLIST_NUMBER.build(""))
                        : validateTeamCyclistListCyclistNumberSize(team));
    }

    private boolean validateDuplicateCyclistNumber(Team team1) {
        return gropingByCyclistNumber(team1)
                .entrySet()
                .stream()
                .anyMatch(entry -> entry.getValue().size() > 1);
    }

    private Map<String, List<Cyclist>> gropingByCyclistNumber(Team team1) {
        return team1.getCyclists()
                .stream()
                .collect(Collectors.groupingBy(Cyclist::getCyclistNumber));
    }

    private Mono<Team> validateTeamCyclistListCyclistNumberSize(Team team) {
        return Mono.just(team)
                .flatMap(team1 -> team1.getCyclists()
                        .stream()
                        .noneMatch(cyclist -> cyclist.getCyclistNumber().length() > 3)
                        ? validateTeamWithSameTeamNumber(team)
                        : Mono.error(BusinessException.Type.CYCLIST_WITH_CYCLIST_NUMBER_MAYOR_THAN_3.build("")));
    }

    private Mono<Team> validateTeamWithSameTeamNumber(Team team) {
        return findAllTeamUseCase.findAllTeams()
                .collectList()
                .flatMap(teams -> teams.isEmpty() ? saveNewTeam(team) : validateDuplicateTeamNumber(team));
    }

    private Mono<Team> saveNewTeam(Team team){
        return teamRepository.saveTeam(team);
    }

    private Mono<Team> validateDuplicateTeamNumber(Team team) {
        return findAllTeamUseCase.findAllTeams()
                .collectList()
                .flatMap(teams -> teams.stream()
                        .noneMatch(team1 -> team1.getTeamCode().equalsIgnoreCase(team.getTeamCode()))
                        ? saveNewTeam(team)
                        : Mono.error(BusinessException.Type.DUPLICATE_TEAM_NUMBER.build("")));
    }



}
