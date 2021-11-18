package eu.automateeverything.rest.dependencies

import eu.automateeverything.data.Repository
import eu.automateeverything.domain.dependencies.Dependency
import eu.automateeverything.domain.dependencies.DependencyChecker
import jakarta.inject.Inject
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType

@Path("dependencies")
class DependenciesController @Inject constructor(
    private val dependencyChecker: DependencyChecker
) {
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    fun getInstanceDependencies(@PathParam("id") id: Long): MutableCollection<Dependency> {
        return dependencyChecker.checkInstance(id).values
    }
}
