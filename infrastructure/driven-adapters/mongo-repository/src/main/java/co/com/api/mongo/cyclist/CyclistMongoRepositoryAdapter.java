package co.com.api.mongo.cyclist;

import co.com.api.model.common.ex.BusinessException;
import co.com.api.model.cyclist.Cyclist;
import co.com.api.model.cyclist.gateways.CyclistRepository;
import co.com.api.mongo.helper.AdapterOperations;
import com.mongodb.DuplicateKeyException;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class CyclistMongoRepositoryAdapter extends AdapterOperations<Cyclist, CyclistDocument, String, CyclistMongoDBRepository>
implements CyclistRepository
{
    private final ReactiveMongoTemplate mongoTemplate;
    public CyclistMongoRepositoryAdapter(CyclistMongoDBRepository repository, ObjectMapper mapper, ReactiveMongoTemplate mongoTemplate) {
        super(repository, mapper, d -> mapper.map(d, Cyclist.class));
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Flux<Cyclist> findAllCyclist() {
        return repository.findAll()
                .onErrorResume(error -> Mono.error(new RuntimeException("Error getting all cyclist from MongoDB" + error.getMessage())))
                .switchIfEmpty(Mono.error(BusinessException.Type.ERROR_GETTING_All_cyclist.build("")))
                .map(this::convertToCyclist);
    }

    private Cyclist convertToCyclist(CyclistDocument cyclistDocument) {
        return Cyclist.builder()

                .cyclistName(cyclistDocument.getCyclistName())
                .cyclistNumber(cyclistDocument.getCyclistNumber())
                .teamCode(cyclistDocument.getTeamCode())
                .nationality(cyclistDocument.getNationality())
                .build();
    }

    @Override
    public Flux<Cyclist> findAllCyclistByNationality(String nationality) {
        return null;
    }

    @Override
    public Flux<Cyclist> findAllCyclistByTeamCode(String teamCode) {
        return null;
    }

    @Override
    public Mono<Cyclist> findCyclistById(String cyclistId) {
        return null;
    }

    @Override
    public Mono<Cyclist> saveCyclist(Cyclist cyclist) {
        return mongoTemplate.save(cyclist)
                .onErrorResume(error -> error instanceof DuplicateKeyException ?
                Mono.error(new BusinessException(BusinessException.Type.ERROR_CYCLIST, "An error occurred while creating the cyclist due to ID duplicates: " + error.getMessage()))
                : Mono.error(new RuntimeException("An error occurred while saving the cyclist" +  error.getMessage())));
    }

    private  CyclistDocument convertToCyclistDocument(Cyclist cyclist1) {
        return CyclistDocument.builder()

                .cyclistName(cyclist1.getCyclistName())
                .cyclistNumber(cyclist1.getCyclistNumber())
                .teamCode(cyclist1.getTeamCode())
                .nationality(cyclist1.getNationality())
                .build();
    }

    @Override
    public Mono<Cyclist> updateCyclistById(String cyclistId, Cyclist cyclist) {
        return null;
    }

    @Override
    public Mono<Void> deleteCyclistById(String cyclistId) {
        return null;
    }
}
