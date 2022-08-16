/**
 * @author Ostrovskiy Dmitriy
 * @created 13.08.2022
 * interface IReportService
 * @version v1.0
 */

package ru.vk.competition.minbenchmark.service;

import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import ru.vk.competition.minbenchmark.dto.ReportDto;
import ru.vk.competition.minbenchmark.dto.ReportDtoIn;

public interface IReportService {

    Mono<ResponseEntity<ReportDto>> getReportById(Integer id);

    Mono<ResponseEntity<Void>> createReport(ReportDtoIn reportDto);

}
