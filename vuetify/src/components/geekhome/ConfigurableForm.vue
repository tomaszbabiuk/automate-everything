<template>
  <div>
    <button v-on:click="count++">You clicked me {{ value }} times.</button>
    <v-form ref="form" v-model="valid" lazy-validation>
      <v-text-field v-model="field.value" v-for="field in value.fields" :key="field.Name" :label="field.hint" required></v-text-field>
    </v-form>
  </div>
</template>

<script>
export default {
  props: ["value"]
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
