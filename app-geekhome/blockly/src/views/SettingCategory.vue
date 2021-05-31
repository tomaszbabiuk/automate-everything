<template>
  <div>
    <v-breadcrumbs :items="breadcrumbs">
      <template v-slot:divider>
        <v-icon>mdi-forward</v-icon>
      </template>
    </v-breadcrumbs>

    <v-expansion-panels focusable v-model="panels" multiple>
      <v-expansion-panel :key="0" >
        <v-expansion-panel-header>
          <template v-slot:default="{ open }">
            <v-row no-gutters>
              <v-col cols="4"> LAN discovery </v-col>
              <v-col cols="8" class="text--secondary">
                <v-fade-transition leave-absolute>
                  <span v-if="open" key="0">
                    {{ description }}
                  </span>
                  <span v-else key="1"> </span>
                </v-fade-transition>
              </v-col>
            </v-row>
          </template>
        </v-expansion-panel-header>
        <v-expansion-panel-content>
          <settings-form :clazz="getSettingsClazz()"></settings-form>
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
      description: "",
    };
  },
  computed: {
    categories() {
      return this.$store.state.settingCategories;
    },
  },

  watch: {
    categories() {
      var settingsClass = this.getSettingsClazz();
      var settingCategory = this.getSettingCategoryByClazz(settingsClass);
      this.breadcrumbs = this.calculateBreadcrumbs(settingCategory);
      this.description = settingCategory.descriptionRes;
    },
  },
  methods: {
    getSettingsClazz: function () {
      return this.$route.params.clazz;
    },

    getSettingCategoryByClazz: function (clazz) {
      var result = null;
      this.$store.state.settingCategories.forEach((element) => {
        if (element.clazz == clazz) {
          result = element;
        }
      });

      return result;
    },

    calculateBreadcrumbs(settingCategory) {
      var breadcrumbs = [];

      breadcrumbs.push({
        text: settingCategory.titleRes,
        disabled: true,
        href: "/settings/" + settingCategory.clazz,
      });

      breadcrumbs.push({
        text: this.$vuetify.lang.t("$vuetify.navigation.settings"),
        disabled: false,
        href: "/settings",
      });

      return breadcrumbs.reverse();
    },
  },
  beforeMount: function () {
    client.getSettingCategories();
  },
};
</script>