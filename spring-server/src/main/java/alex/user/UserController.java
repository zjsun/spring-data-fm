package alex.user;

import alex.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

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
    public JsonResult<Page<User>> query(@RequestParam(required = false, defaultValue = "") String name, @ApiIgnore Pageable pageable) {
        UserQuery query = new UserQuery();
        query.setQ("%" + StringUtils.defaultString(name) + "%");
        return JsonResult.data(userService.query(query, pageable));
    }
}
