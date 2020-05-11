<template>
  <div>
      <v-text-field v-model="value.value" :label="value.hint" :required="value.required" :counter="value.maxSize"></v-text-field>
  </div>
</template>

<script>
export default {
  props: ["value"]
};
</script>