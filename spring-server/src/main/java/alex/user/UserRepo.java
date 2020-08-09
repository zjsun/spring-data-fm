package alex.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * @author Alex.Sun
 * @created 2020-08-07 15:52
 */
public interface UserRepo extends PagingAndSortingRepository<User, Long> {
    List<User> findByNameLikeOrderById(String name, Pageable pageable);
    long countByNameLike(String name);
}
