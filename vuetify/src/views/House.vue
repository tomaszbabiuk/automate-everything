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
        open-on-click
        transition
        returnObject
      >
        <template v-slot:prepend="{ item, active }">
          <v-icon v-if="!item.children">mdi-account</v-icon>
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
        >Select a User</div>
        <v-card v-else :key="selected.id" class="pt-6 mx-auto" flat max-width="400">
          <v-card-text>
            <v-avatar v-if="avatar" size="88">
              <v-img :src="`https://avataaars.io/${avatar}`" class="mb-6"></v-img>
            </v-avatar>
            <h3 class="headline mb-2">{{ selected.name }}</h3>
            <div class="blue--text mb-2">{{ selected.email }}</div>
            <div class="blue--text subheading font-weight-bold">{{ selected.username }}</div>
          </v-card-text>
          <v-divider></v-divider>
          <v-row class="text-left" tag="v-card-text">
            <v-col class="text-right mr-4 mb-2" tag="strong" cols="5">Company:</v-col>
            <v-col>{{ selected.company.name }}</v-col>
            <v-col class="text-right mr-4 mb-2" tag="strong" cols="5">Website:</v-col>
            <v-col>
              <a :href="`//${selected.website}`" target="_blank">{{ selected.website }}</a>
            </v-col>
            <v-col class="text-right mr-4 mb-2" tag="strong" cols="5">Phone:</v-col>
            <v-col>{{ selected.phone }}</v-col>
          </v-row>
        </v-card>
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
      console.log(this.active[0])
      //return this.$store.configurables.find(user => user.id === id);
      return null;
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
        let children = [
          {
            name: "No items",
            key: "noitems",
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