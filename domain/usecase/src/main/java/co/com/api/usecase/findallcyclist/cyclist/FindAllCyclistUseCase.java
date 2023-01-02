package co.com.api.usecase.findallcyclist.cyclist;

import co.com.api.model.cyclist.Cyclist;
import co.com.api.model.cyclist.gateways.CyclistRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log
@RequiredArgsConstructor
public class FindAllCyclistUseCase {

    private final CyclistRepository cyclistRepository;

    public Flux<Cyclist> findAllCyclist(){
        return cyclistRepository
                .findAllCyclist()
                .onErrorResume(error -> {
                    log.info("No cyclists have been found");
                    return Mono.empty();
                });
    }

}
