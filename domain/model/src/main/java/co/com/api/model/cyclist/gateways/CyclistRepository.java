package co.com.api.model.cyclist.gateways;

import co.com.api.model.cyclist.Cyclist;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CyclistRepository {

    Flux<Cyclist> findAllCyclist();
    Flux<Cyclist> findAllCyclistByNationality(String nationality);
    Flux<Cyclist> findAllCyclistByTeamCode(String teamCode);
    Mono<Cyclist> findCyclistById(String cyclistId);
    Mono<Cyclist> saveCyclist(Cyclist cyclist);
    Mono<Cyclist> updateCyclistById(String cyclistId, Cyclist cyclist);
    Mono<Void> deleteCyclistById(String cyclistId);



}
