/**
 * @author Ostrovskiy Dmitriy
 * @created 13.08.2022
 * class TableQuareConstructor
 * @version v1.0
 */

package ru.vk.competition.minbenchmark.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import ru.vk.competition.minbenchmark.dto.ColumnInfoDto;
import ru.vk.competition.minbenchmark.dto.TableDto;
import ru.vk.competition.minbenchmark.entity.Report;
import ru.vk.competition.minbenchmark.entity.TableSave;
import ru.vk.competition.minbenchmark.service.ITableQueryConstructor;

import java.sql.*;
import java.util.*;

import static ru.vk.competition.minbenchmark.dto.TableDto.getListColumnInfo;

@Slf4j
@Service
@RequiredArgsConstructor
public class TableQueryConstructorImpl implements ITableQueryConstructor {

    @Value("${spring.datasource.driverClassName}")
    private String driverClassName;

    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    @Value("${spring.datasource.username}")
    private String datasourceUsername;

    @Value("${spring.datasource.password}")
    private String datasourcePassword;

    final String CREATE_TABLE_QUERY = "CREATE TABLE %s (%s PRIMARY KEY (%s))";
    final String INSERT_QUERY = "INSERT INTO %s (%s) VALUES (%s)";
    final String SELECT_COUNT_ROW_QUERY = "SELECT count(*) from %s";
    final String DELETE_TABLE_QUERY = "DROP TABLE IF EXISTS %s";

    private final TableRepository tableRepository;
    private final ReportRepository reportRepository;
    private final NamedParameterJdbcTemplate template;

    @Override
    public TableSave createTable(TableDto tableDto) throws HttpMediaTypeNotAcceptableException {
        if (tableRepository.existsByTableName(tableDto.getTableName())) {
            log.info("Table already exists with this name: {}!", tableDto.getTableName());
            throw new HttpMediaTypeNotAcceptableException("Table already exists with this name: " + tableDto.getTableName());
        }
        String sql = String.format(CREATE_TABLE_QUERY, tableDto.getTableName(),
                getFields(tableDto.getColumnInfos()), tableDto.getPrimaryKey().toLowerCase());
        log.info("SQL QUERY: {}", sql);
        try {
            int isCreateTable = template.update(sql, new HashMap<>());
            if (isCreateTable == 0) {
                TableSave tableSave = tableRepository.save(new TableSave(tableDto));
                tableSave.setColumnInfos(getListColumnInfo(tableDto, tableSave));
                tableSave.setTableQueries(new HashSet<>());
                tableSave.setReports(new HashSet<>());
                return tableRepository.save(tableSave);
            }
        } catch (Exception ex) {
            log.info("Error writing to the database!",ex.getMessage());
            throw new HttpMediaTypeNotAcceptableException("Error writing to the database!" + ex.getMessage());
        }
        return null;
    }

    @Override
    public ResponseEntity<Void> executeQuery(Optional<String> stringOptional) {
        try {
            Connection connection = getConnect();
            Statement statement = connection.createStatement();
            statement.execute(stringOptional.get());
            statement.close();
            connection.close();
            return new ResponseEntity<Void>(HttpStatus.CREATED);
        } catch (Exception e) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            return new ResponseEntity<Void>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @Override
    public int dropTableIfExists(String name) {
        String sql = String.format(DELETE_TABLE_QUERY, name);
        int isDeleteTable = template.update(sql, new HashMap<>());
        return isDeleteTable;
    }

    @Override
    public Map<TableSave, Integer> getSizeRowTables(Set<TableSave> tables) {
        Map<TableSave, Integer> mapTables = null;
        try {
            Connection connection = getConnect();
            Statement statement =  connection.createStatement();
            mapTables = setMapSizeByTable(statement, tables);
            statement.close();
            connection.close();
            return mapTables;
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return mapTables;
    }

    @Transactional
    @Override
    public Map<TableSave, Integer> getSizeRowTables(Long id) throws HttpMediaTypeNotAcceptableException {
        Report report = reportRepository.findById(id.longValue()).orElseThrow(() ->
                new HttpMediaTypeNotAcceptableException(String.format("Cannot find Report by Id %s", id.toString())
                ));
        return getSizeRowTables(report);
    }

    @Transactional
    @Override
    public Map<TableSave, Integer> getSizeRowTables(Report report) {
        Set<TableSave> tables = report.getTables();
        return getSizeRowTables(tables);
    }

    private Map<TableSave, Integer> setMapSizeByTable(Statement statement, Set<TableSave> tables) throws SQLException {
        Map<TableSave, Integer> mapTables = new HashMap<>();
        for (TableSave table : tables) {
            ResultSet resultSet = statement.executeQuery(String.format(SELECT_COUNT_ROW_QUERY, table.getTableName()));
            ResultSetMetaData metaData = resultSet.getMetaData();
            //getting the column type
            int columnCount = metaData.getColumnCount();
            mapTables.put(table, columnCount);
            log.debug("Row size: {} in table: {}", resultSet.getRow(), table.getTableName());
        }
        return mapTables;
    }

    private Connection getConnect() throws SQLException, ClassNotFoundException {
        Connection connection;
        Class.forName(driverClassName);
        connection = DriverManager.getConnection(
                datasourceUrl,
                datasourceUsername,
                datasourcePassword
        );
        log.debug("Database connected....");
        return connection;
    }

    private String getFields(List<ColumnInfoDto> columnInfoDtos) {
        StringBuilder stringBuilder = new StringBuilder();
        for (ColumnInfoDto column : columnInfoDtos) {
            StringBuffer buffer = new StringBuffer();
            buffer.append(column.getTitle());
            buffer.append(' ');
            buffer.append(column.getType());
            buffer.append(',');
            stringBuilder.append(buffer);
        }
        return String.valueOf(stringBuilder);
    }

}
