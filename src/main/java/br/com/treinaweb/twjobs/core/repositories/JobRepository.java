package br.com.treinaweb.twjobs.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.treinaweb.twjobs.core.models.Job;

public interface JobRepository extends JpaRepository<Job, Long> {

    boolean existsByCompanyEmailAndId(String companyEmail, Long id);
    
}
