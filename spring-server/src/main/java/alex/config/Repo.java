package alex.config;

import alex.util.Snowflake;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.annotation.Id;
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing;
import org.springframework.data.relational.core.mapping.event.BeforeSaveEvent;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;
import org.springframework.util.ReflectionUtils;

/**
 * @author Alex.Sun
 * @created 2020-08-08 10:08
 */
@Slf4j
@Configuration
@EnableJdbcAuditing
public class Repo {

    @Bean
    public ApplicationListener<BeforeSaveEvent> idGenerator() {
        return event -> {
            Object entity = event.getEntity();
            ReflectionUtils.doWithFields(entity.getClass(), field -> {
                ReflectionUtils.makeAccessible(field);
                if (field.isAnnotationPresent(Id.class)) {
                    if ((field.getType().isAssignableFrom(long.class) && (long) field.get(entity) == 0)
                            || (field.getType().isAssignableFrom(Long.class) && (field.get(entity) == null || ((Long) field.get(entity)).longValue() == 0))) {
                        field.set(entity, Snowflake.DEFAULT.nextId());
                    }
                }
            });
        };
    }

    @Bean
    public PageableHandlerMethodArgumentResolverCustomizer customizerPageable() {
        return resolver -> {
            resolver.setPageParameterName("pi");
            resolver.setSizeParameterName("ps");
        };
    }
}
