<template>
  <v-app>
    <v-app-bar app color="primary" dark>
      <v-app-bar-nav-icon @click.stop="drawer = !drawer"></v-app-bar-nav-icon>

      <v-toolbar-title>
        geekHOME - {{ $route.name }}:{{ $route.params.locale }}
      </v-toolbar-title>
      <v-spacer></v-spacer>

      <v-btn icon>
        <v-icon>mdi-heart</v-icon>
      </v-btn>
    </v-app-bar>

    <v-navigation-drawer v-model="drawer" app>
      <v-list-item>
        <v-list-item-content>
          <v-list-item-title class="title">geekHOME</v-list-item-title>
          <v-list-item-subtitle>Automation Studio</v-list-item-subtitle>
        </v-list-item-content>
      </v-list-item>

      <v-divider></v-divider>

      <v-list dense>
        <v-list-item link v-for="item in navigationItems" :key="item.title" :to='item.route'>
          <v-list-item-action>
            <v-icon style="fill:#9e9e9e">$vuetify.icon.{{item.icon}}</v-icon>
          </v-list-item-action>
          <v-list-item-content>
            <v-list-item-title class="grey--text">{{ item.title }}</v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </v-list>
    </v-navigation-drawer>

    <v-content class="mx-4 mb-4">
      <div :class="$route.name">
        <v-container class="my-5">
          <router-view></router-view>
        </v-container>
      </div>
    </v-content>
  </v-app>
</template>

<script>
export default {
  name: "App",

  components: {},

  data: () => ({
    drawer: false,
    navigationItems: [
      { title: "Inbox", route: "/inbox", icon: "inbox" },
      { title: "Timeline", route: "/timeline", icon: "timeline" },
      { title: "Alerts", route: "/alerts", icon: "bell"},
      { title: "Control", route: "/control", icon: "button" },
      { title: "House", route: "/house", icon: "house" },
      { title: "Discover", route: "/discover", icon: "crosshair"},
      { title: "Settings", route: "/settings", icon: "equalizer" },
      { title: "Plugins", route: "/plugins", icon: "plugin" }
    ]
  })
}
</script>
<style lang="scss">
@import '@/styles/index.scss';
</style>