<template>
  <div>
    <button v-on:click="count++">You clicked me {{ value }} times.</button>
    <v-form ref="form">
      <!-- <v-text-field v-model="field.value" v-for="field in value.fields" :key="field.Name" :label="field.hint" required></v-text-field> -->

        <component v-model="field.value" v-for="field in value.fields" :key="field.Name" v-bind:is="configurableClassToFormComponent"></component>
    </v-form>
  </div>
</template>

// TODO:
// You're likely experiencing a problem with the order of your operations. For instance, you're defining your own App component that uses the v-app component before you've even told Vue to make use of it, so Vue assumes you're using your own custom v-app component.

<script>
export default {
  props: ["value"],
  computed: {
          configurableClassToFormComponent: function() {
            console.log(this)
            return "v-text-field"
          }
        }
};
</script>

<style>
.v-offset {
  margin: 0 auto;
  max-width: calc(100% - 32px);
  position: relative;
}
.v-offset--full-width {
  max-width: 100%;
}
</style>
