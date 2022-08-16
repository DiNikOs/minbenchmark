/**
 * @author Ostrovskiy Dmitriy
 * @created 13.08.2022
 * class TableQueriesDto
 * @version v1.0
 */

package ru.vk.competition.minbenchmark.dto;

import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class TableQueriesDto {

    @NotEmpty(message = "Table Queries not be null!")
    List<TableQueryDto> tableQueries;
}
