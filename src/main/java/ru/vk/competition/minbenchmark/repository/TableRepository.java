/**
 * @author Ostrovskiy Dmitriy
 * @created 13.08.2022
 * interface TableRepository
 * @version v1.0
 */

package ru.vk.competition.minbenchmark.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.vk.competition.minbenchmark.dto.TableDto;
import ru.vk.competition.minbenchmark.entity.Report;
import ru.vk.competition.minbenchmark.entity.TableSave;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface TableRepository extends CrudRepository<TableSave, Long> {

    @Query("select new ru.vk.competition.minbenchmark.dto.TableDto(t) from TableSave t where t.tableName = :name")
    Optional<TableDto> getByName(@Param("name") String name);

    TableSave findByTableName(String tableName);

    Set<TableSave> findAllByTableNameIn(Iterable<String> tableNames);

    boolean existsByTableName(String tableName);

    TableSave save(TableSave tableSave);

    @Transactional
    void deleteTableSaveByTableId(Long id);

    @Transactional
    void deleteTableSaveByTableName(String tableName);

}
