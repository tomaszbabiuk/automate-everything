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
        >Select item<img src="/rest/svg" height="100" width="100" /></div>
        <div v-else>
          <v-btn @click.stop="openCreator(descendant)" class="mx-1" v-for="descendant in selected.descendants" :key="descendant.clazz">{{descendant.addNewRes}}</v-btn>
          <v-btn @click.stop="openCreator(selected)" v-if="selected.addNewRes != null">{{selected.addNewRes}}</v-btn>

          <v-dialog v-model="dialog" persistent fullscreen transition="dialog-bottom-transition">
              <v-card>
              <v-toolbar dark color="primary">
                <v-btn icon dark @click="dialog = false">
                  <v-icon>mdi-close</v-icon>
                </v-btn>
                <v-toolbar-title>{{dialogTitle}}</v-toolbar-title>
                <v-spacer></v-spacer>
                <v-toolbar-items>
                  <v-btn dark text @click="closeCreator">{{$vuetify.lang.t('$vuetify.configurables.add')}}</v-btn>
                </v-toolbar-items>
              </v-toolbar>
              
              <v-container>
                <v-form ref="form" v-if="newInstanceDef">
                  <component v-for="field in newInstanceDef.fields" :key="field.name" :hint="field.hint"
                    :counter="field.maxSize" :required="field.required" :id="field.name" :initialValue="null"
                    v-bind:is="configurableClassToFormComponent(field.class)">
                  </component>
                </v-form>
              </v-container>
              
              </v-card>
          </v-dialog>
        </div>
      </v-scroll-y-transition>
    </v-col>
  </v-row>
</template>

<script>
import { client } from "../rest.js"
import store from '../plugins/vuex'
import {NEW_INSTANCE, RESET_INSTANCE} from '../plugins/vuex'

export default {
  data: function() {
    return {
      dialog: false,
      active: [],
      open: [],
      dialogTitle: "",
      newInstanceDef: null
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
      configurableClassToFormComponent: function(clazz) {
        return "configurable-" + clazz.toLowerCase()
      },

      async fetchActions (item) {
        return await item.descendants
      },

      openCreator(configurable) {
        this.newInstanceDef = JSON.parse(JSON.stringify(configurable))
        this.dialogTitle = configurable.addNewRes
        store.commit(NEW_INSTANCE, configurable)
        this.dialog = true
      },

      closeCreator() {
        this.dialog = false
        client.postNewInstance(store.state.newInstance)
        store.commit(RESET_INSTANCE)
      }
  },
  mounted: function() {
    client.getConfigurables();
  }
};
</script>



