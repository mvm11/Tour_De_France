package co.com.api.usecase.findallcyclist.cyclist;

import co.com.api.model.common.ex.BusinessException;
import co.com.api.model.cyclist.Cyclist;
import co.com.api.model.cyclist.gateways.CyclistRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Log
@RequiredArgsConstructor
public class SaveCyclistUseCase {

    private final CyclistRepository cyclistRepository;

    private final FindAllCyclistUseCase findAllCyclistUseCase;

    public Mono<Cyclist> saveCyclist(Cyclist cyclist){
        return Mono.just(cyclist).flatMap(this::validateCyclistNumber);
    }

    private Mono<Cyclist> validateCyclistNumber(Cyclist cyclist1) {
        return findAllCyclistUseCase
                .findAllCyclist()
                .filter(cyclist -> cyclist1.getCyclistNumber().equalsIgnoreCase(cyclist.getCyclistNumber()))
                .collectList()
                .flatMap(list -> (list.isEmpty()) ? validateCyclistFields(cyclist1) : Mono.error(BusinessException.Type.DUPLICATE_CYCLIST_NUMBER.build("")));
    }

    private Mono<Cyclist> validateCyclistFields(Cyclist cyclist1) {
        return Mono.just(Arrays.stream(cyclist1.cyclistFields())
                        .filter(this::getFieldsPredicate)
                        .collect(Collectors.toList()))
                .flatMap(validateCyclistFieldsSize(cyclist1));
    }

    private boolean getFieldsPredicate(String field) {
        return !isNull(field) && !field.equalsIgnoreCase("");
    }
    private Function<List<String>, Mono<Cyclist>> validateCyclistFieldsSize(Cyclist cyclist1) {
        return list -> (list.size() == cyclist1.cyclistFields().length)
                ? cyclistRepository.saveCyclist(cyclist1)
                : Mono.error(BusinessException.Type.INCOMPLETE_CYCLIST_INFORMATION.build(""));
    }

}
