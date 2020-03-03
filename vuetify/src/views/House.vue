<template>
  <v-row class="pa-4" justify="space-between">
    <v-col cols="5">
      <v-treeview
        :active.sync="active"
        :items="items"
        :load-children="fetchUsers"
        item-key="key"
        :open.sync="open"
        activatable
        color="warning"
        
        transition
        returnObject
      >
        <template v-slot:prepend="{ item }">
          <v-icon v-if="!item.children">mdi-plus</v-icon>
        </template>
      </v-treeview>
    </v-col>

    <v-divider vertical></v-divider>

    <v-col class="d-flex text-center">
      <v-scroll-y-transition mode="out-in">
        <div
          v-if="!selected"
          class="title grey--text text--lighten-1 font-weight-light"
          style="align-self: center;"
        >Select item from the tree</div>
        <div v-else>
          <p class="text-justify">
            {{selected.description}}
          </p>
          <v-btn>Add</v-btn>
        </div>
      </v-scroll-y-transition>
    </v-col>
  </v-row>
</template>

<script>
import { client } from "../rest.js";
import { mapState } from "vuex";

const pause = ms => new Promise(resolve => setTimeout(resolve, ms));

export default {
  data: () => ({
    active: [],
    open: [],
    items: []
  }),

  computed: {
    ...mapState(["configurables"]),
    selected() {
      console.log("selected");      
      if (!this.active.length) return undefined;
      return this.active[0];
    }
  },

  watch: {
    active(newValue, oldValue) {
      console.log(newValue);
      console.log(`Updating from ${oldValue} to ${newValue}`);
    },
    selected: "randomAvatar",
    configurables(newValue, oldValue) {
      console.log(`Updating from ${oldValue} to ${newValue}`);
      newValue
        .map(value => {
          return {

            name: value.titleRes,
            key: value.class,
            description: "Lorem ipsum et dolores",
            children: []
          };
        })
        .forEach(value => {
          this.items.push(value);
        });
    }
  },

  mounted: function() {
    client.getConfigurables();
  },

  methods: {
    async fetchUsers(item) {
      // Remove in 6 months and say
      // you've made optimizations! :)
      await pause(1500);

      let loadDataPromise =  new Promise(function(resolve) {

        // if (item.fields.length > 0) {

        // }

        let children = [
          {
            name: "0 items",
            key: "zeroOf" + item.key,
            description: "There's no groups defined yet",
            class: item.class
            //TODO: add children property to create a branch
          }
        ];
        resolve(children);
      });

      return loadDataPromise.then(json => (item.children.push(...json)))
    },
  }
};
</script>