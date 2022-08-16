/**
 * @author Ostrovskiy Dmitriy
 * @created 15.08.2022
 * class TableCreateDto
 * @version v1.0
 */

package ru.vk.competition.minbenchmark.dto;

import lombok.*;
import org.springframework.validation.annotation.Validated;
import ru.vk.competition.minbenchmark.entity.TableSave;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.List;

@Validated
@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class TableReportDtoIn {

    @Pattern(regexp = "^[_A-Za-z0-9]+$")
    @NotBlank(message = "Table must not be null!")
    private String tableName;

    @NotEmpty(message = "Column Infos must not be null!")
    private List<ColumnInfoDto> columns;

    public TableReportDtoIn(TableSave table) {
        this.tableName = table.getTableName();
        this.columns = table.getListColumnInfoDto();
    }

    public TableReportDtoIn(@Pattern(regexp = "^[_A-Za-z0-9]+$")
                            @NotBlank(message = "Table must not be null!") String tableName,
                            @NotEmpty(message = "Column Infos must not be null!") List<ColumnInfoDto> columnInfos) {
        this.tableName = tableName;
        this.columns = columnInfos;
    }
}
