package com.btb.odj.service.provider;

import com.btb.odj.util.Provider;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
@ConfigurationProperties(prefix = "odj.db")
public class ProviderProperties {

    private List<Provider> providers;

    public void setProviders(List<String> list) {
        this.providers = Arrays.stream(Provider.values())
                .filter(p -> list.contains(p.label))
                .toList();
    }

    public boolean isActive(Provider provider) {
        return providers.contains(provider);
    }
}
