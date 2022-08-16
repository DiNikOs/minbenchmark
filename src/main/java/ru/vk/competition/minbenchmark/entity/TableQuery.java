/**
 * @author Ostrovskiy Dmitriy
 * @created 13.08.2022
 * class TableQuery
 * @version v1.0
 */

package ru.vk.competition.minbenchmark.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import ru.vk.competition.minbenchmark.dto.TableQueryDto;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "table_query")
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class TableQuery implements Serializable {

    @Id
    @Column(name = "query_id")
    private Long queryId;

    @Pattern(regexp = "^[_A-Za-z0-9]+$")
    @Size(max = 50, message = "Too long > 50!")
    @NotBlank(message = "TableName must not be null!")
    @Column(name = "table_name", length = 50)
    private String tableName;

    @Size(max = 120, message = "Too long > 120!")
    @NotBlank(message = "Query must not be null!")
    @Column(name = "query", length = 120)
    private String query;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY,
            optional = false)
    @JoinColumn(name = "table_id", nullable = false)
    private TableSave tableSave;

    public TableQuery(TableQueryDto tableQuery, TableSave tableSave) {
        this.queryId = tableQuery.getQueryId().longValue();
        this.tableName = tableQuery.getTableName();
        this.query = tableQuery.getQuery();
        this.tableSave = tableSave;
    }
}
