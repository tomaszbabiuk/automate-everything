<template>
  <div>
    <button v-on:click="count++">You clicked me {{ value }} times.</button>
    <v-form ref="form">
        <component v-bind="{'value': field}" v-for="field in value.fields" :key="field.Name" 
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
