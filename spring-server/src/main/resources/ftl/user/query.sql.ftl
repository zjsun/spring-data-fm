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