package ru.vk.competition.minbenchmark.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.vk.competition.minbenchmark.dto.SingleQueryDto;
import ru.vk.competition.minbenchmark.entity.SingleQuery;
import ru.vk.competition.minbenchmark.service.ISingleQueryService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Slf4j
@RestController
@RequestMapping("/api/single-query")
@RequiredArgsConstructor
public class SingleQueryController {

    private final ISingleQueryService singleQueryService;

    @GetMapping("/get-all-single-queries")
    public Flux<SingleQuery> getAllTableQueries() {
        return singleQueryService.getAllQueries();
    }

    @GetMapping("/get-single-query-by-id/{id}")
    public Mono<ResponseEntity<SingleQueryDto>> getTableQueryById(@PathVariable("id")
                                                                      @NotNull @PositiveOrZero
                                                                              Integer id) {
        return singleQueryService.getQueryById(id);
    }

    @DeleteMapping("/delete-single-query-by-id/{id}")
    public Mono<ResponseEntity<Void>> deleteTableQueryById(@PathVariable("id")
                                                               @NotNull @PositiveOrZero
                                                                       Integer id) {
        return singleQueryService.deleteQueryById(id);
    }

    @PostMapping("/add-new-query")
    public Mono<ResponseEntity<Void>> addNewQuery(@RequestBody @Valid SingleQueryDto singleQuery) throws HttpMessageConversionException {
        return singleQueryService.addNewQuery(singleQuery);
    }


    @PutMapping("/modify-single-query")
    public Mono<ResponseEntity<Void>> modifySingleQuery(@RequestBody @Valid SingleQueryDto singleQuery) {
        return singleQueryService.modifySingleQuery(singleQuery);
    }

    @GetMapping("/execute-single-query-by-id/{id}")
    public Mono<ResponseEntity<Void>> executeSingleQueryById(@PathVariable("id")
                                                                 @NotNull @PositiveOrZero
                                                                         Integer id) {
        return singleQueryService.executeSingleQueryById(id);
    }
}