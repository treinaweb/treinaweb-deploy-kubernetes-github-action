package br.com.treinaweb.twjobs.api.skills.mappers;

import br.com.treinaweb.twjobs.api.skills.dtos.SkillRequest;
import br.com.treinaweb.twjobs.api.skills.dtos.SkillResponse;
import br.com.treinaweb.twjobs.core.models.Skill;

public interface SkillMapper {

    Skill toSkill(SkillRequest skillRequest);
    SkillResponse toSkillResponse(Skill skill);
    
}
