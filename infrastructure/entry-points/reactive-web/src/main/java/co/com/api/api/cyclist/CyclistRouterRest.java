package co.com.api.api.cyclist;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;


@Configuration
public class CyclistRouterRest {
@Bean
public RouterFunction<ServerResponse> routerFunctionCyclist(CyclistHandler cyclistHandler) {
    return route(GET("/api/cyclist"), cyclistHandler::listenFindAllCyclistUseCase)
            .andRoute(GET("/api/cyclist/teamCode/{teamCode}"), cyclistHandler::listenFindAllCyclistByTeamCodeUseCase)
            .andRoute(GET("/api/cyclist/nationality/{nationality}"), cyclistHandler::listenFindAllCyclistByNationalityUseCase)
            .andRoute(GET("/api/cyclist/teamCode/{teamCode}/cyclistNumber/{cyclistNumber}"), cyclistHandler::listenFindCyclistsByCyclistNumberUseCase)
            .andRoute(POST("/api/cyclist/save/teamCode/{teamCode}"), cyclistHandler::listenPOSTUseCase)
            .andRoute(PUT("/api/cyclist/update/teamCode/{teamCode}/cyclistNumber/{cyclistNumber}"), cyclistHandler::listenUpdateCyclistUseCase)
            .and(route(DELETE("/api/cyclist/delete/teamCode/{teamCode}/cyclistNumber/{cyclistNumber}"), cyclistHandler::listenDeleteCyclistUseCase));
    }
}
