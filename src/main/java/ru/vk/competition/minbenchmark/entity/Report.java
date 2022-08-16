/**
 * @author Ostrovskiy Dmitriy
 * @created 13.08.2022
 * class Report
 * @version v1.0
 */

package ru.vk.competition.minbenchmark.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "report")
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Report implements Serializable {

    @Id
    @Column(name = "report_id")
    private Long reportId;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "tables_reports",
            joinColumns = @JoinColumn(name = "report_id"),
            inverseJoinColumns = @JoinColumn(name = "table_id"))
    private Set<TableSave> tables;

}
