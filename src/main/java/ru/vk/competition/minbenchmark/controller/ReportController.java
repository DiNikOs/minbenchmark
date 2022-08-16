/**
 * @author Ostrovskiy Dmitriy
 * @created 13.08.2022
 * class ReportController
 * @version v1.0
 */

package ru.vk.competition.minbenchmark.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.vk.competition.minbenchmark.dto.ReportDto;
import ru.vk.competition.minbenchmark.dto.ReportDtoIn;
import ru.vk.competition.minbenchmark.service.IReportService;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/report")
@RequiredArgsConstructor
public class ReportController {

    private final IReportService reportService;

    @GetMapping("/get-report-by-id/{id}")
    public Mono<ResponseEntity<ReportDto>> getReportById(@PathVariable Integer id) {
        return reportService.getReportById(id);
    }

    @PostMapping("/create-report")
    public Mono<ResponseEntity<Void>> createReport(@RequestBody @Valid ReportDtoIn reportDto) {
        return reportService.createReport(reportDto);
    }

}
