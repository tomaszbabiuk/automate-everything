<template>
  <div>

    Settings category
    <settings-form :clazz="getSettingsClazz()"></settings-form>

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
    getSettingsClazz: function () {
      return this.$route.params.clazz;
    },
  },
  mounted: function () {
    client.getSettingCategories();
  },
};
</script>