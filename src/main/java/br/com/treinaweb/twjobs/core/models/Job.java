package br.com.treinaweb.twjobs.core.models;

import java.math.BigDecimal;
import java.util.List;

import br.com.treinaweb.twjobs.core.enums.JobLevel;
import br.com.treinaweb.twjobs.core.enums.JobType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Job {

    @Id
    @ToString.Include
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Include
    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false, length = 100)
    private String location;

    @Column(nullable = false, length = 9)
    @Enumerated(EnumType.STRING)
    private JobType type;

    @Column(nullable = false, length = 9)
    @Enumerated(EnumType.STRING)
    private JobLevel level;

    @Column(nullable = false, scale = 2)
    private BigDecimal salary;

    @ManyToMany
    private List<Skill> skills;

    @ManyToOne(optional = false)
    private User company;

    @ManyToMany
    private List<User> candidates;
    
}
