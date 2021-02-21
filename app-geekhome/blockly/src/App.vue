<template>
  <v-app>
    <v-app-bar app color="primary" dark>
      <v-app-bar-nav-icon @click.stop="drawer = !drawer"></v-app-bar-nav-icon>

      <v-toolbar-title>{{$vuetify.lang.t('$vuetify.application.name')}} - {{ $route.name }}</v-toolbar-title>
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
            <v-icon v-if="isPolishLocale">$vuetify.icon.flag_pl</v-icon>
            <v-icon v-else>$vuetify.icon.flag_uk</v-icon>
          </v-btn>
        </template>
        <v-list>
          <v-list-item
            v-for="(item, index) in languageSelectorItems"
            :key="index"
            @click="selected(item)"
          >
            <v-list-item-title>{{ item.title }}</v-list-item-title>
          </v-list-item>
        </v-list>
      </v-menu>
      <template v-slot:extension v-if="showTabs">
        <v-tabs
          v-model="tab"
          align-with-title
        >
          <v-tabs-slider color="yellow"></v-tabs-slider>

          <v-tab
            v-for="factory in factories"
            :key="factory.id"
            :to="'/discover/' + factory.id"
          >
            {{ factory.name }}
          </v-tab>
        </v-tabs>
      </template>
    </v-app-bar>

    <v-navigation-drawer v-model="drawer" app>
      <v-list-item>
        <v-list-item-content>
          <v-list-item-title class="title">{{$vuetify.lang.t('$vuetify.application.name')}}</v-list-item-title>
          <v-list-item-subtitle>{{$vuetify.lang.t('$vuetify.application.subtitle')}}</v-list-item-subtitle>
        </v-list-item-content>
      </v-list-item>

      <v-divider></v-divider>

      <v-list dense>
        <v-list-item link v-for="item in navigationItems" :key="item.title" :to="item.route">
          <v-list-item-action>
            <v-icon style="fill:#9e9e9e">$vuetify.icon.{{item.icon}}</v-icon>
          </v-list-item-action>
          <v-list-item-content>
            <v-list-item-title class="grey--text">{{$vuetify.lang.t(item.title)}}</v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </v-list>
    </v-navigation-drawer>
    <v-main class="mx-4 mb-4">
      <div v-if="error">
        <v-banner single-line sticky>
          {{$vuetify.lang.t(error.message)}}
          <template v-slot:actions>
            <v-btn
              text
              color="deep-purple accent-4"
              @click="error.actionCallback"
            >{{$vuetify.lang.t(error.actionTitle)}}</v-btn>
          </template>
        </v-banner>
      </div>
      <div :class="$route.name">
        <v-container>
          <router-view></router-view>
        </v-container>
      </div>
    </v-main>
  </v-app>
</template>

<script>
import { client } from "./rest.js";

export default {
  name: "App",

  data: function() {
    return {
      tab: null,
      drawer: false,
      navigationItems: [
        { title: "$vuetify.navigation.inbox", route: "/inbox", icon: "inbox" },
        {
          title: "$vuetify.navigation.timeline",
          route: "/timeline",
          icon: "timeline"
        },
        { 
          title: "$vuetify.navigation.alerts",
          route: "/alerts", 
          icon: "bell" },
        {
          title: "$vuetify.navigation.control",
          route: "/control",
          icon: "button"
        },
        { 
          title: "$vuetify.navigation.objects", 
          route: "/configurables/null", 
          icon: "objects" 
        },
        { 
          title: "$vuetify.navigation.tags", 
          route: "/tags", 
          icon: "tag" 
        },
        { 
          title: "$vuetify.navigation.icons", 
          route: "/icons", 
          icon: "icons" 
        },
        {
          title: "$vuetify.navigation.discover",
          route: "/discover/null",
          icon: "crosshair"
        },
        {
          title: "$vuetify.navigation.settings",
          route: "/settings",
          icon: "equalizer"
        },
        {
          title: "$vuetify.navigation.plugins",
          route: "/plugins",
          icon: "plugin"
        }
      ],
      languageSelectorItems: [
        { title: "English", code: "en" },
        { title: "Polski", code: "pl" }
      ]
    };
  },

  methods: {
    selected: function(item) {
      this.$vuetify.lang.current = item.code;
      localStorage.selectedLanguage = item.code;
      location.href = location.href + "";
    },
    enableAutomation: function(enable) {
      client.enableAutomation(enable);
    },
  },

  computed: {
    plugins() {
      return this.$store.state.plugins;
    },
    automation() {
      return this.$store.state.automation;
    },
    factories() {
      return this.$store.state.plugins.filter(element => {
        return element.enabled && element.isHardwareFactory
      })
    },
    isPolishLocale: function() {
      return this.$vuetify.lang.current === "pl";
    },
    error() {
      return this.$store.state.error;
    },
    showTabs: function() {
      return this.$route.name === 'discover' && this.factories.length > 0
    }
  },
   watch: {
    factories (factories) {
      if (factories.length > 0) {
        this.navigationItems[7].route='/discover/' + factories[0].id
      } else {
        this.navigationItems[7].route='/discover/null'
      }
    }
  },
  mounted: function() {
    if (typeof localStorage.selectedLanguage === "undefined") {
      localStorage.selectedLanguage = this.$vuetify.lang.current;
    } else {
      this.$vuetify.lang.current = localStorage.selectedLanguage;
    }

    client.getPlugins();
    client.getAutomation();
  }
};
</script>
<style lang="scss">
@import "@/styles/index.scss";
</style>