/**
 * @author Ostrovskiy Dmitriy
 * @created 15.08.2022
 * class TableReportDto
 * @version v1.0
 */

package ru.vk.competition.minbenchmark.dto;

import lombok.*;
import org.springframework.validation.annotation.Validated;
import ru.vk.competition.minbenchmark.entity.ColumnInfo;
import ru.vk.competition.minbenchmark.entity.TableSave;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Validated
@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class TableReportDto {

    @Pattern(regexp = "^[_A-Za-z0-9]+$")
    @NotBlank(message = "Table must not be null!")
    private String tableName;

    @NotEmpty(message = "Columns must not be null!")
    @Valid
    private List<ColumnReportDto> columns;

    public TableReportDto(TableSave tableSave, int size) {
        this.tableName = tableSave.getTableName();
        this.columns = getListColumnInfo(tableSave.getColumnInfos(), size);
    }

    public List<ColumnReportDto> getListColumnInfo(List<ColumnInfo> columns, int size) {
        return columns.stream().map(column -> new ColumnReportDto(column, size)).collect(Collectors.toList());
    }
}
