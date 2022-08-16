/**
 * @author Ostrovskiy Dmitriy
 * @created 14.08.2022
 * interface ISingleQueryService
 * @version v1.0
 */

package ru.vk.competition.minbenchmark.service;

import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.vk.competition.minbenchmark.dto.SingleQueryDto;
import ru.vk.competition.minbenchmark.entity.SingleQuery;

public interface ISingleQueryService {

    Flux<SingleQuery> getAllQueries();

    Mono<ResponseEntity<SingleQueryDto>> getQueryById(Integer id);

    Mono<ResponseEntity<Void>> deleteQueryById(Integer id);

    Mono<ResponseEntity<Void>> addNewQuery(SingleQueryDto singleQuery);

    Mono<ResponseEntity<Void>> modifySingleQuery(SingleQueryDto singleQuery);

    Mono<ResponseEntity<Void>> executeSingleQueryById(Integer id);

}
