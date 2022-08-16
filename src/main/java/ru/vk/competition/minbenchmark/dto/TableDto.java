/**
 * @author Ostrovskiy Dmitriy
 * @created 13.08.2022
 * class TableDto
 * @version v1.0
 */

package ru.vk.competition.minbenchmark.dto;

import lombok.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import ru.vk.competition.minbenchmark.entity.ColumnInfo;
import ru.vk.competition.minbenchmark.entity.TableSave;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Validated
@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class TableDto {

  @Pattern(regexp = "^[_A-Za-z0-9]+$")
  @NotBlank(message = "Table must not be null!")
  private String tableName;

  @PositiveOrZero(message = "Columns Amount should be positive or zero!")
  @NotNull(message = "Columns Amount must not be null!")
  private Integer columnsAmount;

  @Pattern(regexp = "^[_A-Za-z0-9]+$")
  @NotNull
  @NotBlank(message = "Primary Key must not be null!")
  private String primaryKey;

  @NotEmpty(message = "Column Infos must not be null!")
  @Valid
  private List<ColumnInfoDto> columnInfos;

  @AssertTrue(message = "Columns Amount is not equal List Column Infos!")
  private boolean isValid() throws HttpMediaTypeNotAcceptableException {
    boolean isNotEquals = columnsAmount == columnInfos.size();
    if (!isNotEquals) {
      throw new HttpMediaTypeNotAcceptableException("Columns Amount is not equal List Column Infos!");
    }
    return isNotEquals;
  }

  public TableDto(TableSave table) {
    this.tableName = table.getTableName();
    this.columnsAmount = table.getColumnInfos().size();
    this.primaryKey = table.getPrimaryKey();
    this.columnInfos = table.getListColumnInfoDto();
  }

  public static List<ColumnInfo> getListColumnInfo(TableDto tableDto, TableSave tableSave) {
    return tableDto.getColumnInfos().stream().map(column -> new ColumnInfo(column, tableSave)).collect(Collectors.toList());
  }

  @AssertTrue(message = "Column Infos not Valid!")
  private boolean isNotDuplicate() throws HttpMediaTypeNotAcceptableException {
    List<String> titles = columnInfos.stream().map(c -> c.getTitle()).collect(Collectors.toList());
    boolean isNotDuplicate = titles.stream().allMatch(new HashSet<>()::add);
    if (!isNotDuplicate) {
      throw new HttpMediaTypeNotAcceptableException("Column Infos is not equal name Column!");
    }
    if (!titles.contains(primaryKey)) {
      throw new HttpMediaTypeNotAcceptableException("PrimaryKey is not equal title column!");
    }
    return isNotDuplicate;
  }

}
