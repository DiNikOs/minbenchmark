/**
 * @author Ostrovskiy Dmitriy
 * @created 13.08.2022
 * class ReportDto
 * @version v1.0
 */

package ru.vk.competition.minbenchmark.dto;

import lombok.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import ru.vk.competition.minbenchmark.entity.Report;
import ru.vk.competition.minbenchmark.entity.TableSave;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Validated
@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ReportDto {

    @PositiveOrZero(message = "Report id should be positive or zero!")
    @NotNull(message = "Report id must not be null!")
    private Integer reportId;

    @PositiveOrZero(message = "Table Amount should be positive or zero!")
    @NotNull(message = "Table Amount must not be null!")
    private Integer tableAmount;

    @NotEmpty(message = "Tables must not be null!")
    @Valid
    private List<TableReportDto> tables;

    @AssertTrue(message = "Tables Amount is not equal List Column Infos!")
    private boolean isValid() throws HttpMediaTypeNotAcceptableException {
        boolean isNotEquals = tableAmount != tables.size();
        if (!isNotEquals) {
            throw new HttpMediaTypeNotAcceptableException("Tables Amount is not equal List Column Infos!");
        }
        return isNotEquals;
    }

    public ReportDto(Report report, Map<TableSave, Integer> tableReports) {
        this.reportId = report.getReportId().intValue();
        this.tableAmount = report.getTables().size();
        this.tables = report.getTables().stream()
                .map(t -> new TableReportDto(t, tableReports.get(t)))
                .collect(Collectors.toList());
    }
}
