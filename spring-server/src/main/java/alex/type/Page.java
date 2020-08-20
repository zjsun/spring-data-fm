package alex.type;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.List;

/**
 * @author Alex.Sun
 * @created 2020-08-20 15:54
 */
@ApiModel
@Getter
public class Page<T> implements Serializable {
    private static final long serialVersionUID = 1;

    @JsonIgnore
    private org.springframework.data.domain.Page<T> page;

    public static <T> Page<T> of(org.springframework.data.domain.Page<T> page) {
        return new Page(page);
    }

    public Page(org.springframework.data.domain.Page<T> page) {
        Assert.notNull(page, "");
        this.page = page;
    }

    private org.springframework.data.domain.Page getPage() {
        return page;
    }

    @ApiModelProperty("内容列表")
    public List getItems() {
        return page.getContent();
    }

    @ApiModelProperty("总页数")
    public int getTotalPages() {
        return page.getTotalPages();
    }

    @ApiModelProperty("总行数")
    public long getTotal() {
        return page.getTotalElements();
    }

    @ApiModelProperty("当前页码")
    public int getPageIndex() {
        return page.getNumber();
    }

    @ApiModelProperty("分页大小")
    public int getPageSize() {
        return page.getSize();
    }
}
