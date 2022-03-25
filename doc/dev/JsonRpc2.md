# JsonRPC2
JsonRPC2 is used in communication over secure channels (like Salt-channel). It is used by "Mobile access" plugin to communicate between the Android smartphone and the automation server. 

## Supported methods:

- GetIcons - returns all icons (IconDto) from data repository (by default). Accepts the list of icon ids to restrict the amount of data being returned (List<Long>) as parameter.

- GetInstances - returns all instances (InstanceDto) from data repository.

- GetMessages - returns all messages (InboxItemDto) from data repository.

- GetTags - returns all tags (TagDto) from data repository.

- GetVersions - returns all information about the entities in data repository (VersionDto)

- GetAutomationUnits - returns all evaluations of automation units (AutomationUnitDto)

- Subscribe - subscribe to one of the events from the list below. Accepts the list of entity names as filters, to limit the amount of data to the ones that client is interested in (List<String>)

|Event name      | Entities                              | Description                                                                   |
|----------------|---------------------------------------|-------------------------------------------------------------------------------|
|PortUpdate      |PortDto                                |fired every time when state of any port changes                                |
|PluginUpdate    |PluginDto                              |fired every time plugin state changes                                          |
|DiscoveryInfo   |DiscoveryEventDto                      |fired when hardware adapters report changes (mostly during the discovery phase)|
|AutomationUpdate|AutomationUnitDto, AutomationHistoryDto|fired when there's an update of state or value caused by the automation engine |
|AutomationState |Boolean, AutomationHistoryDto          |fired when automation engine is disabled or enabled                            |
|Heartbeat       |HeartbeatDto                           |fired periodically to signal the system is alive                               |
|InboxUpdate     |InboxItemDto                           |fired when there's a new item in the inbox                                     |
|InstanceUpdate  |InstanceDto                            |fired if new object is added or an existing object is updated                  |