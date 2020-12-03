<template>
  <div>
    <v-form ref="form">
        <component v-for="field in value.fields" :key="field.def.Name" :hint="field.def.hint" :initialValue="field.value"
          :instanceId="field.instanceId" :counter="field.def.maxSize" :required="field.def.required"
          v-bind:is="configurableClassToFormComponent(field.def.class)">
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
