/**
 * @author Ostrovskiy Dmitriy
 * @created 13.08.2022
 * class TableReport
 * @version v1.0
 */

package ru.vk.competition.minbenchmark.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Builder
@Entity
@Table(name = "table_report")
@NoArgsConstructor
@AllArgsConstructor
@IdClass(TableReport.TableReportId.class)
public class TableReport {

    @Id
    @Column(name = "report_id", nullable = false)
    private Long reportId;

    @Id
    @Column(name = "table_id", nullable = false)
    private Long tableId;

    public static TableReportId TableReportId(TableReport tableReport) {
        return new TableReportId(tableReport.getReportId(), tableReport.getTableId());
    }

    @Getter
    @Setter
    @EqualsAndHashCode
    @Access(value = AccessType.FIELD)
    @Embeddable
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TableReportId implements Serializable {
        private Long reportId;
        private Long tableId;
    }

}
