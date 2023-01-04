package co.com.api.usecase.findallcyclist.cyclist;

import co.com.api.model.cyclist.Cyclist;
import co.com.api.model.cyclist.gateways.CyclistRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import reactor.core.publisher.Flux;

@Log
@RequiredArgsConstructor
public class FindAllCyclistUseCase {

    private final CyclistRepository cyclistRepository;

    public Flux<Cyclist> findAllCyclist(){
        return cyclistRepository
                .findAllCyclist();
    }

}
