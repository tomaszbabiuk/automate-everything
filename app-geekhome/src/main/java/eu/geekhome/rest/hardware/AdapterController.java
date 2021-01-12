package eu.geekhome.rest.hardware;

import eu.geekhome.HardwareManager;
import eu.geekhome.rest.HardwareManagerHolderService;
import eu.geekhome.services.hardware.HardwareAdapterDto;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

@Path("hardwareadapters")
public class AdapterController {

    private final HardwareManager _hardwareManager;
    private HardwareAdapterDtoMapper _mapper;

    @Inject
    public AdapterController(
            HardwareManagerHolderService hardwareManagerHolderService,
            HardwareAdapterDtoMapper mapper) {
        _hardwareManager = hardwareManagerHolderService.getInstance();
        _mapper = mapper;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public List<HardwareAdapterDto> getAdapters() {
        return _hardwareManager
                    .getFactories()
                    .stream()
                    .map(_mapper::map)
                    .collect(Collectors.toList());
    }
}