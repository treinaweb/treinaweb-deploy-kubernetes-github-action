package br.com.treinaweb.twjobs.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.treinaweb.twjobs.core.models.Skill;

public interface SkillRepository extends JpaRepository<Skill, Long> {

    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Long id);
    
}
