package br.com.treinaweb.twjobs.api.jobs.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.treinaweb.twjobs.api.skills.dtos.SkillResponse;
import br.com.treinaweb.twjobs.api.skills.mappers.SkillMapper;
import br.com.treinaweb.twjobs.core.exceptions.JobNotFoundException;
import br.com.treinaweb.twjobs.core.repositories.JobRepository;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/jobs/{id}/skills")
public class JobSkillsRestController {

    private final SkillMapper skillMapper;
    private final JobRepository jobRepository;

    @GetMapping
    public List<SkillResponse> findSkillsByJobId(@PathVariable Long id) {
        var job = jobRepository.findById(id)
            .orElseThrow(JobNotFoundException::new);
        return job.getSkills()
            .stream()
            .map(skillMapper::toSkillResponse)
            .toList();
    }
    
}
