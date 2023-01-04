package co.com.api.model.cyclist.gateways;

import co.com.api.model.cyclist.Cyclist;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CyclistRepository {

    Flux<Cyclist> findAllCyclist();
    Flux<Cyclist> findAllCyclistByTeamCode(String teamCode);
    Flux<Cyclist> findAllCyclistByNationality(String nationality);
    Mono<Cyclist> findCyclistByCyclistNumber(String teamCode, String cyclistNumber);
    Mono<Cyclist> saveCyclist(String teamCode, Cyclist cyclist);
    Mono<Cyclist> updateCyclistById(String cyclistId, Cyclist cyclist);
    Mono<Void> deleteCyclistById(String cyclistId);



}
