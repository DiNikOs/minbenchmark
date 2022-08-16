/**
 * @author Ostrovskiy Dmitriy
 * @created 13.08.2022
 * interface TableQueryRepository
 * @version v1.0
 */

package ru.vk.competition.minbenchmark.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.vk.competition.minbenchmark.dto.TableQueryDto;
import ru.vk.competition.minbenchmark.entity.TableQuery;

import java.util.concurrent.Callable;

@Repository
public interface TableQueryRepository extends CrudRepository<TableQuery, Long> {

    @Query("select new ru.vk.competition.minbenchmark.dto.TableQueryDto(t) from TableQuery t where t.tableName = :name")
    Iterable<TableQueryDto> findByTableName(@Param("name") String name);

    @Query("select new ru.vk.competition.minbenchmark.dto.TableQueryDto(t) from TableQuery t")
    Iterable<TableQueryDto> findAllQuery();
}
