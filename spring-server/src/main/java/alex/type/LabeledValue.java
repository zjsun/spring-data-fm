package alex.type;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author Alex.Sun
 * @created 2020-08-09 13:07
 */
public interface LabeledValue<V extends Number> extends Valued<V> {

    @ApiModelProperty("Label")
    String getLabel();

    @JsonIgnore
    default int getValueInt() {
        return getValue() == null ? 0 : getValue().intValue();
    }

    @JsonIgnore
    default long getValueLong() {
        return getValue() == null ? 0 : getValue().longValue();
    }
}
