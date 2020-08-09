package alex.user;

import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Alex.Sun
 * @created 2020-08-07 17:21
 */
@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepo userRepo;

    @Override
    public User get(long id) {
        return userRepo.findById(id).orElse(null);
    }

    @Transactional
    @Override
    public User save(User user) {
        return userRepo.save(user);
    }

    @Override
    public Page<User> query(UserQuery query, Pageable pageable) {
        return new PageImpl<>(userRepo.findByNameContainingOrderById(query.getQ(), pageable), pageable, userRepo.countByNameContaining(query.getQ()));
    }
}
