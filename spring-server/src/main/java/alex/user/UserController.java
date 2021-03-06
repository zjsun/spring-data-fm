package alex.user;

import alex.type.JsonResult;
import alex.type.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

/**
 * @author Alex.Sun
 * @created 2020-08-07 15:50
 */
@Api(tags = "User")
@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @ApiOperation("Get")
    @GetMapping("/{id}")
    public JsonResult<User> get(@PathVariable long id) {
        return JsonResult.data(userService.get(id));
    }

    @ApiOperation("Save")
    @PostMapping("/save")
    public JsonResult<User> save(@RequestBody User user) {
        return JsonResult.data(userService.save(user));
    }

    @ApiOperation("Query")
    @GetMapping("/query")
    public JsonResult<Page<User>> query(@RequestParam(required = false, defaultValue = "") String name, Pageable pageable) {
        UserQuery query = new UserQuery();
        query.setQ(StringUtils.defaultString(name));
        return JsonResult.page(userService.query(query, pageable));
    }
}
