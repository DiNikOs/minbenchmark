/**
 * @author Ostrovskiy Dmitriy
 * @created 13.08.2022
 * class Table
 * @version v1.0
 */

package ru.vk.competition.minbenchmark.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import ru.vk.competition.minbenchmark.dto.ColumnInfoDto;
import ru.vk.competition.minbenchmark.dto.TableDto;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "table_save")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TableSave {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "table_id")
    private Long tableId;

    @Pattern(regexp = "^[_A-Za-z0-9]+$")
    @NotBlank(message = "Primary Key must not be null!")
    @Size(max = 50, message = "Too long > 50!")
    @Column(name = "primary_key", length = 50)
    private String primaryKey;

    @Pattern(regexp = "^[_A-Za-z0-9]+$")
    @NotBlank(message = "Table must not be null!")
    @Size(max = 50, message = "Too long > 50!")
    @Column(name = "table_name", length = 50, unique = true)
    private String tableName;

    @JsonManagedReference
    @OneToMany(fetch = FetchType.EAGER,
            mappedBy = "tableSave",
            cascade = {CascadeType.REFRESH, CascadeType.DETACH,
                    CascadeType.MERGE, CascadeType.PERSIST},
            orphanRemoval = true)
    private List<ColumnInfo> columnInfos;

    @JsonManagedReference
    @OneToMany(mappedBy = "tableSave",
            cascade = {CascadeType.REFRESH, CascadeType.DETACH,
                    CascadeType.MERGE, CascadeType.PERSIST},
            orphanRemoval = true)
    private Set<TableQuery> tableQueries;

    @ManyToMany()
    @JoinTable(name = "tables_reports",
            joinColumns = @JoinColumn(name = "table_id"),
            inverseJoinColumns = @JoinColumn(name = "report_id"))
    private Set<Report> reports;

    public List<ColumnInfoDto> getListColumnInfoDto() {
        return this.getColumnInfos().stream().map(ColumnInfoDto::new).collect(Collectors.toList());
    }

    public TableSave(TableDto tableDto) {
        this.primaryKey = tableDto.getPrimaryKey().toLowerCase();
        this.tableName = tableDto.getTableName();
    }

}
