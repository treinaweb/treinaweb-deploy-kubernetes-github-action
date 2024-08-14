package br.com.treinaweb.twjobs.api.skills.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;

import br.com.treinaweb.twjobs.api.skills.controllers.SkillRestController;
import br.com.treinaweb.twjobs.api.skills.dtos.SkillResponse;

@Component
public class SkillAssembler implements SimpleRepresentationModelAssembler<SkillResponse> {

    @Override
    public void addLinks(EntityModel<SkillResponse> resource) {
        var id = resource.getContent().getId();

        var selfLink = linkTo(methodOn(SkillRestController.class).findById(id))
            .withSelfRel()
            .withType("GET");

        var updateLink = linkTo(methodOn(SkillRestController.class).update(id, null))
            .withRel("update")
            .withType("PUT");

        var deleteLink = linkTo(methodOn(SkillRestController.class).delete(id))
            .withRel("delete")
            .withType("DELETE");
        

        resource.add(selfLink, updateLink, deleteLink);
    }

    @Override
    public void addLinks(CollectionModel<EntityModel<SkillResponse>> resources) {
        var selfLink = linkTo(methodOn(SkillRestController.class).findAll(null))
            .withSelfRel()
            .withType("GET");
        
        var createLink = linkTo(methodOn(SkillRestController.class).create(null))
            .withRel("create")
            .withType("POST");

        resources.add(selfLink, createLink);
    }
    
}
