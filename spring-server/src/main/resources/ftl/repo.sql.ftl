<#macro set>
    <@dynamic prepend="set">
        <#nested />
    </@dynamic>
</#macro>

<#macro column active=true op=",">
    <@repo.if condition=active prepend=op>
        <#nested />
    </@repo.if>
</#macro>

<#macro where>
    <@dynamic prepend="where">
        <#nested />
    </@dynamic>
</#macro>

<#-- @ftlvariable name="_params" type="java.util.Map" -->
<#-- @ftlvariable name="_p_count" type="int" -->
<#macro param value>
    <@compress single_line=true>
        <#if _p_count?has_content>
            <#assign _p_count=_p_count+1 />
        <#else>
            <#assign _p_count=0 />
        </#if>
        <#local pName='_p_' + _p_count />
        <#local ignore=_params?api.put(pName, value)??>
        :${pName}
    </@compress>
</#macro>

<#macro term active=true op="and">
    <@repo.if condition=active prepend=op>
        <#nested />
    </@repo.if>
</#macro>

<#macro and active=true>
    <@repo.term active=active op="and">
        <#nested />
    </@repo.term>
</#macro>

<#macro or active=true>
    <@repo.term active=active op="or">
        <#nested />
    </@repo.term>
</#macro>

<#-- @ftlvariable name="_dynamic" type="java.lang.Object" -->
<#-- @ftlvariable name="_first" type="java.lang.Boolean" -->
<#-- @ftlvariable name="_execute" type="java.lang.Boolean" -->
<#macro dynamic prepend=false>
    <#assign _execute=false/>
    <#if _first?has_content&&_first&&!(_dynamic?is_boolean&&_dynamic==false)>
        <#local prepend=_dynamic?string! />
    </#if>
    <#local _dynamic_temp=_dynamic!false />
    <#assign _dynamic=prepend />
    <#assign _first=true />
    <#nested />
    <#assign _dynamic=_dynamic_temp />
    <#assign _execute=true/>
</#macro>

<#macro if condition prepend="">
    <#if condition>
        <#assign _execute=false/>
        <#if _first?has_content&&_first>
            ${(_dynamic?is_boolean&&_dynamic==false)?string(prepend,_dynamic?string!)}
        <#else>
            ${prepend}
        </#if>
        <#local _dynamic_temp=_dynamic!false />
        <#assign _dynamic=false />
        <#assign _first=true />
        <#nested />
        <#assign _first=false />
        <#assign _dynamic=_dynamic_temp />
        <#assign _execute=true/>
    <#else>
        <#assign _execute=false/>
    </#if>
</#macro>

<#macro foreach collection prepend="" open="" close="" conjunction="" removeFirstPrepend=false>
    <#list collection>
        <#assign _execute=false/>
        <#if _first?has_content&&_first>
            ${(_dynamic?is_boolean&&_dynamic==false)?string(prepend,_dynamic?string!)}
        <#else>
            ${prepend}
        </#if>
        ${open}
        <#local _dynamic_temp=_dynamic!false />
        <#if removeFirstPrepend>
            <#assign _dynamic='' />
        <#else>
            <#assign _dynamic=false />
        </#if>
        <#items as item>
            <#if _execute>
                <#assign _dynamic=false />
            </#if>
            <#assign _first=true />
            <#nested item item?index/>
            <#sep>${conjunction}
        </#items>
        <#assign _first=false />
        <#assign _dynamic=_dynamic_temp />
        ${close}
        <#assign _execute=true/>
    <#else>
        <#assign _execute=false/>
    </#list>
</#macro>