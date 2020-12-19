package eu.geekhome.rest.icons;

import eu.geekhome.rest.PluginsManager;
import eu.geekhome.services.repository.IconDto;
import eu.geekhome.services.repository.Repository;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("icons")
public class IconController {

    private final Repository _repository;

    @Inject
    public IconController(PluginsManager pluginsManager) {
        _repository = pluginsManager.getRepositories().get(0);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public long postIcon(IconDto iconDto) {
        return _repository.saveIcon(iconDto);
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public void putIcon(IconDto iconDto) {
        _repository.updateIcon(iconDto);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public List<IconDto> getAllIconCategories() {
        return _repository.getAllIcons();
    }

    @GET
    @Path("/{id}/raw")
    @Produces("image/svg+xml;charset=utf-8")
    public String getRaw(@PathParam("id") long id) {
        return _repository.getIcon(id).getRaw();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public void deleteIcon(@PathParam("id") long id) {
        _repository.deleteIcon(id);
    }
}