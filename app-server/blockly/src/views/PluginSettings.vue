<template>
  <div class="mb-16">
    <v-breadcrumbs :items="breadcrumbs">
      <template v-slot:divider>
        <v-icon>mdi-forward</v-icon>
      </template>
    </v-breadcrumbs>

    <v-skeleton-loader v-if="loading"
      type="paragraph, table-tfoot"
    ></v-skeleton-loader>
    <v-expansion-panels focusable multiple v-else v-model="panels"
    :disabled="saving">
      <v-expansion-panel
        v-for="settingGroup in plugin.settingGroups"
        :key="settingGroup.clazz"
      >
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
          <settings-form :settingGroup="settingGroup" :disabled="saving"></settings-form>
        </v-expansion-panel-content>
      </v-expansion-panel>
    </v-expansion-panels>
    <v-btn
      v-if="loading == false"
      :disabled="saving"
      fab
      dark
      large
      color="primary"
      fixed
      right
      bottom
      class="ma-4"
      @click="onApplySettings()"
    >
      <v-icon dark>mdi-check-all</v-icon>
    </v-btn>
    <v-snackbar
      v-model="snackbar"
    >
      {{$vuetify.lang.t('$vuetify.plugin_settings.saved_message')}}

      <template v-slot:action="{ attrs }">
        <v-btn
          color="pink"
          text
          v-bind="attrs"
          @click="snackbar = false"
        >
          {{$vuetify.lang.t('$vuetify.common.dismiss')}}
        </v-btn>
      </template>
    </v-snackbar>
  </div>
</template>

<script>
import { client } from "../rest.js";

export default {
  data: function () {
    return {
      snackbar: false,
      panels: [0,1],
      loading: true,
      saving: false,
      breadcrumbs: [],
      plugin: null,
    };
  },

  computed: {
    plugins() {
      return this.$store.state.plugins;
    },
  },

  methods: {
    dataLoaded() {
      console.log('data loaded')
      this.plugin = this.findPlugin(this.getPluginId());
      this.breadcrumbs = this.calculateBreadcrumbs(this.plugin);
    },

    getPluginId: function () {
      return this.$route.params.id;
    },

    findPlugin: function (pluginId) {
      var pluginsFound = this.$store.state.plugins.filter((element) => {
        return element.id == pluginId;
      });

      if (pluginsFound.length == 1) {
        return pluginsFound[0];
      } else {
        return null;
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

    onApplySettings() {
      this.saving = true;
      client.putSettings(
        this.getPluginId(),
        this.$store.state.settings,
        this.handleValidationResult
      );
    },

    handleValidationResult: function (validationResult) {
      console.log(validationResult)
      
      var isFormValid = true;
      for (const clazz in validationResult) {
        var validationGroup = validationResult[clazz]
        for (const field in validationGroup) {
          if (!validationGroup[field].valid) {
            isFormValid = false;
          }
        }      
      }

      this.saving = false;

      if (isFormValid) {
        this.panels = [];
        this.snackbar = true;
      }
    },
  },
  beforeMount: async function () {

    var that = this;
    await Promise.all([client.getSettings(this.getPluginId()), client.getPlugins()]).then(
      function () {
        that.loading = false;
        that.dataLoaded();
      }
    );
  },
};
</script>