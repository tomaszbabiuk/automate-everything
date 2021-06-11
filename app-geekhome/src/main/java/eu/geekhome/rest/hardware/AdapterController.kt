package eu.geekhome.rest.hardware

import eu.geekhome.HardwareManager
import javax.inject.Inject
import eu.geekhome.rest.HardwareManagerHolderService
import javax.ws.rs.GET
import javax.ws.rs.Produces
import eu.geekhome.domain.hardware.HardwareAdapterDto
import javax.ws.rs.Path
import javax.ws.rs.core.MediaType

@Path("hardwareadapters")
class AdapterController @Inject constructor(
    hardwareManagerHolderService: HardwareManagerHolderService,
    private val mapper: HardwareAdapterDtoMapper
) {

    private val hardwareManager: HardwareManager = hardwareManagerHolderService.instance

    @get:Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @get:GET
    val adapters: List<HardwareAdapterDto>
        get() = hardwareManager
            .bundles()
            .map { mapper.map(it)}
}