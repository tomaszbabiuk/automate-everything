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
        >Select item</div>
        <div v-else>
          <v-btn @click.stop="openCreator(descendant)" class="mx-1" v-for="descendant in selected.descendants" :key="descendant.clazz">{{descendant.addNewRes}}</v-btn>
          <v-btn @click.stop="openCreator(selected)" v-if="selected.addNewRes != null">{{selected.addNewRes}}</v-btn>

          <v-dialog v-model="dialog" fullscreen hide-overlay transition="dialog-bottom-transition">
              <v-card>
              <v-toolbar dark color="primary">
                <v-btn icon dark @click="dialog = false">
                  <v-icon>mdi-close</v-icon>
                </v-btn>
                <v-toolbar-title>{{created.addNewRes}}</v-toolbar-title>
                <v-spacer></v-spacer>
                <v-toolbar-items>
                  <v-btn dark text @click="dialog = false">Save</v-btn>
                </v-toolbar-items>
              </v-toolbar>
              
              Lorem ipsum dolor sit amet consectetur adipisicing elit. Culpa quasi iusto distinctio sint aliquid nesciunt explicabo minima officiis quia soluta, voluptatem laudantium laboriosam incidunt dolor cupiditate voluptas! Sequi, nostrum quod?
              
              </v-card>
          </v-dialog>
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
      dialog: false,
      active: [],
      open: [],
      created: {
        addNewRes: "n/A"
      }
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
      openCreator(submodel) {
        this.dialog = true;
        this.created = submodel;
        console.log(submodel);
      }
  },
  mounted: function() {
    client.getConfigurables();
  }
};
</script>