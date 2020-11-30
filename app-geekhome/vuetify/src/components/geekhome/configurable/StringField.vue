<template>
  <div>
      <v-text-field v-model="mutableValue" :label="hint" :required="required" :counter="counter"></v-text-field>
  </div>
</template>

<script>
export default {
  props: ["initialValue", "hint", "required", "counter", "key"],
  computed: {
    mutableValue: function () {
      return this.initialValue
    }
  }
};
</script>