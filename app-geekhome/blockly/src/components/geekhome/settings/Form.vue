<template>
  <div>
    <v-form ref="form" v-if="settingCategory != null">
      <component
        class="ma-4"
        v-for="field in settingCategory.fields"
        :key="field.name"
        :hint="field.hint"
        :counter="field.maxSize"
        :required="field.required"
        :id="field.name"
        v-bind:is="fieldClassToFormComponent(field.clazz)"
      >
      </component>
    </v-form>
  </div>
</template>

<script>
export default {
  props: ["clazz"],
  computed: {
    settingCategory: function () {
      return this.getSettingCategoryByClazz(this.clazz);
    },
  },
  methods: {
    fieldClassToFormComponent: function (clazz) {
      return "settings-" + clazz.toLowerCase();
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
  },
};
</script>
