/**
 * @author Ostrovskiy Dmitriy
 * @created 15.08.2022
 * class ReportDtoIn
 * @version v1.0
 */

package ru.vk.competition.minbenchmark.dto;

import lombok.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.HttpMediaTypeNotAcceptableException;

import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Validated
@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class ReportDtoIn {

    @PositiveOrZero(message = "Report id should be positive or zero!")
    @NotNull(message = "Report id must not be null!")
    private Integer reportId;

    @PositiveOrZero(message = "Table Amount should be positive or zero!")
    @NotNull(message = "Table Amount must not be null!")
    private Integer tableAmount;

    @NotEmpty(message = "Tables must not be null!")
    private List<TableReportDtoIn> tables;

    @AssertTrue(message = "Tables Amount is not equal List Column Infos!")
    private boolean isValid() throws HttpMediaTypeNotAcceptableException {
        return tableAmount == tables.size();
    }

    public ReportDtoIn(@PositiveOrZero(message = "Report id should be positive or zero!")
                       @NotNull(message = "Report id must not be null!") Integer reportId,
                       @PositiveOrZero(message = "Table Amount should be positive or zero!")
                       @NotNull(message = "Table Amount must not be null!") Integer tableAmount,
                       @NotEmpty(message = "Tables must not be null!") List<TableReportDtoIn> tables) {
        this.reportId = reportId;
        this.tableAmount = tableAmount;
        this.tables = tables;
    }
}
