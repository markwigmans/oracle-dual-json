package com.btb.odj.service.provider;

import com.btb.odj.service.ESDataService;
import com.btb.odj.service.JPADataService;
import com.btb.odj.service.MongoDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.List;
import java.util.Optional;

@Slf4j
public class ProviderCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        ProviderProperties config = Binder.get(context.getEnvironment()).bind("odj.db",ProviderProperties.class).orElse(null);
        if (config != null && config.getProviders() != null) {
            Optional<Object> annotatedClass = metadata.getAnnotations().stream()
                    .map(a -> a.getSource())
                    .distinct()
                    .findAny();
            if (annotatedClass.isPresent()) {
                if (contains(ESDataService.class, annotatedClass.get(), "se", config.getProviders())) {
                    log.info("'{}' activated", annotatedClass.get());
                    return true;
                }
                if (contains(JPADataService.class, annotatedClass.get(), "jpa", config.getProviders())) {
                    log.info("'{}' activated", annotatedClass.get());
                    return true;
                }
                if (contains(MongoDataService.class, annotatedClass.get(), "mongo", config.getProviders())) {
                    log.info("'{}' activated", annotatedClass.get());
                    return true;
                } else {
                    log.info("Class '{}' NOT activated ", annotatedClass.get());
                }
            }
            else {
                return false;
            }
            return false;
        } else {
            return false;
        }
    }

    boolean contains(Class<?> p, Object annotatedClass, String value, List<String> providers) {
        final String clazz = annotatedClass.toString();
        final String provider = p.getCanonicalName();
        if (clazz.compareToIgnoreCase(provider) == 0) {
            return providers.stream().anyMatch(value::equalsIgnoreCase);
        } else {
            return false;
        }
    }
}
