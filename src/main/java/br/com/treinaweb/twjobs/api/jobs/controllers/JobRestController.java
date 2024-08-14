package br.com.treinaweb.twjobs.api.jobs.controllers;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.treinaweb.twjobs.api.jobs.assemblers.JobAssembler;
import br.com.treinaweb.twjobs.api.jobs.dtos.JobRequest;
import br.com.treinaweb.twjobs.api.jobs.dtos.JobResponse;
import br.com.treinaweb.twjobs.api.jobs.mappers.JobMapper;
import br.com.treinaweb.twjobs.core.exceptions.JobNotFoundException;
import br.com.treinaweb.twjobs.core.permissions.TWJobsPermissions;
import br.com.treinaweb.twjobs.core.repositories.JobRepository;
import br.com.treinaweb.twjobs.core.services.auth.SecurityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/jobs")
public class JobRestController {

    private final JobMapper jobMapper;
    private final JobAssembler jobAssembler;
    private final JobRepository jobRepository;
    private final SecurityService securityService;
    private final PagedResourcesAssembler<JobResponse> pagedResourcesAssembler;

    @GetMapping
    public CollectionModel<EntityModel<JobResponse>> findAll(Pageable pageable) {
        var jobs = jobRepository.findAll(pageable)
            .map(jobMapper::toJobResponse);
        return pagedResourcesAssembler.toModel(jobs, jobAssembler);
    }

    @GetMapping("/{id}")
    public EntityModel<JobResponse> findById(@PathVariable Long id) {
        var job = jobRepository.findById(id)
            .orElseThrow(JobNotFoundException::new);
        var jobResponse = jobMapper.toJobResponse(job);
        return jobAssembler.toModel(jobResponse);
    }

    @PostMapping
    @TWJobsPermissions.IsCompany
    @ResponseStatus(code = HttpStatus.CREATED)
    public EntityModel<JobResponse> create(@RequestBody @Valid JobRequest jobRequest) {
        var job = jobMapper.toJob(jobRequest);
        job.setCompany(securityService.getCurrentUser());
        job = jobRepository.save(job);
        var jobResponse = jobMapper.toJobResponse(job);
        return jobAssembler.toModel(jobResponse);
    }

    @PutMapping("/{id}")
    @TWJobsPermissions.IsOwner
    public EntityModel<JobResponse> update(
        @RequestBody @Valid JobRequest jobRequest, 
        @PathVariable Long id
    ) {
        var job = jobRepository.findById(id)
            .orElseThrow(JobNotFoundException::new);
        var jobData = jobMapper.toJob(jobRequest);
        BeanUtils.copyProperties(jobData, job, "id", "company", "candidates");
        job = jobRepository.save(job);
        var jobResponse = jobMapper.toJobResponse(job);
        return jobAssembler.toModel(jobResponse);
    }

    @DeleteMapping("/{id}")
    @TWJobsPermissions.IsOwner
    public ResponseEntity<?> delete(@PathVariable Long id) {
        var job = jobRepository.findById(id)
            .orElseThrow(JobNotFoundException::new);
        jobRepository.delete(job);
        return ResponseEntity.noContent().build();
    }
    
}
