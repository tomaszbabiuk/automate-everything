<template>
  <div>
    <v-treeview
    dense
    :items="configurables"
    item-key="class"
    item-text="titleRes"
  ></v-treeview>
  </div>
</template>

<script>
import { client } from "../rest.js"

export default {
  data: function() {
    return {
      items: [
        { clazz: "name1", id: "id1" },
        { clazz: "name2", id: "id2" },
      ]
    }
  },
  computed: {
    configurables () {
      return this.$store.state.configurables
    }
  },
  mounted: function() {
    client.getConfigurables()
  }
};
</script>

/*
[
  {
    "fields": [
      {
        "name": "name",
        "hint": "Name",
        "class": "StringField"
      },
      {
        "name": "name",
        "hint": "Description",
        "class": "StringField"
      }
    ],
    "class": "SceneConfigurable",
    "addNewRes": "Scenes",
    "titleRes": "Scenes",
    "iconName": "scene",
    "children": []
  },
  {
    "fields": [
      {
        "name": "name",
        "hint": "Name",
        "class": "StringField"
      },
      {
        "name": "name",
        "hint": "Description",
        "class": "StringField"
      }
    ],
    "class": "GroupConfigurable",
    "addNewRes": "Groups",
    "titleRes": "Groups",
    "iconName": "group",
    "children": []
  },
  {
    "fields": [
      {
        "name": "name",
        "hint": "Name",
        "class": "StringField"
      },
      {
        "name": "name",
        "hint": "Description",
        "class": "StringField"
      }
    ],
    "class": "FloorConfigurable",
    "addNewRes": "House plan",
    "titleRes": "House plan",
    "iconName": "group",
    "children": [
      {
        "fields": [
          {
            "name": "name",
            "hint": "Name",
            "class": "StringField"
          },
          {
            "name": "name",
            "hint": "Description",
            "class": "StringField"
          }
        ],
        "class": "RoomConfigurable",
        "addNewRes": "Room",
        "titleRes": "Room",
        "iconName": "group",
        "children": []
      }
    ]
  },
  {
    "fields": [
      {
        "name": "name",
        "hint": "Name",
        "class": "StringField"
      },
      {
        "name": "name",
        "hint": "Description",
        "class": "StringField"
      }
    ],
    "class": "DeviceConfigurable",
    "addNewRes": "Devices",
    "titleRes": "Devices",
    "iconName": "group",
    "children": []
  }
]
*/