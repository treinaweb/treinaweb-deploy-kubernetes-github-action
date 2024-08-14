package br.com.treinaweb.twjobs.api.skills.dtos;

import br.com.treinaweb.twjobs.core.validators.SkillNameIsUnique;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SkillRequest {

    @NotEmpty
    @SkillNameIsUnique
    private String name;
    
}
