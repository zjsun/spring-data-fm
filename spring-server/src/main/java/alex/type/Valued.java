package alex.type;

import io.swagger.annotations.ApiModelProperty;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author Alex.Sun
 * @created 2020-08-09 13:05
 */
public interface Valued<V extends Number> {

    @ApiModelProperty("Value")
    V getValue();

    static <T extends Valued<V>, V extends Number> T findByValue(Class<T> enumType, V value, T def) {
        return Arrays.stream(enumType.getEnumConstants()).filter(e -> Objects.equals(e.getValue(), value)).findFirst().orElse(def);
    }

}
