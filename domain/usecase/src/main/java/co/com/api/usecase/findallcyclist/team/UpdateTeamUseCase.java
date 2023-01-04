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
public class UpdateTeamUseCase {

    private final TeamRepository teamRepository;

    private final FindAllTeamUseCase findAllTeamUseCase;

    public Mono<Team> updateTeam(String teamId, Team newTeam){
        return teamRepository.findTeamById(teamId)
                .flatMap(oldTeam -> validateNewTeamFields(oldTeam, newTeam));
    }

    private Mono<Team> validateNewTeamFields(Team oldTeam, Team newTeam){
        return Mono.just(Arrays.stream(newTeam.teamFields())
                        .filter(this::getFieldsPredicate)
                        .collect(Collectors.toList()))
                .flatMap(validateTeamFieldsSize(oldTeam, newTeam));
    }

    private boolean getFieldsPredicate(String field) {
        return !isNull(field) && !field.equalsIgnoreCase("");
    }

    private Function<List<String>, Mono<Team>> validateTeamFieldsSize(Team oldTeam, Team newTeam) {
        return list -> (list.size() == newTeam.teamFields().length)
                ? validateNewTeamCyclistListSize(oldTeam, newTeam)
                : Mono.error(BusinessException.Type.INCOMPLETE_TEAM_INFORMATION.build(""));
    }

    private Mono<Team> validateNewTeamCyclistListSize(Team oldTeam, Team newTeam) {
        return Mono.just(newTeam)
                .flatMap(team -> team.getCyclists().size() > 8
                        ? Mono.error(BusinessException.Type.CYCLIST_LIST.build(""))
                        : validateNewTeamCyclistListElements(oldTeam, newTeam));
    }

    private Mono<Team> validateNewTeamCyclistListElements(Team oldTeam, Team newTeam) {
        return Mono.just(newTeam)
                .flatMap(team1 -> filterCyclistListFields(team1)
                        .findAny()
                        .isEmpty()
                        ? validateTeamCode(oldTeam, team1)
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

    private Mono<Team> validateTeamCode(Team oldTeam, Team newTeam) {
        return Mono.just(newTeam)
                .flatMap(team1 -> team1.getTeamCode().length() > 3
                        ? Mono.error(BusinessException.Type.TEAM_CODE_EXCEPTION.build(""))
                        : validateTeamFields(oldTeam, newTeam));
    }

    private Mono<Team> validateTeamFields(Team oldTeam, Team newTeam) {
        return Mono.just(newTeam)
                .flatMap(team1 -> team1.getCyclists()
                        .stream()
                        .allMatch(cyclist -> cyclist.getTeamCode().equalsIgnoreCase(newTeam.getTeamCode()))
                        ? validateTeamCyclistListCyclistNumber(oldTeam, newTeam)
                        : Mono.error(BusinessException.Type.CYCLIST_LIST_CYCLIST_DISTINCT_TEAM_NUMBER.build("")));
    }

    private Mono<Team> validateTeamCyclistListCyclistNumber(Team oldTeam, Team newTeam) {
        return Mono.just(newTeam)
                .flatMap(team1 -> validateDuplicateCyclistNumber(team1)
                        ? Mono.error(BusinessException.Type.DUPLICATE_CYCLIST_NUMBER.build(""))
                        : validateTeamCyclistListCyclistNumberSize(oldTeam, newTeam));
    }

    private Mono<Team> validateTeamCyclistListCyclistNumberSize(Team oldTeam, Team newTeam) {
        return Mono.just(newTeam)
                .flatMap(team1 -> team1.getCyclists()
                        .stream()
                        .noneMatch(cyclist -> cyclist.getCyclistNumber().length() > 3)
                        ? buildTeam(oldTeam, newTeam)
                        : Mono.error(BusinessException.Type.CYCLIST_WITH_CYCLIST_NUMBER_MAYOR_THAN_3.build("")));
    }

    private static boolean validateDuplicateCyclistNumber(Team team1) {
        return gropingByCyclistNumber(team1)
                .entrySet()
                .stream()
                .anyMatch(entry -> entry.getValue().size() > 1);
    }

    private static Map<String, List<Cyclist>> gropingByCyclistNumber(Team team1) {
        return team1.getCyclists()
                .stream()
                .collect(Collectors.groupingBy(Cyclist::getCyclistNumber));
    }

    private Mono<Team> buildTeam(Team oldTeam, Team newTeam) {
        return Mono.just(oldTeam)
                .flatMap(team -> {
                    Team team1 = Team.builder()
                            .teamName(newTeam.getTeamName())
                            .teamCode(newTeam.getTeamCode())
                            .country(newTeam.getCountry())
                            .cyclists(newTeam.getCyclists())
                            .build();
                   return teamRepository.updateTeam(oldTeam.getId(), newTeam);
                });
    }

}
