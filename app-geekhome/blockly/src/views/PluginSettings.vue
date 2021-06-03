<template>
  <div>
    <v-breadcrumbs :items="breadcrumbs">
      <template v-slot:divider>
        <v-icon>mdi-forward</v-icon>
      </template>
    </v-breadcrumbs>

    <v-expansion-panels focusable multiple v-if="plugin != null">
      <v-expansion-panel v-for="settingGroup in plugin.settingGroups" :key="settingGroup.clazz">
        <v-expansion-panel-header>
          <template v-slot:default="{ open }">
            <v-row no-gutters>
              <v-col cols="4"> {{ settingGroup.titleRes }} </v-col>
              <v-col cols="8" class="text--secondary">
                <v-fade-transition leave-absolute>
                  <span v-if="open" key="0">
                    {{ settingGroup.descriptionRes }}
                  </span>
                  <span v-else key="1"> </span>
                </v-fade-transition>
              </v-col>
            </v-row>
          </template>
        </v-expansion-panel-header>
        <v-expansion-panel-content>
          <settings-form :settingGroup="settingGroup"></settings-form>
           <v-btn
           class="float-right"
            depressed
            color="primary">
            Apply
          </v-btn>
        </v-expansion-panel-content>
      </v-expansion-panel>
    </v-expansion-panels>
  </div>
</template>

<script>
import { client } from "../rest.js";

export default {
  data: function () {
    return {
      panels: [0],
      breadcrumbs: [],
      plugin: null
    };
  },
  computed: {
    plugins() {
      return this.$store.state.plugins;
    },
  },

  watch: {
    plugins() {
      this.refresh();
    }
  },
  methods: {
    refresh: function() {
      this.plugin = this.findPlugin(this.getPluginId())
      if (this.plugin == null) {
        client.getPlugins();
      } else {
        this.breadcrumbs = this.calculateBreadcrumbs(this.plugin);
      }
    },

    getPluginId: function() {
      return this.$route.params.id;
    },

    findPlugin: function(pluginId) {
      var pluginsFound = this.$store.state.plugins.filter((element) => {
        return element.id == pluginId;
      });

      if (pluginsFound.length == 1) {
        return pluginsFound[0]
      } else {
        return null
      }
    },

    calculateBreadcrumbs(plugin) {
      var breadcrumbs = [];

      breadcrumbs.push({
        text: plugin.name,
        disabled: true,
        href: "/plugins/" + plugin.id,
      });

      breadcrumbs.push({
        text: this.$vuetify.lang.t("$vuetify.navigation.plugins"),
        disabled: false,
        href: "/plugins",
      });

      return breadcrumbs.reverse();
    },

  },
  beforeMount: function () {
    this.refresh();
  },
};
</script>