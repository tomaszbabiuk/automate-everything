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
        :disabled="disabled"
        :initialValue="field.initialValue"
        v-bind:is="fieldClassToFormComponent(field.type)"
      >
      </component>
    </v-form>
  </div>
</template>

<script>
export default {
  props: ["settingGroup", "disabled"],
  methods: {
    fieldClassToFormComponent: function (type) {
      return "settings-" + type.toLowerCase() + "field";
    },
  },
};
</script>
