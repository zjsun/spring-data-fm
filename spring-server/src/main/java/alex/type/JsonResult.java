package alex.type;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * Created by sun on 11/7/16.
 */
@ApiModel
@Getter
@Builder
public class JsonResult<T> implements Serializable {
    private static final long serialVersionUID = 1;
    private static final int OK = 0;

    @ApiModelProperty
    private int code;

    @ApiModelProperty
    @Builder.Default
    private String msg = StringUtils.EMPTY;

    @ApiModelProperty
    private T data;

    private JsonResult(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static JsonResult<?> ok() {
        return JsonResult.builder().code(OK).build();
    }


    public static JsonResult<?> error(int code, String msg) {
        return JsonResult.builder().code(code).msg(msg).build();
    }

    public static <T> JsonResult<T> data(T data) {
        return JsonResult.<T>builder().code(OK).data(data).build();
    }

    public static <T> JsonResult<Page<T>> page(org.springframework.data.domain.Page<T> page){
        return JsonResult.<Page<T>>builder().code(OK).data(Page.of(page)).build();
    }

    //TODO: JsonResult.page()
}
