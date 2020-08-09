package alex;

import alex.util.Version;
import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.builders.*;
import springfox.documentation.schema.AlternateTypeRule;
import springfox.documentation.schema.AlternateTypeRuleConvention;
import springfox.documentation.service.AllowableValues;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.ModelPropertyBuilderPlugin;
import springfox.documentation.spi.schema.contexts.ModelPropertyContext;
import springfox.documentation.spring.web.DescriptionResolver;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.common.SwaggerPluginSupport;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static springfox.documentation.schema.AlternateTypeRules.newRule;
import static springfox.documentation.schema.Annotations.findPropertyAnnotation;
import static springfox.documentation.swagger.schema.ApiModelProperties.allowableValueFromString;
import static springfox.documentation.swagger.schema.ApiModelProperties.findApiModePropertyAnnotation;

/**
 * @author Alex.Sun
 * @created 2020-08-07 17:54
 */
@Configuration
@EnableSwagger2
public class Swagger {

    @Controller
    public static class SwaggerController {
        @RequestMapping("/api")
        public void doc(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            request.getRequestDispatcher("/doc.html").forward(request, response);
        }
    }

    @Bean
    public ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("Demo API").version(Version.getVersion()).description("Demo API Doc").contact(new Contact("Demo Team", "https://github.com/zjsun/spring-data-fm", "zjsun@126.com"))
                .build();
    }

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("ALL")
                .apiInfo(apiInfo())
                .select()
                .paths(PathSelectors.ant("/api/**"))
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .build();
    }

    @Bean
    public AlternateTypeRuleConvention pageableConvention(
            final TypeResolver resolver) {
        return new AlternateTypeRuleConvention() {

            @Override
            public int getOrder() {
                return Ordered.HIGHEST_PRECEDENCE;
            }

            @Override
            public List<AlternateTypeRule> rules() {
                return newArrayList(
                        newRule(resolver.resolve(Pageable.class), resolver.resolve(pageableMixin()))
                );
            }
        };
    }

    private Type pageableMixin() {
        return new AlternateTypeBuilder()
                .fullyQualifiedClassName(
                        String.format("%s.generated.%s",
                                Pageable.class.getPackage().getName(),
                                Pageable.class.getSimpleName()))
                .withProperties(newArrayList(
                        property(Integer.class, "pi", "Page Index (zero based)"),
                        property(Integer.class, "ps", "Page Size")
                ))
                .build();
    }

    private AlternateTypePropertyBuilder property(Class<?> type, String name, String title) {
        return new AlternateTypePropertyBuilder()
                .withName(name)
                .withType(type)
                .withCanRead(true)
                .withCanWrite(true).withAnnotations(Lists.newArrayList(createApiParamAnnotation(title)));
    }

    private ApiParam createApiParamAnnotation(String title) {
        return new ApiParam() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return ApiParam.class;
            }

            @Override
            public String name() {
                return "";
            }

            @Override
            public String value() {
                return title;
            }

            @Override
            public String defaultValue() {
                return "";
            }

            @Override
            public String allowableValues() {
                return "";
            }

            @Override
            public boolean required() {
                return false;
            }

            @Override
            public String access() {
                return "";
            }

            @Override
            public boolean allowMultiple() {
                return false;
            }

            @Override
            public boolean hidden() {
                return false;
            }

            @Override
            public String example() {
                return "";
            }

            @Override
            public Example examples() {
                return new Example() {
                    @Override
                    public Class<? extends Annotation> annotationType() {
                        return Example.class;
                    }

                    @Override
                    public ExampleProperty[] value() {
                        return new ExampleProperty[0];
                    }
                };
            }

            @Override
            public String type() {
                return "";
            }

            @Override
            public String format() {
                return "";
            }

            @Override
            public boolean allowEmptyValue() {
                return false;
            }

            @Override
            public boolean readOnly() {
                return false;
            }

            @Override
            public String collectionFormat() {
                return "";
            }
        };
    }

    @Configuration
    @Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER)
    public static class SlimModelPropertyBuilderPlugin implements ModelPropertyBuilderPlugin {

        @Autowired
        private DescriptionResolver descriptions;


        @Override
        public void apply(ModelPropertyContext context) {
            Optional<ApiModelProperty> annotation = Optional.absent();

            if (context.getAnnotatedElement().isPresent()) {
                annotation = annotation.or(findApiModePropertyAnnotation(context.getAnnotatedElement().get()));
            }
            if (context.getBeanPropertyDefinition().isPresent()) {
                annotation = annotation.or(findPropertyAnnotation(
                        context.getBeanPropertyDefinition().get(),
                        ApiModelProperty.class));
            }
            if (annotation.isPresent()) {
                context.getBuilder()
                        .allowableValues(annotation.transform(toAllowableValues()).orNull())
                        .required(annotation.transform(toIsRequired()).or(false))
                        .readOnly(annotation.transform(toIsReadOnly()).or(false))
                        .description(annotation.transform(toDescription(descriptions)).orNull())
                        .isHidden(annotation.transform(toHidden()).or(false))
                        .type(annotation.transform(toType(context.getResolver())).orNull())
                        .position(annotation.transform(toPosition()).or(0))
                        .example(annotation.transform(toExample()).orNull());
            } else {
                context.getBuilder().isHidden(true);
            }
        }

        @Override
        public boolean supports(DocumentationType delimiter) {
            return SwaggerPluginSupport.pluginDoesApply(delimiter);
        }

        static Function<ApiModelProperty, AllowableValues> toAllowableValues() {
            return annotation -> allowableValueFromString(annotation.allowableValues());
        }

        static Function<ApiModelProperty, Boolean> toIsRequired() {
            return annotation -> annotation.required();
        }

        static Function<ApiModelProperty, Integer> toPosition() {
            return annotation -> annotation.position();
        }

        static Function<ApiModelProperty, Boolean> toIsReadOnly() {
            return annotation -> annotation.readOnly();
        }

        static Function<ApiModelProperty, Boolean> toAllowEmptyValue() {
            return annotation -> annotation.allowEmptyValue();
        }

        static Function<ApiModelProperty, String> toDescription(
                final DescriptionResolver descriptions) {

            return annotation -> {
                String description = "";
                if (!Strings.isNullOrEmpty(annotation.value())) {
                    description = annotation.value();
                } else if (!Strings.isNullOrEmpty(annotation.notes())) {
                    description = annotation.notes();
                }
                return descriptions.resolve(description);
            };
        }

        static Function<ApiModelProperty, ResolvedType> toType(final TypeResolver resolver) {
            return annotation -> {
                try {
                    return resolver.resolve(Class.forName(annotation.dataType()));
                } catch (ClassNotFoundException e) {
                    return resolver.resolve(Object.class);
                }
            };
        }

        static Function<ApiModelProperty, Boolean> toHidden() {
            return annotation -> annotation.hidden();
        }

        static Function<ApiModelProperty, String> toExample() {
            return annotation -> {
                String example = "";
                if (!Strings.isNullOrEmpty(annotation.example())) {
                    example = annotation.example();
                }
                return example;
            };
        }
    }
}
