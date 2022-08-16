/**
 * @author Ostrovskiy Dmitriy
 * @created 13.08.2022
 * interface ITableService
 * @version v1.0
 */

package ru.vk.competition.minbenchmark.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.vk.competition.minbenchmark.dto.TableDto;
import ru.vk.competition.minbenchmark.entity.TableSave;

public interface ITableService {

    Flux<TableSave> getAllTables();

    Mono<ResponseEntity<Void>> createTable(TableDto tableDto);

    Mono<ResponseEntity<TableDto>> getTableByName(String name);

    Mono<ResponseEntity<Void>> dropTableByName(String name);

}
