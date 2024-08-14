package br.com.treinaweb.twjobs.api.jobs.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.treinaweb.twjobs.core.exceptions.JobNotFoundException;
import br.com.treinaweb.twjobs.core.permissions.TWJobsPermissions;
import br.com.treinaweb.twjobs.core.repositories.JobRepository;
import br.com.treinaweb.twjobs.core.services.auth.SecurityService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/jobs/{id}")
public class CanidateRestController {

    private final JobRepository jobRepository;
    private final SecurityService securityService;

    @PostMapping("/apply")
    @TWJobsPermissions.IsCandidate
    public ResponseEntity<?> apply(@PathVariable Long id) {
        var job = jobRepository.findById(id)
            .orElseThrow(JobNotFoundException::new);
        job.getCandidates().add(securityService.getCurrentUser());
        jobRepository.save(job);
        return ResponseEntity.noContent().build();
    }
    
}
