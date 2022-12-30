package co.com.api.usecase.findallcyclist.cyclist;

import co.com.api.model.cyclist.Cyclist;
import co.com.api.model.cyclist.gateways.CyclistRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CyclistUseCase {

    private final CyclistRepository cyclistRepository;

    public Flux<Cyclist> findAllCyclist(){
        return cyclistRepository.findAllCyclist();
    }

    Mono<Cyclist> findCyclistById(String cyclistId){
        return cyclistRepository.findCyclistById(cyclistId);
    }

    public Flux<Cyclist> findAllCyclistByNationality(String nationality) {
        return cyclistRepository.findAllCyclistByNationality(nationality);
    }

    public Flux<Cyclist> findAllCyclistByTeamCode(String teamCode) {
        return cyclistRepository.findAllCyclistByTeamCode(teamCode);
    }

    public Mono<Cyclist> saveCyclist(Cyclist cyclist) {

        return cyclistRepository.saveCyclist(cyclist);
    }

    public Mono<Cyclist> updateCyclistById(String cyclistId, Cyclist cyclist) {
        return null;
    }

    public Mono<Void> deleteCyclistById(String cyclistId) {
        return cyclistRepository.deleteCyclistById(cyclistId);
    }
}
