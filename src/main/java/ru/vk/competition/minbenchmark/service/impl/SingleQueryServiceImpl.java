package ru.vk.competition.minbenchmark.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Service;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ru.vk.competition.minbenchmark.dto.SingleQueryDto;
import ru.vk.competition.minbenchmark.entity.SingleQuery;
import ru.vk.competition.minbenchmark.repository.SingleQueryRepository;
import ru.vk.competition.minbenchmark.service.ISingleQueryService;
import ru.vk.competition.minbenchmark.service.ITableQueryConstructor;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class SingleQueryServiceImpl implements ISingleQueryService {

    private final SingleQueryRepository queryRepository;
    private final ITableQueryConstructor tableQueryConstructor;

    public Flux<SingleQuery> getAllQueries() {
        return Mono.fromCallable(queryRepository::findAll)
                .publishOn(Schedulers.boundedElastic())
                .flatMapIterable(x -> x);
    }

    public Mono<ResponseEntity<SingleQueryDto>> getQueryById(Integer id) {
        return Mono.fromCallable(() -> {
            SingleQuery singleQuery = queryRepository.findByQueryId(id.longValue()).orElseThrow(() ->
                    new HttpMessageNotWritableException(String.format("Cannot find tableQuery by Id %s", id.toString())
        ));
            return new ResponseEntity<SingleQueryDto> (new SingleQueryDto(singleQuery), HttpStatus.OK);
        }).publishOn(Schedulers.boundedElastic());
    }

    public Mono<ResponseEntity<Void>> deleteQueryById(Integer id) {
        return Mono.fromCallable(() -> {
            try {
                if(queryRepository.findByQueryId(id.longValue()).map(SingleQuery::getQueryId).isEmpty()) {
                    return new ResponseEntity<Void>(HttpStatus.NOT_ACCEPTABLE);
                } else {
                    queryRepository.deleteByQueryId(id);
                    return new ResponseEntity<Void>(HttpStatus.ACCEPTED);
                }
            } catch (Exception e) {
                return new ResponseEntity<Void>(HttpStatus.NOT_ACCEPTABLE);
            }
        }).publishOn(Schedulers.boundedElastic());
    }

    public Mono<ResponseEntity<Void>> addNewQuery(SingleQueryDto singleQuery) {
        return Mono.fromCallable(() -> {
            try {
                queryRepository.save(new SingleQuery(singleQuery));
                return new ResponseEntity<Void>(HttpStatus.CREATED);
            } catch (Exception e) {
                return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
            }
        }).publishOn(Schedulers.boundedElastic());
    }

    public Mono<ResponseEntity<Void>> modifySingleQuery(SingleQueryDto singleQueryDto) {
        return Mono.fromCallable(() -> {
            SingleQuery singleQuery = queryRepository.findByQueryId(singleQueryDto.getQueryId().longValue())
                    .orElseThrow(() -> new HttpMediaTypeNotAcceptableException(
                    String.format("Cannot find tableQuery by ID %s", singleQueryDto.getQueryId())));
            singleQuery.setSingleQuery(singleQueryDto);
            queryRepository.save(singleQuery);
            return ResponseEntity.<Void>ok(null);
        }).publishOn(Schedulers.boundedElastic());
    }

    public Mono<ResponseEntity<Void>> executeSingleQueryById(Integer id) {
        return Mono.fromCallable(() -> {
            Optional<String> createSql = queryRepository.findByQueryId(id.longValue()).map(SingleQuery::getQuery);
            return tableQueryConstructor.executeQuery(createSql);
        }).publishOn(Schedulers.boundedElastic());
    }

}