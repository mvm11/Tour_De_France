package co.com.api.usecase.findallcyclist.cyclist;

import co.com.api.model.cyclist.Cyclist;
import co.com.api.model.cyclist.gateways.CyclistRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import reactor.core.publisher.Mono;

@Log
@RequiredArgsConstructor
public class FindCyclistsByCyclistNumberUseCase {

    private final CyclistRepository cyclistRepository;

    public Mono<Cyclist> findCyclistsByCyclistNumber(String teamCode, String cyclistNumber){
        return cyclistRepository.findCyclistByCyclistNumber(teamCode,cyclistNumber);
    }

}
