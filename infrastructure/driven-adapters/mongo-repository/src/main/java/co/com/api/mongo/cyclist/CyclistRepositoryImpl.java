package co.com.api.mongo.cyclist;

import co.com.api.model.cyclist.Cyclist;
import co.com.api.model.cyclist.gateways.CyclistRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class CyclistRepositoryImpl implements CyclistRepository {

    private CyclistMongoRepositoryAdapter cyclistMongoRepositoryAdapter;

    private CyclistMongoDBRepository cyclistMongoDBRepository;

    @Override
    public Flux<Cyclist> findAllCyclist() {
        return cyclistMongoRepositoryAdapter.findAll();
    }

    @Override
    public Flux<Cyclist> findAllCyclistByNationality(String nationality) {
        return cyclistMongoRepositoryAdapter.doQueryMany(cyclistMongoDBRepository.findAllCyclistByNationality(nationality));
    }

    @Override
    public Flux<Cyclist> findAllCyclistByTeamCode(String teamCode) {
        return cyclistMongoRepositoryAdapter.doQueryMany(cyclistMongoDBRepository.findAllCyclistByTeamCode(teamCode));
    }

    @Override
    public Mono<Cyclist> findCyclistById(String cyclistId) {
        return cyclistMongoRepositoryAdapter.findById(cyclistId);
    }

    @Override
    public Mono<Cyclist> saveCyclist(Cyclist cyclist) {
        return cyclistMongoRepositoryAdapter.save(cyclist);
    }

    @Override
    public Mono<Cyclist> updateCyclistById(String cyclistId, Cyclist cyclist) {
        return null;
    }

    @Override
    public Mono<Void> deleteCyclistById(String cyclistId) {
        return cyclistMongoRepositoryAdapter.deleteById(cyclistId);
    }
}
