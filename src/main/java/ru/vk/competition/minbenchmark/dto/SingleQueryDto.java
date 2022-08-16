/**
 * @author Ostrovskiy Dmitriy
 * @created 14.08.2022
 * class SingleQueryDto
 * @version v1.0
 */

package ru.vk.competition.minbenchmark.dto;

import lombok.*;
import org.springframework.validation.annotation.Validated;
import ru.vk.competition.minbenchmark.entity.SingleQuery;

import javax.validation.constraints.*;

@Validated
@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class SingleQueryDto {

    @PositiveOrZero(message = "QueryId should be positive or zero!")
    @NotNull(message = "Query Id must not be null!")
    private Integer queryId;

    @Size(max = 120, message = "Too long > 120!")
    @NotBlank(message = "Query must not be null!")
    private String query;

    public SingleQueryDto(SingleQuery singleQuery) {
        this.queryId = singleQuery.getQueryId().intValue();
        this.query = singleQuery.getQuery();
    }

}
