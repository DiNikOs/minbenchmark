/**
 * @author Ostrovskiy Dmitriy
 * @created 13.08.2022
 * interface ReportRepository
 * @version v1.0
 */

package ru.vk.competition.minbenchmark.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.vk.competition.minbenchmark.entity.Report;
import ru.vk.competition.minbenchmark.entity.TableSave;

import java.util.List;
import java.util.Set;


@Repository
public interface ReportRepository extends CrudRepository<Report, Long> {

    Set<Report> findAllByReportId(@Param("reportId") Long reportId);
}
