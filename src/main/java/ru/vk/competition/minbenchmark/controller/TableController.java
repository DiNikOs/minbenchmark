/**
 * @author Ostrovskiy Dmitriy
 * @created 13.08.2022
 * class TableController
 * @version v1.0
 */

package ru.vk.competition.minbenchmark.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.vk.competition.minbenchmark.dto.TableDto;
import ru.vk.competition.minbenchmark.entity.SingleQuery;
import ru.vk.competition.minbenchmark.entity.TableSave;
import ru.vk.competition.minbenchmark.service.ITableService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Validated
@Slf4j
@RestController
@RequestMapping("/api/table")
@RequiredArgsConstructor
public class TableController {

    private final ITableService tableService;

    @GetMapping("/get-all-tables")
    public Flux<TableSave> getAllTables() {
        return tableService.getAllTables();
    }

    @PostMapping(value = "/create-table", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Void>> createTable(@RequestBody @Valid TableDto tableDto) {
        return tableService.createTable(tableDto);
    }

    @GetMapping("/get-table-by-name/{name}")
    public Mono<ResponseEntity<TableDto>> getTableByName(@PathVariable @Valid
                                                         @Size(max = 50, message = "Too long > 50!")
                                                         @NotNull String name) {
        return tableService.getTableByName(name);
    }

    @DeleteMapping("/drop-table-by-name/{name}")
    public Mono<ResponseEntity<Void>> dropTableByName(@PathVariable @Valid
                                                          @Size(max = 50, message = "Too long > 50!")
                                                          @NotNull String name) {
        return tableService.dropTableByName(name);
    }

}
