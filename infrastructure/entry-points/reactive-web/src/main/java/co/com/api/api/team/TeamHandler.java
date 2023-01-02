package co.com.api.api.team;

import co.com.api.model.common.ex.BusinessException;
import co.com.api.model.cyclist.Cyclist;
import co.com.api.model.team.Team;
import co.com.api.usecase.findallcyclist.team.FindAllTeamUseCase;
import co.com.api.usecase.findallcyclist.team.SaveTeamUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class TeamHandler {

    private final FindAllTeamUseCase findAllTeamUseCase;
    private final SaveTeamUseCase saveTeamUseCase;

    public Mono<ServerResponse> listenFindAllTeamsUseCase(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(findAllTeamUseCase.findAllTeams(), Cyclist.class);
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
}
