<template>
  <div>
    <v-skeleton-loader v-if="loading"
      type="table"
    ></v-skeleton-loader>
    <v-card class="pa-3 mb-3" v-for="plugin in plugins" :key="plugin.name" v-else>
      <v-row>
        <v-col md="8" sm="12">
          <div class="caption grey--text">{{$vuetify.lang.t('$vuetify.plugins_list.name')}}</div>
          <div>{{plugin.name}} ({{ plugin.enabled ? $vuetify.lang.t('$vuetify.plugins_list.enabled') : $vuetify.lang.t('$vuetify.plugins_list.disabled')}})</div>
        </v-col>
        <v-col md="1" sm="2">
          <div class="caption grey--text">{{$vuetify.lang.t('$vuetify.plugins_list.version')}}</div>
          <div>{{plugin.version}}</div>
        </v-col>
        <v-col md="3" sm="10" align="right">
          <v-btn v-if="plugin.enabled"
            class="float-right"
            outlined
            color="error"
            @click="submitState(plugin)"
          >{{ $vuetify.lang.t('$vuetify.plugins_list.disable') }}</v-btn>
          <v-btn v-else
            class="float-right"
            outlined
            color="primary"
            @click="submitState(plugin)"
          >{{ $vuetify.lang.t('$vuetify.plugins_list.enable') }}</v-btn>
        </v-col>
        <v-col md="10" sm="10">
          <div class="caption grey--text">{{$vuetify.lang.t('$vuetify.plugins_list.description')}}</div>
          <div class="text-justify">{{plugin.description}}</div>
        </v-col>
        <v-col md="2" sm="2">
          <v-btn v-if="plugin.settingGroups.length > 0"
            class="float-right"
            outlined
            color="primary"
            @click="openPluginSettings(plugin)"
          >{{$vuetify.lang.t('$vuetify.plugins_list.settings')}}</v-btn>
        </v-col>
      </v-row>
    </v-card>
    <v-dialog v-model="copyrightDialog.show" max-width="500px">
      <v-card>
        <v-card-title class="headline">{{
          $vuetify.lang.t("$vuetify.plugins_list.copyright")
        }}</v-card-title>
        <v-spacer></v-spacer>
        <v-card-text>
          {{ copyrightDialog.plugin.copyright }}
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn
            color="blue darken-1"
            text
            @click="closeCopyrightDialog()"
            >{{ $vuetify.lang.t("$vuetify.common.cancel") }}</v-btn
          >
          <v-btn
            color="blue darken-1"
            text
            @click="enablePluginAndCloseCopyrightDialog(copyrightDialog.plugin)"
            >{{ $vuetify.lang.t("$vuetify.plugins_list.enable") }}</v-btn
          >
          <v-spacer></v-spacer>
        </v-card-actions>
      </v-card>
    </v-dialog>
    <div v-if="plugins.length == 0 && !loading" class="text-center">
      {{ $vuetify.lang.t("$vuetify.noDataText") }}
    </div>
  </div>
</template>

<script>
import { client } from "../rest.js";

export default {
  data: function() {
    return {
      loading: true,
      copyrightDialog: {
        show: false,
        plugin: {}
      },
    }
  },

  methods: {
    showCopyrightDialog: function (plugin) {
      this.copyrightDialog.show = true;
      this.copyrightDialog.plugin = plugin;
    },

    closeCopyrightDialog: function () {
      this.copyrightDialog.show = false;
    },

    enablePluginAndCloseCopyrightDialog: function(plugin) {
      client.enablePlugin(plugin.id, true)
      this.closeCopyrightDialog();
    },

    submitState(plugin) {
      if (!plugin.enabled) {
        if (plugin.copyright != null) {
          this.showCopyrightDialog(plugin);
        } else {
          client.enablePlugin(plugin.id, true);
        }
      } else {
        client.enablePlugin(plugin.id, false);
      }
    },

    openPluginSettings: function (plugin) {
      this.$router.push({
        name: "pluginSettings",
        params: { id: plugin.id },
      });
    },
  },

  computed: {
    plugins() {
      var selectedCategory = this.$route.params.category

      function isInSelectedCategory(element) {
        return (element.category.toLowerCase() == selectedCategory);
      }

      return this.$store.state.plugins.filter(isInSelectedCategory);
    },

    matchPluginActionColor: function(plugin) {
      if (plugin.enabled) {
        return 'primary'
      } else {
        return 'error'
      }
    },
  },
  
  mounted: async function() {
    var that = this;
    await client.getPlugins().then(function() {
      that.loading = false
    })
  },
};
</script>