package co.com.api.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;


@Configuration
public class RouterRest {
@Bean
public RouterFunction<ServerResponse> routerFunctionCyclist(HandlerCyclist handlerCyclist) {
    return route(GET("/api/cyclist"), handlerCyclist::listenFindAllCyclistUseCase)
    .andRoute(POST("/api/cyclist/save"), handlerCyclist::listenPOSTUseCase).and(route(GET("/api/otherusercase/path"), handlerCyclist::listenGETOtherUseCase));

    }
}
