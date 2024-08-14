package br.com.treinaweb.twjobs.config;

import java.util.List;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.treinaweb.twjobs.api.jobs.dtos.JobRequest;
import br.com.treinaweb.twjobs.api.jobs.dtos.JobResponse;
import br.com.treinaweb.twjobs.core.models.Job;
import br.com.treinaweb.twjobs.core.models.Skill;
import br.com.treinaweb.twjobs.core.repositories.SkillRepository;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ModelMapperConfig {

    private final SkillRepository skillRepository;

    @Bean
    ModelMapper modelMapper() {
        var modelmapper = new ModelMapper();

        modelmapper.createTypeMap(JobRequest.class, Job.class)
            .addMappings(mapper -> mapper
                .using(toListOfSkills())
                .map(JobRequest::getSkills, Job::setSkills)
            );

        modelmapper.createTypeMap(Job.class, JobResponse.class)
            .addMappings(mapper -> mapper
                .map(src -> src.getCompany().getName(), JobResponse::setCompany)
            );

        return modelmapper;
    }

    private Converter<List<Long>, List<Skill>> toListOfSkills() {
        return context -> skillRepository.findAllById(context.getSource());
    }
    
}
