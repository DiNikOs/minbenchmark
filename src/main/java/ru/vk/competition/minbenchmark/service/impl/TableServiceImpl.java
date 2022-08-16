/**
 * @author Ostrovskiy Dmitriy
 * @created 13.08.2022
 * class TableService
 * @version v1.0
 */

package ru.vk.competition.minbenchmark.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ru.vk.competition.minbenchmark.dto.TableDto;
import ru.vk.competition.minbenchmark.entity.TableSave;
import ru.vk.competition.minbenchmark.repository.TableQueryConstructorImpl;
import ru.vk.competition.minbenchmark.repository.TableRepository;
import ru.vk.competition.minbenchmark.service.ITableService;

@Slf4j
@Service
@RequiredArgsConstructor
public class TableServiceImpl implements ITableService {

    private final TableRepository tableRepository;
    private final TableQueryConstructorImpl tableQueryConstructor;

    @Override
    public Flux<TableSave> getAllTables() {
        return Mono.fromCallable(tableRepository::findAll)
                .publishOn(Schedulers.boundedElastic())
                .flatMapIterable(x -> x);
    }

    @Transactional
    @Override
    public Mono<ResponseEntity<Void>> createTable(TableDto tableDto) {
        return Mono.fromCallable(() -> {
            TableSave tableSave = tableQueryConstructor.createTable(tableDto);
            return new ResponseEntity<Void>(tableSave != null? HttpStatus.CREATED : HttpStatus.NOT_ACCEPTABLE);
        }).publishOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<ResponseEntity<TableDto>> getTableByName(String name) {
        return Mono.fromCallable(() -> {
            return new ResponseEntity<TableDto>(
                    tableRepository.getByName(name).orElse(null),
                    HttpStatus.OK);
        }).publishOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<ResponseEntity<Void>> dropTableByName(String name) {
        return Mono.fromCallable(() -> {
            try {
                if(!tableRepository.existsByTableName(name)) {
                    log.info("Not Found Table: {}!", name);
                    return new ResponseEntity<Void>(HttpStatus.NOT_ACCEPTABLE);
                } else {
                    int queryDelete = tableQueryConstructor.dropTableIfExists(name);
                    if (queryDelete != 0) {
                        return new ResponseEntity<Void>(HttpStatus.NOT_ACCEPTABLE);
                    }
                    tableRepository.deleteTableSaveByTableName(name);
                    log.info("Delete Table: {}!", name);
                    return new ResponseEntity<Void>(HttpStatus.CREATED);
                }
            } catch (Exception e) {
                log.warn("Exception delete table", e);
                return new ResponseEntity<Void>(HttpStatus.NOT_ACCEPTABLE);
            }
        }).publishOn(Schedulers.boundedElastic());
    }

}
