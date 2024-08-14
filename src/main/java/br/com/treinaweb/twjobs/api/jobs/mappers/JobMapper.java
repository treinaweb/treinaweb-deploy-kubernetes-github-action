package br.com.treinaweb.twjobs.api.jobs.mappers;

import br.com.treinaweb.twjobs.api.jobs.dtos.JobRequest;
import br.com.treinaweb.twjobs.api.jobs.dtos.JobResponse;
import br.com.treinaweb.twjobs.core.models.Job;

public interface JobMapper {

    JobResponse toJobResponse(Job job);
    Job toJob(JobRequest jobRequest);
    
}
