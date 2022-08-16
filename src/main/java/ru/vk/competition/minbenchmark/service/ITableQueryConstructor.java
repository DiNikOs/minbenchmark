/**
 * @author Ostrovskiy Dmitriy
 * @created 15.08.2022
 * interface ITableQueryConstructor
 * @version v1.0
 */

package ru.vk.competition.minbenchmark.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import ru.vk.competition.minbenchmark.dto.TableDto;
import ru.vk.competition.minbenchmark.entity.Report;
import ru.vk.competition.minbenchmark.entity.TableSave;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface ITableQueryConstructor {

    TableSave createTable(TableDto tableDto) throws HttpMediaTypeNotAcceptableException;

    ResponseEntity<Void> executeQuery(Optional<String> stringOptional);

    int dropTableIfExists(String name);

    Map<TableSave, Integer> getSizeRowTables(Report report);

    Map<TableSave, Integer> getSizeRowTables(Long id) throws HttpMediaTypeNotAcceptableException;

    Map<TableSave, Integer> getSizeRowTables(Set<TableSave> tables);


}
