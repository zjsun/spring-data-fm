package alex.config;

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

    // only for test
    private int count = 100;

    @Bean
    public ApplicationListener<BeforeSaveEvent> idGenerator() {
        return event -> {
            Object entity = event.getEntity();
            ReflectionUtils.doWithFields(entity.getClass(), field -> {
                ReflectionUtils.makeAccessible(field);
                if (field.isAnnotationPresent(Id.class) && field.getType().isAssignableFrom(long.class)) {
                    long id = (long) field.get(entity);
                    if (id == 0)
                        field.set(entity, count++);
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
