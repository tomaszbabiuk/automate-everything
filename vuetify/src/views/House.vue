<template>
  <v-row class="pa-4" justify="space-between">
    <v-col cols="5">
      <v-treeview
        :active.sync="active"
        :items="configurables"
        item-key="class"
        item-text="titleRes"
        item-children="children"
        :open.sync="open"
        :load-children="fetchActions"
        activatable
        color="warning"
        open-on-click
        transition
      ></v-treeview>
    </v-col>

    <v-divider vertical></v-divider>

    <v-col class="d-flex text-center">
      <v-scroll-y-transition mode="out-in">
        <div
          v-if="!selected"
          class="title grey--text text--lighten-1 font-weight-light"
          style="align-self: center;"
        >Select a User</div>
        <div v-else>Selected</div>
      </v-scroll-y-transition>
    </v-col>
  </v-row>
</template>

<script>
import { client } from "../rest.js";

export default {
  data: function() {
    return {
      active: [],
      open: [],
      configurablesOffline: [
        {
          fields: [
            {
              name: "name",
              hint: "Name",
              class: "StringField"
            },
            {
              name: "name",
              hint: "Description",
              class: "StringField"
            }
          ],
          class: "SceneConfigurable",
          addNewRes: "Scenes",
          titleRes: "Scenes",
          iconName: "scene",
          children: []
        },
        {
          fields: [
            {
              name: "name",
              hint: "Name",
              class: "StringField"
            },
            {
              name: "name",
              hint: "Description",
              class: "StringField"
            }
          ],
          class: "GroupConfigurable",
          addNewRes: "Groups",
          titleRes: "Groups",
          iconName: "group",
          children: []
        },
        {
          fields: [
            {
              name: "name",
              hint: "Name",
              class: "StringField"
            },
            {
              name: "name",
              hint: "Description",
              class: "StringField"
            }
          ],
          class: "FloorConfigurable",
          addNewRes: "House plan",
          titleRes: "House plan",
          iconName: "group",
          children: [
            {
              fields: [
                {
                  name: "name",
                  hint: "Name",
                  class: "StringField"
                },
                {
                  name: "name",
                  hint: "Description",
                  class: "StringField"
                }
              ],
              class: "RoomConfigurable",
              addNewRes: "Room",
              titleRes: "Room",
              iconName: "group",
              children: []
            }
          ]
        },
        {
          fields: [
            {
              name: "name",
              hint: "Name",
              class: "StringField"
            },
            {
              name: "name",
              hint: "Description",
              class: "StringField"
            }
          ],
          class: "DeviceConfigurable",
          addNewRes: "Devices",
          titleRes: "Devices",
          iconName: "group",
          children: []
        }
      ]
    };
  },
  computed: {
    configurables() {
      return this.$store.state.configurables;
    },
    selected() {
      if (!this.active.length) return undefined;

      const clazz = this.active[0];

      return this.configurables.find(x => x.class === clazz);
    }
  },
  methods: {
      async fetchActions (item) {
        console.log(item)
        return await item.children
      },
  },
  mounted: function() {
    client.getConfigurables();
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