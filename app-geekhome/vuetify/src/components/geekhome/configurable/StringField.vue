<template>
  <div>
      <v-text-field v-model="inputVal" :label="hint" :required="required" :counter="counter"></v-text-field>
  </div>
</template>

<script>
export default {
  props: ["value", "hint", "required", "counter"],
  computed: {
    inputVal: {
      get() {
        return this.value;
      },
      set(val) {
        this.$emit('input', val);
      }
    }
  
  }
};
</script>