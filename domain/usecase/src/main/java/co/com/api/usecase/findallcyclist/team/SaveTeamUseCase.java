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
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Log
@RequiredArgsConstructor
public class SaveTeamUseCase {

    private final TeamRepository teamRepository;

    private final FindAllTeamUseCase findAllTeamUseCase;

    public Mono<Team> saveTeam(Team team){
        return Mono.just(team).flatMap(this::validateDuplicateTeam);
    }

    private Mono<Team> validateDuplicateTeam(Team team) {
        return findAllTeamUseCase
                .findAllTeams()
                .filter(team1 -> team1.getTeamCode().equalsIgnoreCase(team.getTeamCode()))
                .collectList()
                .flatMap(list -> (list.isEmpty())
                        ? validateTeamCode(team)
                        : Mono.error(BusinessException.Type.DUPLICATE_TEAM_NUMBER.build("")));
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
