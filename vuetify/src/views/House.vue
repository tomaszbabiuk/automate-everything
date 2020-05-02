<template>
  <v-row class="pa-4" justify="space-between">
    <v-col cols="5">
      <v-treeview
        :active.sync="active"
        :items="configurables"
        item-key="class"
        item-text="titleRes"
        :open.sync="open"
        :load-children="fetchActions"
        activatable
        color="warning"
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
        <div v-else>
            Selected
            <button v-for="descendant in selected.descendants" :key="descendant.clazz">{{descendant.addNewRes}}</button>
        </div>
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
        return await item.descendants
      },
  },
  mounted: function() {
    client.getConfigurables();
  }
};
</script>