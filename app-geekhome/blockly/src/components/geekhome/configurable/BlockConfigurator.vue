<template>
  <div class="text-center">
    <v-menu offset-y>
      <template v-slot:activator="{ on, attrs }">
        <v-btn color="primary" dark fab class="ma-4" v-bind="attrs" v-on="on" left>
          <v-icon dark>mdi-plus</v-icon>
        </v-btn>
      </template>
      <v-list>
        <v-subheader>Create new:</v-subheader>
        <v-list-item v-for="item in items" :key="item.id">
          <v-list-item-title>{{ item.title }}</v-list-item-title>
        </v-list-item>

        <v-subheader>Reuse:</v-subheader>
        <v-list-item v-for="item in items2" :key="item.id">
          <v-list-item-title>{{ item.title }}</v-list-item-title>
        </v-list-item>
      </v-list>
    </v-menu>
  </div>
</template>

<script>
export default {
  data: () => ({
    items: [
      { id: 0, title: "Click Me 1" },
      { id: 1, title: "Click Me 2" },
      { id: 2, title: "Click Me 3" },
      { id: 3, title: "Click Me 4" },
    ],
    items2: [
      { id: 4, title: "Click Me 5" },
      { id: 5, title: "Click Me 6" },
      { id: 6, title: "Click Me 7" },
      { id: 7, title: "Click Me 8" },
    ],
  }),
};
</script>