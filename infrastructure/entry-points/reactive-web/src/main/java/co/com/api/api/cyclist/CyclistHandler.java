package co.com.api.api.cyclist;

import co.com.api.model.common.ex.BusinessException;
import co.com.api.model.cyclist.Cyclist;
import co.com.api.model.team.Team;
import co.com.api.usecase.findallcyclist.cyclist.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CyclistHandler {

    private final FindAllCyclistsUseCase findAllCyclistsUseCase;
    private final FindAllCyclistsByTeamCodeUseCase findAllCyclistsByTeamCodeUseCase;
    private final FindAllCyclistsByNationalityUseCase findAllCyclistsByNationalityUseCase;
    private final FindCyclistsByCyclistNumberUseCase findCyclistsByCyclistNumberUseCase;
    private final SaveCyclistUseCase saveCyclistUseCase;
    private final UpdateCyclistUseCase updateCyclistUseCase;

    private final DeleteCyclistUseCase deleteCyclistUseCase;
    public Mono<ServerResponse> listenFindAllCyclistUseCase(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(findAllCyclistsUseCase.findAllCyclist(), Cyclist.class);
    }

    public Mono<ServerResponse> listenFindAllCyclistByTeamCodeUseCase(ServerRequest serverRequest) {
        String teamCode = serverRequest.pathVariable("teamCode");
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(findAllCyclistsByTeamCodeUseCase.findAllCyclistByTeamCode(teamCode), Cyclist.class);
    }

    public Mono<ServerResponse> listenFindAllCyclistByNationalityUseCase(ServerRequest serverRequest) {
        String nationality = serverRequest.pathVariable("nationality");
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(findAllCyclistsByNationalityUseCase.findAllCyclistByNationality(nationality), Cyclist.class);
    }

    public Mono<ServerResponse> listenFindCyclistsByCyclistNumberUseCase(ServerRequest serverRequest) {
        String teamCode = serverRequest.pathVariable("teamCode");
        String cyclistNumber = serverRequest.pathVariable("cyclistNumber");
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(findCyclistsByCyclistNumberUseCase.findCyclistsByCyclistNumber(teamCode, cyclistNumber), Cyclist.class);
    }

    public Mono<ServerResponse> listenPOSTUseCase(ServerRequest serverRequest) {
            String teamCode = serverRequest.pathVariable("teamCode");
            return serverRequest.bodyToMono(Cyclist.class)
                    .flatMap(cyclist -> ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(saveCyclistUseCase.saveCyclist(teamCode, cyclist), Cyclist.class))
                    .onErrorResume(this::handleError);
    }

    public Mono<ServerResponse> listenUpdateCyclistUseCase(ServerRequest serverRequest) {
        String teamCode = serverRequest.pathVariable("teamCode");
        String cyclistNumber = serverRequest.pathVariable("cyclistNumber");
        return serverRequest.bodyToMono(Cyclist.class)
                .flatMap(cyclist -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(updateCyclistUseCase.updateCyclist(teamCode, cyclistNumber, cyclist), Cyclist.class))
                .onErrorResume(this::handleError);
    }

    public Mono<ServerResponse> listenDeleteCyclistUseCase(ServerRequest serverRequest) {
        String teamCode = serverRequest.pathVariable("teamCode");
        String cyclistNumber = serverRequest.pathVariable("cyclistNumber");

        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(deleteCyclistUseCase.deleteCyclist(teamCode, cyclistNumber), Team.class);
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
