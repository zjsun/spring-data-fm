# spring-data-fm
A freemarker extension for spring-data to solve complex query scenarios.

## Key Features
#### 1. Dynamic sql/hql/jpql rendered with freemarker, which is type-safe.
`user/query.sql.ftl`:
```sql
<#-- @ftlvariable name="query" type="alex.user.UserQuery" -->
select u.* from t_user u

<@repo.where>
    <@repo.term active=query.q?has_content>
        u.name like <@repo.param value=query.q />
    </@repo.term>
    <@repo.term active=query.gender?has_content>
        u.gender = <@repo.param value=query.gender.valueInt />
    </@repo.term>
</@repo.where>

order by u.id desc
```

#### 2. Declarative query method in `Spring data repository`.
`UserRepo.java`:
```java
public interface UserRepo extends PagingAndSortingRepository<User, Long> {

    @Query(name = "user/query")
    Page<User> query(UserQuery query, Pageable pageable);

}
``` 

#### 3. Support for entity or POJO, with nested properties.
`User.java`:
```java
public class User implements Serializable {
    private static final long serialVersionUID = 1;

    ...

    private Contact contact;
    
    ...

}
```

`Contact.java`:
```java
public class Contact implements Serializable {
    private static final long serialVersionUID = 1;

    private String address;

    private String zipCode;

    private String email;

    private String mobile;

    private String qq;

}
```

#### 4. Enjoy.