/**
 * @author Ostrovskiy Dmitriy
 * @created 13.08.2022
 * class ColumnInfo
 * @version v1.0
 */

package ru.vk.competition.minbenchmark.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.springframework.validation.annotation.Validated;
import ru.vk.competition.minbenchmark.dto.ColumnInfoDto;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Objects;

@Validated
@Entity
@Table(name = "column_info")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ColumnInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "column_info_id")
    private Long id;

    @Pattern(regexp = "^[_A-Za-z0-9]+$")
    @NotBlank(message = "Title column must not be null!")
    private String title;

    @Pattern(regexp = "^[_A-Za-z0-9()]+$")
    @NotBlank(message = "Type column must not be null!")
    private String type;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY,
            optional = false,
            cascade=CascadeType.ALL)
    @JoinColumn(name = "table_id", nullable=false)
    private TableSave tableSave;

    public ColumnInfo(ColumnInfoDto columnInfoDto) {
        this.title = columnInfoDto.getTitle();
        this.type = columnInfoDto.getType();
    }

    public ColumnInfo(ColumnInfoDto columnInfoDto, TableSave tableSave) {
        this.title = columnInfoDto.getTitle();
        this.type = columnInfoDto.getType();
        this.tableSave = tableSave;
    }

}
