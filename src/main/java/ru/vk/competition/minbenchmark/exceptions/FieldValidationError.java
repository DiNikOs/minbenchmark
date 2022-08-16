/**
 * @author Ostrovskiy Dmitriy
 * @created 13.08.2022
 * class FieldValidationError
 * @version v1.0
 */

package ru.vk.competition.minbenchmark.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FieldValidationError {

    private String object;
    private String field;
    private Object rejectedValue;
    private String message;

    public FieldValidationError(String object, String message) {
        this.object = object;
        this.message = message;
    }

    @Override
    public String toString() {
        return "\n" + "FieldValidationError{" + "\n" +
                "object='" + object + "," + "\n" +
                "field='" + field + "," + "\n" +
                "rejectedValue=" + rejectedValue + "," + "\n" +
                "message='" + message +
                '}';
    }

}
