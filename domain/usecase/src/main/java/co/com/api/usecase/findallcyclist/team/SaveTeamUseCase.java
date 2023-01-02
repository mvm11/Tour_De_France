package co.com.api.usecase.findallcyclist.team;

import co.com.api.model.common.ex.BusinessException;
import co.com.api.model.cyclist.Cyclist;
import co.com.api.model.team.Team;
import co.com.api.model.team.gateways.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static java.util.Objects.isNull;

@Log
@RequiredArgsConstructor
public class SaveTeamUseCase {

    private final TeamRepository teamRepository;

    private final FindAllTeamUseCase findAllTeamUseCase;

    public Mono<Team> saveTeam(Team team){
        return Mono.just(team).flatMap(this::validateDuplicateTeamCode);
    }

    private Mono<Team> validateDuplicateTeamCode(Team team) {
        return findAllTeamUseCase
                .findAllTeams()
                .filter(team1 -> team1.getTeamCode().equalsIgnoreCase(team.getTeamCode()))
                .collectList()
                .flatMap(list -> (list.isEmpty())
                        ? validateDuplicateTeamName(team)
                        : Mono.error(BusinessException.Type.DUPLICATE_TEAM_NUMBER.build("")));
    }

    private Mono<Team> validateDuplicateTeamName(Team team) {
        return findAllTeamUseCase
                .findAllTeams()
                .filter(team1 -> team1.getTeamName().equalsIgnoreCase(team.getTeamName()))
                .collectList()
                .flatMap(list -> (list.isEmpty())
                        ? validateTeamCyclistList(team)
                        : Mono.error(BusinessException.Type.DUPLICATE_TEAM_NAME.build("")));
    }

    private Mono<Team> validateTeamCyclistList(Team team) {
        return Mono.just(team)
                .flatMap(team1 -> team1.getCyclists().size() > 8
                        ? Mono.error(BusinessException.Type.CYCLIST_LIST.build(""))
                        : validateTeamCyclistCode(team1));
    }

    private Mono<Team> validateTeamCyclistCode(Team team) {
        return Mono.just(team)
                .flatMap(team1 -> compareTeamCyclistListSize(team, team1)
                        ? Mono.error(BusinessException.Type.CYCLIST_LIST_CYCLIST_NUMBER_DUPLICATE.build(""))
                        : validateTeamCyclist(team1));
    }

    private Mono<Team> validateTeamCyclist(Team team) {
        return Mono.just(team)
                .flatMap(team1 -> team1.getCyclists()
                        .stream()
                        .filter(cyclist -> cyclist.getTeamCode().equalsIgnoreCase(team.getTeamCode()))
                        .count() != team.getCyclists().size()
                        ? Mono.error(BusinessException.Type.CYCLIST_LIST_CYCLIST_DISTINCT_TEAM_NUMBER.build(""))
                        : validateTeamCyclistFields(team1));
    }
    private boolean compareTeamCyclistListSize(Team team, Team team1) {
        return getLongStream(team1)
                .sum() != team.getCyclists().size();
    }

    private LongStream getLongStream(Team team1) {
        return getStringListMap(team1)
                .values()
                .stream()
                .filter(getCyclistWithSameNumberPredicate())
                .mapToLong(Collection::size);
    }

    private Map<String, List<Cyclist>> getStringListMap(Team team1) {
        return getCyclistStream(team1)
                .collect(Collectors.groupingBy(Cyclist::getCyclistNumber));
    }

    private Stream<Cyclist> getCyclistStream(Team team1) {
        return team1.getCyclists()
                .stream();
    }

    private Predicate<List<Cyclist>> getCyclistWithSameNumberPredicate() {
        return cyclistWithSameNumber -> cyclistWithSameNumber.size() == 1;
    }

    private Mono<Team> validateTeamCyclistFields(Team team) {
        return Mono.just(team)
                .flatMap(team1 -> filterCyclistListFields(team1)
                        .findAny()
                        .isEmpty()
                        ? validateTeamCode(team1)
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

    private Mono<Team> validateTeamCode(Team team) {
        return Mono.just(team)
                .flatMap(team1 -> team1.getTeamCode().length() > 3
                        ? Mono.error(BusinessException.Type.TEAM_CODE_EXCEPTION.build(""))
                        : validateTeamFields(team1));
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
                ? teamRepository.saveTeam(team)
                : Mono.error(BusinessException.Type.INCOMPLETE_TEAM_INFORMATION.build(""));
    }

}
