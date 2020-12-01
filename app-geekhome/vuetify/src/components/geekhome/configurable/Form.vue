<template>
  <div>
    <v-form ref="form">
        <component v-for="field in value.fields" :hint="field.hint" :v-model="field.value" :key="field.Name"
          v-bind:is="configurableClassToFormComponent(field.class)">
        </component>
    </v-form>
  </div>
</template>

<script>
export default {
  props: ["value"],
  methods: {
          configurableClassToFormComponent: function(clazz) {
            return "configurable-" + clazz.toLowerCase()
          }
        }
};
</script>
