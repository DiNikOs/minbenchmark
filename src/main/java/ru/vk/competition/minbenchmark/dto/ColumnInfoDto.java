/**
 * @author Ostrovskiy Dmitriy
 * @created 13.08.2022
 * class ColumnInfoDto
 * @version v1.0
 */

package ru.vk.competition.minbenchmark.dto;

import lombok.*;
import org.springframework.validation.annotation.Validated;
import ru.vk.competition.minbenchmark.entity.ColumnInfo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Validated
@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class ColumnInfoDto {

    @Pattern(regexp = "^[_A-Za-z0-9]+$")
    @NotNull
    @NotBlank(message = "Title column must not be null!")
    private String title;

    @Pattern(regexp = "^[_A-Za-z0-9()]+$")
    @NotNull
    @NotBlank(message = "Type column must not be null!")
    private String type;

    public ColumnInfoDto(ColumnInfo columnInfo) {
        this.title = columnInfo.getTitle().toUpperCase();
        this.type = columnInfo.getType().toUpperCase();
    }

    public ColumnInfoDto(@Pattern(regexp = "^[_A-Za-z0-9]+$") @NotNull @NotBlank(message = "Title column must not be null!")
                                 String title,
                         @Pattern(regexp = "^[_A-Za-z0-9()]+$") @NotNull @NotBlank(message = "Type column must not be null!")
                                 String type) {
        this.title = title.toUpperCase();
        this.type = type.toUpperCase();
    }

    public List<ColumnInfo> getListColumnInfo(List<ColumnInfoDto> columnInfoDtoList) {
        return columnInfoDtoList.stream().map(column -> new ColumnInfo(column)).collect(Collectors.toList());
    }

}
