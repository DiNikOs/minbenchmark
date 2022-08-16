/**
 * @author Ostrovskiy Dmitriy
 * @created 13.08.2022
 * class TableQueryService
 * @version v1.0
 */

package ru.vk.competition.minbenchmark.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ru.vk.competition.minbenchmark.dto.TableQueryDto;
import ru.vk.competition.minbenchmark.entity.TableQuery;
import ru.vk.competition.minbenchmark.entity.TableSave;
import ru.vk.competition.minbenchmark.repository.TableQueryRepository;
import ru.vk.competition.minbenchmark.repository.TableRepository;
import ru.vk.competition.minbenchmark.service.ITableQueryConstructor;
import ru.vk.competition.minbenchmark.service.ITableQueryService;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TableQueryServiceImpl implements ITableQueryService {

    private final TableQueryRepository tableQueryRepository;
    private final TableRepository tableRepository;
    private final ITableQueryConstructor constructor;

    @Transactional
    @Override
    public Mono<ResponseEntity<Void>> addNewQueryToTable(TableQueryDto tableQueryDto) {
        return Mono.fromCallable(() -> {
            TableSave tableSave = tableRepository.findByTableName(tableQueryDto.getTableName());
            TableQuery tableQuery = null;
            if (tableSave == null) {
                return new ResponseEntity<Void>(HttpStatus.NOT_ACCEPTABLE);
            }
            TableQuery tableQuerySave = new TableQuery(tableQueryDto, tableSave);
            Long id = tableQueryDto.getQueryId().longValue();
            if (tableQueryRepository.existsById(id)) {
                log.info("Query isExists by id: {}", id);
                return new ResponseEntity<Void>(HttpStatus.NOT_ACCEPTABLE);
            }
            try {
                tableQuery = tableQueryRepository.save(tableQuerySave);
            } catch (Exception e) {
                return new ResponseEntity<Void>(HttpStatus.NOT_ACCEPTABLE);
            }
            return new ResponseEntity<Void>(tableQuery == null? HttpStatus.NOT_ACCEPTABLE : HttpStatus.CREATED);
        }).publishOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<ResponseEntity<Void>> modifyQueryInTable(TableQueryDto tableQueryDto) {
        return Mono.fromCallable(() -> {
            TableSave tableSave = tableRepository.findByTableName(tableQueryDto.getTableName());
            TableQuery tableQuery = null;
            if (tableSave == null) {
                return new ResponseEntity<Void>(HttpStatus.NOT_ACCEPTABLE);
            }
            try {
                TableQuery tableQueryOptional = tableQueryRepository.findById(tableQueryDto.getQueryId().longValue())
                        .orElseThrow(() -> new HttpMediaTypeNotAcceptableException(
                                "TableQuery not found! by queryId:" + tableQueryDto.getQueryId()));
                tableQueryOptional.setQuery(tableQueryDto.getQuery());
                tableQuery = tableQueryRepository.save(tableQueryOptional);
            } catch (Exception e) {
                return new ResponseEntity<Void>(HttpStatus.NOT_ACCEPTABLE);
            }
            return new ResponseEntity<Void>(tableQuery == null? HttpStatus.NOT_ACCEPTABLE : HttpStatus.OK);
        }).publishOn(Schedulers.boundedElastic());
    }

    @Transactional
    @Override
    public Mono<ResponseEntity<Void>> deleteTableQueryById(Integer queryId) {
        return Mono.fromCallable(() -> {
            Long id = queryId.longValue();
            TableQuery tableQueryOptional = tableQueryRepository.findById(id)
                    .orElseThrow(() -> new HttpMediaTypeNotAcceptableException(
                            "TableQuery not found! by Id:" + id));
            tableQueryRepository.delete(tableQueryOptional);
            return new ResponseEntity<Void>(tableQueryRepository.existsById(id)? HttpStatus.NOT_ACCEPTABLE : HttpStatus.ACCEPTED);
        }).publishOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<ResponseEntity<Void>> executeTableQueryById(Integer queryId) {
        return Mono.fromCallable(() -> {
            Optional<String> tableQuery = tableQueryRepository.findById(queryId.longValue()).map(TableQuery::getQuery);
            return constructor.executeQuery(tableQuery);
        }).publishOn(Schedulers.boundedElastic());
    }

    @Transactional(readOnly=true)
    @Override
    public Flux<TableQueryDto> getAllQueriesByTableName(String name) {
        return Mono.fromCallable(() -> {
            return tableQueryRepository.findByTableName(name);
        }).publishOn(Schedulers.boundedElastic())
                .flatMapIterable(x -> x);
    }

    @Transactional(readOnly=true)
    @Override
    public Mono<ResponseEntity<TableQueryDto>> getTableQueryById(Integer queryId) {
        return Mono.fromCallable(() -> {
            Long id = queryId.longValue();
            TableQuery tableQueryOptional = tableQueryRepository.findById(id)
                    .orElseThrow(() -> new HttpMessageNotWritableException(
                            "TableQuery not found! by Id:" + id));
            tableQueryRepository.delete(tableQueryOptional);
            return new ResponseEntity<>(new TableQueryDto(tableQueryOptional), HttpStatus.OK);
        }).publishOn(Schedulers.boundedElastic());
    }

    @Transactional(readOnly=true)
    @Override
    public Flux<TableQueryDto> getAllTableQueries() {
        return Mono.fromCallable(tableQueryRepository::findAllQuery)
                .publishOn(Schedulers.boundedElastic())
                .flatMapIterable(x -> x);
    }

}
