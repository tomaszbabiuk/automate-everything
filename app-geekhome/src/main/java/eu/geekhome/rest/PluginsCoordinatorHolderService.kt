package eu.geekhome.rest

import eu.geekhome.PluginsCoordinator
import org.jvnet.hk2.annotations.Service
import javax.ws.rs.core.Application
import javax.ws.rs.core.Context


@Service
class PluginsCoordinatorHolderService(@Context app: Application) :
    HolderService<PluginsCoordinator>(app, PluginsCoordinator::class.java)