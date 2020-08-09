package alex.user;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Alex.Sun
 * @created 2020-08-07 16:06
 */
@Getter
@Setter
public class UserQuery implements Serializable {
    private static final long serialVersionUID = 1;

    /**
     * keyword for search
     */
    private String q;

    private User.Gender gender;
}
