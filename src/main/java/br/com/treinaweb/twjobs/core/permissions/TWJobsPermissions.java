package br.com.treinaweb.twjobs.core.permissions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.security.access.prepost.PreAuthorize;

public @interface TWJobsPermissions {

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("""
        hasAuthority('COMPANY') and 
        @securityService.isOwner(principal.username, #id)
    """)
    public @interface IsOwner {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("hasAuthority('COMPANY')")
    public @interface IsCompany {}
    
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("hasAuthority('CANDIDATE')")
    public @interface IsCandidate {}
    
}
