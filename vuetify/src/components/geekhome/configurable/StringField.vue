<template>
  <div>
      <v-text-field v-model="value.value" :label="value.hint" required></v-text-field>
  </div>
</template>

<script>
export default {
  props: ["value"]
};
</script>