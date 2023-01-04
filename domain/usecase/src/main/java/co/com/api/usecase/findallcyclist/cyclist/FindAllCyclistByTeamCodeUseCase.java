package co.com.api.usecase.findallcyclist.cyclist;

import co.com.api.model.common.ex.BusinessException;
import co.com.api.model.cyclist.Cyclist;
import co.com.api.model.cyclist.gateways.CyclistRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import reactor.core.publisher.Mono;

import java.util.List;

@Log
@RequiredArgsConstructor
public class FindAllCyclistByTeamCodeUseCase {

    private final CyclistRepository cyclistRepository;

    public Mono<List<Cyclist>> findAllCyclistByTeamCode(String teamCode){
        return null;
    }
}
