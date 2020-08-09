package alex.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author Alex.Sun
 * @created 2020-08-07 15:49
 */
public interface UserService {
    User get(long id);

    User save(User user);

    Page<User> query(UserQuery query, Pageable pageable);
}
