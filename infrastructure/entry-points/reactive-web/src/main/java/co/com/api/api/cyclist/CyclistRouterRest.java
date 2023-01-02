package co.com.api.api.cyclist;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;


@Configuration
public class CyclistRouterRest {
@Bean
public RouterFunction<ServerResponse> routerFunctionCyclist(CyclistHandler cyclistHandler) {
    return route(GET("/api/cyclist"), cyclistHandler::listenFindAllCyclistUseCase)
            .andRoute(GET("/api/cyclist/{teamCode}"), cyclistHandler::listenFindAllCyclistByTeamCodeUseCase)
    .andRoute(POST("/api/cyclist/save"), cyclistHandler::listenPOSTUseCase).and(route(GET("/api/otherusercase/path"), cyclistHandler::listenFindAllCyclistByTeamCodeUseCase));

    }
}
