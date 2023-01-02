package co.com.api.api.cyclist;

import co.com.api.model.common.ex.BusinessException;
import co.com.api.model.cyclist.Cyclist;
import co.com.api.usecase.findallcyclist.cyclist.FindAllCyclistByTeamCodeUseCase;
import co.com.api.usecase.findallcyclist.cyclist.FindAllCyclistUseCase;
import co.com.api.usecase.findallcyclist.cyclist.SaveCyclistUseCase;
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

    private final FindAllCyclistUseCase findAllCyclistUseCase;

    private final FindAllCyclistByTeamCodeUseCase findAllCyclistByTeamCodeUseCase;
    private final SaveCyclistUseCase saveCyclistUseCase;
    public Mono<ServerResponse> listenFindAllCyclistUseCase(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(findAllCyclistUseCase.findAllCyclist(), Cyclist.class);
    }

    public Mono<ServerResponse> listenFindAllCyclistByTeamCodeUseCase(ServerRequest serverRequest) {
        String teamCode = serverRequest.pathVariable("teamCode");
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(findAllCyclistByTeamCodeUseCase.findAllCyclistByTeamCode(teamCode), Cyclist.class);
    }

    public Mono<ServerResponse> listenPOSTUseCase(ServerRequest serverRequest) {

            return serverRequest.bodyToMono(Cyclist.class)
                    .flatMap(cyclist -> ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(saveCyclistUseCase.saveCyclist(cyclist), Cyclist.class))
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
