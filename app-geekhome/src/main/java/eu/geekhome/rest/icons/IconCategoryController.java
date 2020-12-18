package eu.geekhome.rest.icons;

import eu.geekhome.rest.PluginsManager;
import eu.geekhome.services.repository.IconCategoryDto;
import eu.geekhome.services.repository.Repository;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("iconcategories")
public class IconCategoryController {

    private final Repository _repository;

    @Inject
    public IconCategoryController(PluginsManager pluginsManager) {
        _repository = pluginsManager.getRepositories().get(0);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public long postIconCategory(IconCategoryDto iconCategoryDto) {
        return _repository.saveIconCategory(iconCategoryDto);
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public void putIconCategory(IconCategoryDto iconCategoryDto) {
        _repository.updateIconCategory(iconCategoryDto);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public List<IconCategoryDto> getAllIConCategories() {
        return _repository.getAllIconCategories();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public void deleteIconCategory(@PathParam("id") long id) {
        _repository.deleteIconCategory(id);
    }
}