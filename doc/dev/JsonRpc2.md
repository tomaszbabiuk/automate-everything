# JsonRPC2

[JsonRPC2](https://www.jsonrpc.org/specification) is used in communication over secure channels (like Salt-channel). It is used by "Mobile access" plugin to
communicate between the Android smartphone and the automation server.

The plan is to use the same channel to control the system totally offline by using the app and NFC reader [(see project roadmap)](../Roadmap.md).

## Supported methods

### GetIcons

Returns all icons (IconDto) from data repository (by default). Accepts the list of icon ids to restrict the amount of
data being returned (List<Long>) as parameter.

Example request

```json
{
  "jsonrpc": "2.0",
  "method": "GetIcons",
  "params": [
    42,
    23
  ],
  "id": 1
}
```

Example response

```json
{
  "jsonrpc": "2.0",
  "result": [
    {
      "id": 42,
      "iconCategoryId": 7,
      "owner": "system_icons",
      "raw": "<svg content here>"
    },
    {
      "id": 23,
      "iconCategoryId": 7,
      "owner": "system_icons",
      "raw": "<svg content here>"
    }
  ],
  "id": 1
}
```

### GetInstances

Returns all instances (InstanceDto) from data repository. Example request

```json
{
  "jsonrpc": "2.0",
  "method": "GetInstances",
  "id": 2
}
```

Example response

```json
{
  "jsonrpc": "2.0",
  "result": [
    {
      "id": 2,
      "iconId": 17,
      "tagIds": [
        24,
        20
      ],
      "clazz": "eu.automateeverything.scenesplugin.SceneConfigurable",
      "fields": {
        "name": "Scene 1",
        "description": "",
        "automation_only": "0"
      },
      "automation": null
    },
    {
      "id": 3,
      "iconId": 18,
      "tagIds": [
        20,
        25,
        26
      ],
      "clazz": "eu.automateeverything.scenesplugin.SceneConfigurable",
      "fields": {
        "name": "Scene 2",
        "description": "",
        "automation_only": "0"
      },
      "automation": null
    }
  ],
  "id": 2
}
```

### GetMessages

Returns all messages (InboxItemDto) from data repository. TODO: example with data

Example request:

```json
{
  "jsonrpc": "2.0",
  "method": "GetMessages",
  "id": 3
}
```

Example response:

```json
{
  "jsonrpc": "2.0",
  "result": [
    {
      "id": 43,
      "subject": "Automation has been enabled",
      "body": "Automation is ON.",
      "timestamp": 1648237398704,
      "read": false
    },
    {
      "id": 42,
      "subject": "Automation has been enabled",
      "body": "Automation is ON.",
      "timestamp": 1648156236218,
      "read": false
    }
  ],
  "id": 3
}
```

### GetTags

Returns all tags (TagDto) from data repository.

Example request:

```json
 {
  "jsonrpc": "2.0",
  "method": "GetTags",
  "id": 4
}
```

Example response:

```json
{
  "jsonrpc": "2.0",
  "result": [
    {
      "id": 2,
      "parentId": 1,
      "name": "Tag1"
    },
    {
      "id": 4,
      "parentId": null,
      "name": "Ground floor"
    }
  ],
  "id": 4
}
```

### GetVersions

Returns all information about the entities in data repository (VersionDto)

Example request:

```json
 {
  "jsonrpc": "2.0",
  "method": "GetVersions",
  "id": 5
}
```

Example response:

```json
{
  "jsonrpc": "2.0",
  "result": [
    {
      "name": "InstanceDto",
      "timestamp": 1123123123
    },
    {
      "name": "TagDto",
      "timestamp": 123123123123
    }
  ],
  "id": 5
}
```

### GetAutomationUnits

Returns all evaluations of automation units (AutomationUnitDto)

Example request:

```json
 {
  "jsonrpc": "2.0",
  "method": "GetAutomationUnits",
  "id": 6
}
```

Example response:

```json
{
  "jsonrpc": "2.0",
  "result": [
    {
      "type": "States",
      "valueRange": null,
      "instance": {
        "id": 2,
        "iconId": 17,
        "tagIds": [
          24,
          20
        ],
        "clazz": "eu.automateeverything.scenesplugin.SceneConfigurable",
        "fields": {
          "name": "Scene 1",
          "description": "",
          "automation_only": "0"
        },
        "automation": null
      },
      "evaluationResult": {
        "interfaceValue": "Inactive",
        "decimalValue": null,
        "isSignaled": false,
        "descriptions": [],
        "error": null,
        "nextStates": {
          "states": [
            {
              "id": "inactive",
              "name": "Inactive",
              "action": "Deactivate",
              "type": "Control",
              "isSignaled": false
            },
            {
              "id": "active",
              "name": "Active",
              "action": "Activate",
              "type": "Control",
              "isSignaled": true
            }
          ],
          "current": "inactive",
          "extendedWidth": false
        }
      }
    }
  ],
  "id": 6
}
```

### Control
Used to control the objects/automation.



### Subscribe

Subscribes to one of the events from the list below:

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

The name of the entity, the client is interested in is required as a param (String).

Example request:

```json
{
  "jsonrpc": "2.0",
  "method": "Subscribe",
  "params": "HeartbeatDto",
  "id": 7
}
```

Example response:

```json
{
  "jsonrpc": "2.0",
  "result": "subscription-id",
  "id": 7
}
```

Example subscription data (HeartbeatDto):

```json
{
  "jsonrpc": "2.0",
  "result": {
    "timestamp": 123123123123,
    "inboxUnreadCount": 10,
    "isAutomationEnabled": true
  },
  "id": "subscription-id"
}
```

## Error handling

Example error response:

```json
{
  "jsonrpc": "2.0",
  "error": {
    "code": -32600,
    "message": "Invalid Request"
  },
  "id": null
}
```