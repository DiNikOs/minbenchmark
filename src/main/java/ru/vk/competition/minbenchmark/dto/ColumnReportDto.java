/**
 * @author Ostrovskiy Dmitriy
 * @created 15.08.2022
 * class ColumnReportDto
 * @version v1.0
 */

package ru.vk.competition.minbenchmark.dto;

import lombok.*;
import org.springframework.validation.annotation.Validated;
import ru.vk.competition.minbenchmark.entity.ColumnInfo;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Validated
@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ColumnReportDto extends ColumnInfoDto{

    @PositiveOrZero(message = "Size should be positive or zero!")
    @NotNull
    private Integer size;

    public ColumnReportDto(ColumnInfo columnInfo, int size) {
        super(columnInfo);
        this.size = size;
    }
}
