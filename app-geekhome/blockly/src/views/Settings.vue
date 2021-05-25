<template>
  <div class="mb-16">

    <div>
        <v-card class="mx-auto float-left ml-5 mt-5" max-width="344" v-for="category in categories" :key="category.clazz">
          <v-card-title class="headline">
            <div
              style="transform: scale(0.5)"
              v-html="category.iconRaw"
            ></div>
            {{ category.titleRes }}
          </v-card-title>

          <v-card-subtitle>{{
            category.descriptionRes
          }}</v-card-subtitle>

          <v-card-actions>
            <v-btn text @click="browse(category)">
              Browse
            </v-btn>
          </v-card-actions>
        </v-card>
    </div>

   


  </div>
</template>

<script>
import { client } from "../rest.js";


export default {
  data: function () {
    return {
     };
  },
  computed: {
    categories() {
      return this.$store.state.settingCategories;
    },

  },
  methods: {
  },
  mounted: function () {
    client.getSettingCategories();
  },
};
</script>