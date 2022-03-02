<template>
  <v-app>
    <v-app-bar app color="primary" dark>
      <v-app-bar-nav-icon @click.stop="drawer = !drawer"></v-app-bar-nav-icon>

      <v-toolbar-title
        >{{ $vuetify.lang.t("$vuetify.application.name") }} -
        {{ $vuetify.lang.t("" + $route.meta.titleRes) }}</v-toolbar-title
      >
      <v-spacer></v-spacer>

      <v-btn icon v-if="automation.enabled" @click="enableAutomation(false)">
        <v-icon>$vuetify.icon.pause</v-icon>
      </v-btn>
      <v-btn icon v-else @click="enableAutomation(true)">
        <v-icon>$vuetify.icon.play</v-icon>
      </v-btn>
      <v-menu offset-y>
        <template v-slot:activator="{ on }">
          <v-btn icon v-on="on">
            {{ temperatureUnit.title }}
          </v-btn>
        </template>
        <v-list>
          <v-list-item
            v-for="(item, index) in temperatureSelectorItems"
            :key="index"
            @click="selectTemperatureUnit(item)"
          >
            <v-list-item-title>{{ item.title }}</v-list-item-title>
          </v-list-item>
        </v-list>
      </v-menu>
      <v-menu offset-y>
        <template v-slot:activator="{ on }">
          <v-btn icon v-on="on">
            <v-icon v-if="isPolishLocale">$vuetify.icon.flag_pl</v-icon>
            <v-icon v-else>$vuetify.icon.flag_uk</v-icon>
          </v-btn>
        </template>
        <v-list>
          <v-list-item
            v-for="(item, index) in languageSelectorItems"
            :key="index"
            @click="selectLanguage(item)"
          >
            <v-list-item-title>{{ item.title }}</v-list-item-title>
          </v-list-item>
        </v-list>
      </v-menu>
      <template v-slot:extension v-if="showTabs">
        <v-tabs v-model="tab" align-with-title>
          <v-tabs-slider color="yellow"></v-tabs-slider>

          <v-tab
            v-for="tab in tabs"
            :key="tab.id"
            :to="tab.to"
          >
            {{ tab.name }}
          </v-tab>
        </v-tabs>
      </template>
    </v-app-bar>

    <v-navigation-drawer v-model="drawer" app>
      <v-list-item>
        <v-list-item-content>
          <v-list-item-title class="title">{{
            $vuetify.lang.t("$vuetify.application.name")
          }}</v-list-item-title>
          <v-list-item-subtitle>{{
            $vuetify.lang.t("$vuetify.application.subtitle")
          }}</v-list-item-subtitle>
        </v-list-item-content>
      </v-list-item>

      <v-divider></v-divider>

      <v-list dense>
        <v-list-item
          link
          v-for="item in navigationItems"
          :key="item.title"
          :to="item.route"
        >
          <v-list-item-action v-if="item.hasBadge && inboxUnreadCount > 0">
            <v-badge color="red" :content="inboxUnreadCount">
              <v-icon style="fill: #9e9e9e"
                >$vuetify.icon.{{ item.icon }}</v-icon
              >
            </v-badge>
          </v-list-item-action>
          <v-list-item-action v-else>
            <v-icon style="fill: #9e9e9e">$vuetify.icon.{{ item.icon }}</v-icon>
          </v-list-item-action>
          <v-list-item-content>
            <v-list-item-title class="grey--text">{{
              $vuetify.lang.t(item.title)
            }}</v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </v-list>
    </v-navigation-drawer>
    <v-main class="mx-4 mb-4">
      <div v-if="error">
        <v-banner single-line sticky>
          {{ $vuetify.lang.t(error.message) }}
          <template v-slot:actions>
            <v-btn
              text
              color="deep-purple accent-4"
              @click="error.actionCallback"
              >{{ $vuetify.lang.t(error.actionTitle) }}</v-btn
            >
          </template>
        </v-banner>
      </div>
      <div :class="$route.name">
        <v-container>
          <v-alert v-cloak prominent type="error" v-if="!automation.enabled">
            <v-row align="center">
              <v-col class="grow">
                {{ $vuetify.lang.t("$vuetify.app.automation_disabled_info") }}
              </v-col>
              <v-col class="shrink">
                <v-btn @click="enableAutomation(true)">{{
                  $vuetify.lang.t("$vuetify.app.enable")
                }}</v-btn>
              </v-col>
            </v-row>
          </v-alert>
          <router-view></router-view>
        </v-container>
      </div>
    </v-main>
  </v-app>
</template>

<script>
import { client } from "./rest.js";
import { sseClient } from "./sse.js";
import { temp } from "./temp.js";

export default {
  name: "App",

  data: function () {
    return {
      tab: null,
      drawer: false,
      temperatureUnit: "",
      navigationItems: [
        {
          title: "$vuetify.navigation.inbox",
          route: "/inbox",
          icon: "inbox",
          hasBadge: true,
        },
        {
          title: "$vuetify.navigation.timeline",
          route: "/timeline",
          icon: "timeline",
          hasBadge: false,
        },
        {
          title: "$vuetify.navigation.control",
          route: "/control",
          icon: "button",
          hasBadge: false,
        },
        {
          title: "$vuetify.navigation.objects",
          route: "/objects/null",
          icon: "objects",
          hasBadge: false,
        },
        {
          title: "$vuetify.navigation.tags",
          route: "/tags",
          icon: "tag",
          hasBadge: false,
        },
        {
          title: "$vuetify.navigation.icons",
          route: "/icons",
          icon: "icons",
          hasBadge: false,
        },
        {
          title: "$vuetify.navigation.discover",
          route: "/discover/null",
          icon: "crosshair",
          hasBadge: false,
        },
        {
          title: "$vuetify.navigation.plugins",
          route: "/plugins/hardware",
          icon: "plugin",
          hasBadge: false,
        },
      ],
      languageSelectorItems: [
        { title: "English", code: "en" },
        { title: "Polski", code: "pl" },
      ],
      temperatureSelectorItems: [temp.CELSIUS, temp.KELVIN, temp.FAHRENHEIT],
    };
  },

  methods: {
    selectLanguage: function (item) {
      this.$vuetify.lang.current = item.code;
      localStorage.selectedLanguage = item.code;
      location.href = location.href + "";
    },

    selectTemperatureUnit: function (item) {
      this.temperatureUnit = item;
      location.href = location.href + "";
    },

    enableAutomation: function (enable) {
      client.enableAutomation(enable);
    },
  },

  computed: {
    tabs() {
      var isPluginsRoute = this.$route.name == "plugins";
      var result = []
      if (isPluginsRoute) {
          result=[
            {"id": "hardware",  "name": this.$vuetify.lang.t("$vuetify.plugins_list.category_hardware"), "to": "/plugins/hardware"},
            {"id": "objects",   "name": this.$vuetify.lang.t("$vuetify.plugins_list.category_objects"),  "to": "/plugins/objects"},
            {"id": "access",    "name": this.$vuetify.lang.t("$vuetify.plugins_list.category_access"),   "to": "/plugins/access"},
            {"id": "icons",     "name": this.$vuetify.lang.t("$vuetify.plugins_list.category_icons"),    "to": "/plugins/icons"},
            {"id": "others",    "name": this.$vuetify.lang.t("$vuetify.plugins_list.category_others"),   "to": "/plugins/others"}
          ]
      } else {
        this.factories.forEach(element => {
          result.push({"id": element.id, "name": element.name, "to": "/discover/" + element.id })
        });
      }

      return result;
    },

    plugins() {
      return this.$store.state.plugins;
    },

    automation() {
      return this.$store.state.automation;
    },
    
    inboxUnreadCount() {
      return this.$store.state.inboxUnreadCount;
    },

    factories() {
      return this.$store.state.plugins.filter((element) => {
        return element.enabled && element.isHardwareFactory;
      });
    },

    isPolishLocale: function () {
      return this.$vuetify.lang.current === "pl";
    },

    error() {
      return this.$store.state.error;
    },

    showTabs: function () {
      console.log(this.$route)
      var isDiscoveryRoute = this.$route.name == "discover" && this.factories.length > 0;
      var isPluginsRoute = this.$route.name == "plugins";
      return (isDiscoveryRoute || isPluginsRoute);
    },
  },

  watch: {
    factories(factories) {
      if (factories.length > 0) {
        this.navigationItems[6].route = "/discover/" + factories[0].id;
      } else {
        this.navigationItems[6].route = "/discover/null";
      }
    },

    temperatureUnit(newUnit) {
      temp.storeTemperatureUnit(newUnit);
    },
  },

  beforeMount: function () {
    this.temperatureUnit = temp.obtainTemperatureUnit()

    if (typeof localStorage.selectedLanguage === "undefined") {
      localStorage.selectedLanguage = this.$vuetify.lang.current;
    } else {
      this.$vuetify.lang.current = localStorage.selectedLanguage;
    }
  },

  mounted: function () {
    client.getPlugins();
    client.getAutomation();
    sseClient.openLiveEvents();
  },

  beforeDestroy() {
    sseClient.closeLiveEvents();
  },
};
</script>