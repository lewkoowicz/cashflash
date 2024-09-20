package com.lewkowicz.cashflashapi.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Configuration
public class LocaleResolverConfig extends AcceptHeaderLocaleResolver {

    @Bean
    public LocaleResolver localeResolver() {
        AcceptHeaderLocaleResolver resolver = new AcceptHeaderLocaleResolver();
        resolver.setSupportedLocales(Arrays.asList(Locale.of("en"), Locale.of("pl")));
        resolver.setDefaultLocale(Locale.ENGLISH);
        return resolver;
    }

    @Override
    @NonNull
    public Locale resolveLocale(HttpServletRequest request) {
        String headerLang = request.getHeader("Accept-Language");
        return headerLang != null && !headerLang.isEmpty() ? Locale.lookup(Locale.LanguageRange.parse(headerLang), supportedLocales()) : Objects.requireNonNull(getDefaultLocale());
    }

    private List<Locale> supportedLocales() {
        return Arrays.asList(Locale.of("en"), Locale.of("pl"));
    }
}
