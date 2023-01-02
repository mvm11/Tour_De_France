package co.com.api.api.team;

import co.com.api.model.common.ex.BusinessException;
import co.com.api.model.common.ex.NotFoundException;
import co.com.api.model.cyclist.Cyclist;
import co.com.api.model.team.Team;
import co.com.api.usecase.findallcyclist.team.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class TeamHandler {

    private final FindAllTeamUseCase findAllTeamUseCase;
    private final FindTeamByIdUseCase findTeamByIdUseCase;
    private final FindTeamByCountryUseCase findTeamByCountryUseCase;
    private final SaveTeamUseCase saveTeamUseCase;

    private final DeleteTeamUseCase deleteTeamUseCase;

    public Mono<ServerResponse> listenFindAllTeamsUseCase(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(findAllTeamUseCase.findAllTeams(), Cyclist.class);
    }

    public Mono<ServerResponse> listenFindTeamByIdUseCase(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(findTeamByIdUseCase.findTeamById(id), Cyclist.class);
    }

    public Mono<ServerResponse> listenFindTeamByCountryUseCase(ServerRequest serverRequest) {
        String country = serverRequest.pathVariable("country");
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(findTeamByCountryUseCase.findAllTeamsByCountry(country), Cyclist.class)
                .onErrorResume(this::handleErrorListenFindTeamByCountryUseCase);
    }

    private Mono<ServerResponse> handleErrorListenFindTeamByCountryUseCase(Throwable error) {
        if(error instanceof NotFoundException){
            return ServerResponse.status(HttpStatus.NO_CONTENT)
                    .body(Mono.just(error.getMessage()), String.class);
        }
        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Mono.just(error.getMessage()), String.class);
    }

    public Mono<ServerResponse> listenSaveUseCase(ServerRequest serverRequest) {

        return serverRequest.bodyToMono(Team.class)
                .flatMap(team -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(saveTeamUseCase.saveTeam(team), Team.class))
                .onErrorResume(this::handleError);
    }

    private Mono<ServerResponse> handleError(Throwable error) {
        if(error instanceof BusinessException){
            return ServerResponse.status(HttpStatus.BAD_REQUEST)
                    .body(Mono.just(error.getMessage()), String.class);
        }
        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Mono.just(error.getMessage()), String.class);
    }
    public Mono<ServerResponse> listenDeleteTeamUseCase(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(deleteTeamUseCase.deleteTeam(id), Team.class);
    }
}
