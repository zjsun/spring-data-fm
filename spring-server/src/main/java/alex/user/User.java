package alex.user;

import alex.type.LabeledValue;
import alex.type.Valued;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Embedded;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;

/**
 * @author Alex.Sun
 * @created 2020-08-07 15:48
 */
@ApiModel("用户")
@Getter
@Setter
@Table("T_USER")
public class User implements Serializable {
    private static final long serialVersionUID = 1;

    @ApiModel("性别")
    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    @Getter
    public enum Gender implements LabeledValue<Integer> {
        UNKNOWN(0, "未知"), MALE(1, "男"), FEMALE(2, "女");

        private Integer value;
        private String label;

        Gender(int value, String label) {
            this.value = value;
            this.label = label;
        }

        @JsonCreator
        public static Gender findByValue(@JsonProperty("value") Integer value) {
            return Valued.findByValue(Gender.class, value, UNKNOWN);
        }
    }

    @ApiModelProperty("ID")
    @Id
    private long id;

    @ApiModelProperty("姓名")
    @Column
    private String name;

    @AccessType(AccessType.Type.FIELD)
    @ApiModelProperty("性别")
    @Column
    private int gender;

    public Gender getGender() {
        return Valued.findByValue(Gender.class, gender, Gender.UNKNOWN);
    }

    public void setGender(Gender gender) {
        this.gender = gender == null ? 0 : gender.value;
    }

    @ApiModelProperty("联系方式")
    @Embedded.Empty(prefix = "contact_")
    private Contact contact;

    @ApiModelProperty("创建时间")
    @CreatedDate
    private long createAt;

    @ApiModelProperty("更新时间")
    @LastModifiedDate
    private long updateAt;
}
