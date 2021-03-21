package com.chs.appconfiguration;

import com.chs.appconfiguration.utils.ApplicationSecurityContext;
import com.chs.user.authentication.domain.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaAuditingConfiguration {

    @Autowired
    private ApplicationSecurityContext securityContext;

    @Bean
    public AuditorAware<AppUser> auditorProvider() {
        return () -> Optional.ofNullable(securityContext.authenticatedUser());
    }
}