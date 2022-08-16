/**
 * @author Ostrovskiy Dmitriy
 * @created 13.08.2022
 * class ReportServiceImpl
 * @version v1.0
 */

package ru.vk.competition.minbenchmark.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ru.vk.competition.minbenchmark.dto.*;
import ru.vk.competition.minbenchmark.entity.*;
import ru.vk.competition.minbenchmark.repository.ReportRepository;
import ru.vk.competition.minbenchmark.repository.TableRepository;
import ru.vk.competition.minbenchmark.service.IReportService;
import ru.vk.competition.minbenchmark.service.ITableQueryConstructor;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements IReportService {

    private final ReportRepository reportRepository;
    private final TableRepository tableRepository;
    private final ITableQueryConstructor constructor;

    @Override
    public Mono<ResponseEntity<ReportDto>> getReportById(Integer id) {
        return Mono.fromCallable(() -> {
            Report report = reportRepository.findById(id.longValue()).orElseThrow(() ->
                    new HttpMediaTypeNotAcceptableException(String.format("Cannot find Report by Id %s", id.toString())
                    ));

            Map<TableSave, Integer> tableReports = constructor.getSizeRowTables(report.getTables());
            ReportDto reportDto = new ReportDto(report, tableReports);
            return new ResponseEntity<ReportDto> (reportDto, HttpStatus.CREATED);
        }).publishOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<ResponseEntity<Void>> createReport(ReportDtoIn reportDto) {
        return Mono.fromCallable(() -> {
            Long id = reportDto.getReportId().longValue();
            if (reportRepository.existsById(id)) {
                log.debug("Report already exists with this id: {}", id);
                return new ResponseEntity<Void>(HttpStatus.NOT_ACCEPTABLE);
            }
            List<String> tableNamesDto = reportDto.getTables().stream().map(TableReportDtoIn::getTableName).collect(Collectors.toList());
            Set<TableSave> tableSaves = tableRepository.findAllByTableNameIn(tableNamesDto);
            if (tableNamesDto.size() != tableSaves.size()) {
                log.debug("One of the tables with this name does not exist by report: {}", reportDto.getReportId());
                return new ResponseEntity<Void>(HttpStatus.NOT_ACCEPTABLE);
            }
            List<TableReportDtoIn> tableCreateDtos = reportDto.getTables();
            fillUpperCaseColumns(reportDto.getTables());

            List<TableReportDtoIn> tableSaveDtos = tableSaves.stream()
                    .map(t -> new TableReportDtoIn(t.getTableName(), t.getListColumnInfoDto()))
                    .collect(Collectors.toList());

            if (!tableCreateDtos.containsAll(tableSaveDtos)) {
                log.debug("One of the tables, column with this name does not exist by report: {}", reportDto.getReportId());
                return new ResponseEntity<Void>(HttpStatus.NOT_ACCEPTABLE);
            }

            Report report = reportRepository.save(new Report(reportDto.getReportId().longValue(), tableSaves));
            return new ResponseEntity<Void>(report != null? HttpStatus.CREATED : HttpStatus.NOT_ACCEPTABLE);
        }).publishOn(Schedulers.boundedElastic());
    }

    private void fillUpperCaseColumns(List<TableReportDtoIn> tableCreateDtos) {
        for (TableReportDtoIn tableCreateDto : tableCreateDtos) {
            for (ColumnInfoDto column : tableCreateDto.getColumns()) {
                column.setTitle(column.getTitle().toUpperCase());
                column.setType(column.getType().toUpperCase());
            }
        }
    }

}
