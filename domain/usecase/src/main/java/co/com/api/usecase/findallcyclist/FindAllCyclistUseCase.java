package co.com.api.usecase.findallcyclist;

import co.com.api.model.cyclist.Cyclist;
import co.com.api.model.cyclist.gateways.CyclistRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
public class FindAllCyclistUseCase {

    private final CyclistRepository cyclistRepository;

    public Flux<Cyclist> FindAllCyclist(){
        return cyclistRepository.findAllCyclist();
    }
}
