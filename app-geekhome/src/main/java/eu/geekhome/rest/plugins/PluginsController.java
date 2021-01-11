package eu.geekhome.rest.plugins;

import eu.geekhome.rest.PluginsManager;
import org.pf4j.PluginStateEvent;
import org.pf4j.PluginStateListener;
import org.pf4j.PluginWrapper;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.sse.OutboundSseEvent;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseBroadcaster;
import javax.ws.rs.sse.SseEventSink;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
@Path("plugins")
public class PluginsController implements PluginStateListener {

    private final PluginsManager _pluginsManager;
    private final PluginDtoMapper _pluginDtoMapper;
    private final SseBroadcaster _sseBroadcaster;
    private final Sse _sse;

    @Inject
    public PluginsController(PluginsManager pluginsManager, PluginDtoMapper pluginDtoMapper, Sse sse) {
        _sse = sse;
        _sseBroadcaster = sse.newBroadcaster();
        _pluginsManager = pluginsManager;
        _pluginDtoMapper = pluginDtoMapper;

        _pluginsManager.registerStateListener(this);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    public List<PluginDto> getPlugins(@Context HttpServletRequest request) {
        return _pluginsManager
                .getPlugins()
                .stream()
                .map(_pluginDtoMapper::map)
                .collect(Collectors.toList());
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    public PluginDto getPlugin(@PathParam("id") String id) {
        return _pluginDtoMapper.map(_pluginsManager.getPlugin(id));
    }

    @PUT
    @Path("/{id}/enabled")
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    public PluginDto updateEnableState(@PathParam("id") String id, boolean enable) {
        PluginWrapper pluginWrapper = enable ?
                _pluginsManager.enablePlugin(id) :
                _pluginsManager.disablePlugin(id);

        return _pluginDtoMapper.map(pluginWrapper);
    }

    @GET
    @Path("/live")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public void streamPluginChangesLive(@Context SseEventSink sink) {
        _sseBroadcaster.register(sink);
    }

    @Override
    public void pluginStateChanged(PluginStateEvent event) {
        PluginDto data = _pluginDtoMapper.map(event.getPlugin());
        OutboundSseEvent sseEvent = _sse.newEventBuilder()
                .name("pluginChange")
                .mediaType(MediaType.APPLICATION_JSON_TYPE)
                .data(PluginDto.class, data)
                .build();
        _sseBroadcaster.broadcast(sseEvent);
    }
}