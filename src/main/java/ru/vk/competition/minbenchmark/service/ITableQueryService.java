/**
 * @author Ostrovskiy Dmitriy
 * @created 13.08.2022
 * interface ITableQueryService
 * @version v1.0
 */

package ru.vk.competition.minbenchmark.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.vk.competition.minbenchmark.dto.TableQueryDto;
import ru.vk.competition.minbenchmark.entity.TableQuery;

public interface ITableQueryService {

    Mono<ResponseEntity<Void>> addNewQueryToTable(TableQueryDto tableQueryDto);

    Mono<ResponseEntity<Void>> modifyQueryInTable(TableQueryDto tableQueryDto);

    Mono<ResponseEntity<Void>> deleteTableQueryById(Integer id);

    Mono<ResponseEntity<Void>> executeTableQueryById(Integer id);

    Flux<TableQueryDto> getAllQueriesByTableName(String name);

    Mono<ResponseEntity<TableQueryDto>> getTableQueryById(Integer id);

    Flux<TableQueryDto> getAllTableQueries();

}
