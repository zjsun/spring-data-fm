package alex.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Alex.Sun
 * @created 2020-08-09 15:15
 */
@ApiModel("联系信息")
@Getter
@Setter
public class Contact implements Serializable {
    private static final long serialVersionUID = 1;

    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("邮编")
    private String zipCode;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("QQ号")
    private String qq;

}
