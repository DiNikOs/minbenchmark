/**
 * @author Ostrovskiy Dmitriy
 * @created 13.08.2022
 * class TableQueryController
 * @version v1.0
 */

package ru.vk.competition.minbenchmark.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.vk.competition.minbenchmark.dto.TableDto;
import ru.vk.competition.minbenchmark.dto.TableQueriesDto;
import ru.vk.competition.minbenchmark.dto.TableQueryDto;
import ru.vk.competition.minbenchmark.entity.TableQuery;
import ru.vk.competition.minbenchmark.entity.TableSave;
import ru.vk.competition.minbenchmark.service.ITableQueryService;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Slf4j
@RestController
@RequestMapping("/api/table-query")
@RequiredArgsConstructor
public class TableQueryController {

    private final ITableQueryService tableQueryService;

    @PostMapping(value = "/add-new-query-to-table", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Void>> addNewQueryToTable(@RequestBody @Valid TableQueryDto tableDto) {
        return tableQueryService.addNewQueryToTable(tableDto);
    }

    @PutMapping(value = "/modify-query-in-table", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Void>> modifyQueryInTable(@RequestBody @Valid TableQueryDto tableDto) {
        return tableQueryService.modifyQueryInTable(tableDto);
    }

    @DeleteMapping("/delete-table-query-by-id/{id}")
    public Mono<ResponseEntity<Void>> deleteTableQueryById(@PathVariable("id") @NotNull @PositiveOrZero
                                                            Integer id) {
        return tableQueryService.deleteTableQueryById(Integer.valueOf(id));
    }

    @GetMapping("/execute-table-query-by-id/{id}")
    public Mono<ResponseEntity<Void>> executeTableQueryById(@PathVariable("id")
                                                                @NotNull @PositiveOrZero
                                                                        Integer id) {
        return tableQueryService.executeTableQueryById(id);
    }

    @GetMapping("/get-all-queries-by-table-name/{name}")
    @ResponseStatus(HttpStatus.OK)
    public Flux<TableQueryDto> getAllQueriesByTableName(@PathVariable("name")
                                                     @Size(max = 50, message = "Too long > 50!")
                                                     @NotNull String name) {
        return tableQueryService.getAllQueriesByTableName(name);
    }

    @GetMapping("/get-table-query-by-id/{id}")
    public Mono<ResponseEntity<TableQueryDto>> getTableQueryById(@PathVariable("id")
                                                                     @NotNull @PositiveOrZero
                                                                             Integer id) {
        return tableQueryService.getTableQueryById(id);
    }

    @GetMapping("/get-all-table-queries")
    @ResponseStatus(HttpStatus.OK)
    public Flux<TableQueryDto> getAllTableQueries() {
        return tableQueryService.getAllTableQueries();
    }

}
