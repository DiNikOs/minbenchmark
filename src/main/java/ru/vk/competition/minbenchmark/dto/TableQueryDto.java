/**
 * @author Ostrovskiy Dmitriy
 * @created 13.08.2022
 * class TableQueryDto
 * @version v1.0
 */

package ru.vk.competition.minbenchmark.dto;

import lombok.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import ru.vk.competition.minbenchmark.entity.TableQuery;

import javax.validation.constraints.*;

@Validated
@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class TableQueryDto {

    @PositiveOrZero(message = "QueryId should be positive or zero!")
    @NotNull(message = "Query Id must not be null!")
    private Integer queryId;

    @Pattern(regexp = "^[_A-Za-z0-9]+$")
    @Size(max = 50, message = "Too long > 50!")
    @NotBlank(message = "TableName must not be null!")
    private String tableName;

    @Size(max = 120, message = "Too long > 120!")
    @NotBlank(message = "Query must not be null!")
    private String query;

    @AssertTrue(message = "Query does not contain tableName!")
    private boolean isValid() throws HttpMediaTypeNotAcceptableException {
        boolean isNotEquals = query.contains(tableName);
        if (!isNotEquals) {
            throw new HttpMediaTypeNotAcceptableException("Query does not contain tableName!");
        }
        return isNotEquals;
    }

    public TableQueryDto(TableQuery tableQuery) {
        this.queryId = tableQuery.getQueryId().intValue();
        this.tableName = tableQuery.getTableName();
        this.query = tableQuery.getQuery();
    }

}
