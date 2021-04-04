package eu.geekhome.rest.tags;

import eu.geekhome.rest.PluginsCoordinator;
import eu.geekhome.services.repository.Repository;
import eu.geekhome.services.repository.TagDto;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("tags")
public class TagsController {

    private final Repository _repository;

    @Inject
    public TagsController(PluginsCoordinator pluginsCoordinator) {
        _repository = pluginsCoordinator.getRepository();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public long postTag(TagDto tagDto) {
        return _repository.saveTag(tagDto);
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public void putTag(TagDto tagDto) {
        _repository.updateTag(tagDto);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public List<TagDto> getAllTags() {
        return _repository.getAllTags();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public void deleteTag(@PathParam("id") long id) {
        _repository.deleteTag(id);
    }
}