package co.com.api.api.team;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class TeamRouterRest {

    @Bean
    public RouterFunction<ServerResponse> routerFunctionTeam(TeamHandler teamHandler) {
        return route(GET("/api/team"), teamHandler::listenFindAllTeamsUseCase)
                .andRoute(GET("/api/team/{id}"), teamHandler::listenFindTeamByIdUseCase)
                .andRoute(GET("/api/team/country/{country}"), teamHandler::listenFindTeamByCountryUseCase)
                .andRoute(POST("/api/team/save"), teamHandler::listenSaveUseCase);
    }
}
