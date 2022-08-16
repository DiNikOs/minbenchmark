package ru.vk.competition.minbenchmark.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.vk.competition.minbenchmark.dto.SingleQueryDto;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Data
@Builder
@Entity
@Table(name = "single_query")
@NoArgsConstructor
@AllArgsConstructor
public class SingleQuery {

    @Id
    @PositiveOrZero(message = "Column queryId should be positive or zero!")
    @Column(name = "queryId")
    private Long queryId;

    @Size(max = 120, message = "Too long > 120!")
    @NotBlank(message = "Query must not be null!")
    @Column(name = "query", length = 120)
    private String query;

    public SingleQuery(SingleQueryDto singleQuery) {
        this.queryId = singleQuery.getQueryId().longValue();
        this.query = singleQuery.getQuery();
    }

    public void setSingleQuery(SingleQueryDto singleQuery) {
        this.queryId = singleQuery.getQueryId().longValue();
        this.query = singleQuery.getQuery();
    }

}