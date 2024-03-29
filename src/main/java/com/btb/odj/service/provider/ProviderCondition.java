package com.btb.odj.service.provider;

import static com.btb.odj.util.Provider.*;

import com.btb.odj.service.ESDataService;
import com.btb.odj.service.JPADataService;
import com.btb.odj.service.MongoDataService;
import com.btb.odj.util.Provider;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

@Slf4j
public class ProviderCondition implements Condition {

    // the condition will be called multiple times, prevent logging multiple times.
    static final Map<String, Boolean> LOGGED;
    static final Map<String, Provider> IDENTIFIERS;

    static {
        List<Class<?>> serviceClasses = List.of(ESDataService.class, JPADataService.class, MongoDataService.class);
        List<Provider> ids = List.of(ElasticSearch, JPA, MongoDB);
        LOGGED = serviceClasses.stream().collect(Collectors.toMap(Class::getCanonicalName, value -> false));

        IDENTIFIERS = new HashMap<>();
        AtomicInteger index = new AtomicInteger();
        serviceClasses.forEach(clazz -> {
            int currentIndex = index.getAndIncrement();
            IDENTIFIERS.put(clazz.getCanonicalName(), ids.get(currentIndex));
        });
    }

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        Environment env = context.getEnvironment();
        ProviderProperties config =
                Binder.get(env).bind("odj.db", ProviderProperties.class).orElse(null);

        if (config == null || config.getProviders() == null) {
            return false;
        }

        return metadata.getAnnotations().stream()
                .map(a -> Objects.toString(a.getSource()))
                .distinct()
                .findAny()
                .map(clazz -> {
                    log.debug("Validate: '{}'", clazz);

                    if (contains(clazz, config.getProviders())) {
                        if (!LOGGED.get(clazz)) {
                            log.info("'{}' activated", clazz);
                            LOGGED.put(clazz, true);
                        }
                        return true;
                    }
                    return false;
                })
                .orElse(false);
    }

    boolean contains(String clazz, Collection<Provider> providers) {
        final Provider provider = IDENTIFIERS.get(clazz);
        return providers.contains(provider);
    }
}
