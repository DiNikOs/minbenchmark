/**
 * @author Ostrovskiy Dmitriy
 * @created 15.08.2022
 * class TableReportRepository
 * @version v1.0
 */

package ru.vk.competition.minbenchmark.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.vk.competition.minbenchmark.entity.TableReport;
import ru.vk.competition.minbenchmark.entity.TableSave;

@Repository
public interface TableReportRepository extends CrudRepository<TableReport, Long> {

}
