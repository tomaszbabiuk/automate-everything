<template>
  <div>
    <v-form ref="form" v-if="settingGroup != null">
      <component
        class="ma-4"
        v-for="field in settingGroup.fields"
        :key="field.name"
        :hint="field.hint"
        :counter="field.maxSize"
        :required="field.required"
        :id="field.name"
        :clazz="settingGroup.clazz"
        v-bind:is="fieldClassToFormComponent(field.clazz)"
      >
      </component>
    </v-form>
  </div>
</template>

<script>
export default {
  props: ["settingGroup", "settings"],
  methods: {
    fieldClassToFormComponent: function (clazz) {
      return "settings-" + clazz.toLowerCase();
    },
  },
};
</script>
