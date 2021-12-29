<template>
  <v-row>
    <v-col cols="3" class="grey--text pa-0"> {{ hint }} [%] </v-col>
    <v-col cols="9">
      <v-slider
        v-model="percent"
        :counter="counter"
        :error="error"
        :error-messages="errorMessages"
        max="100"
        min="0"
        thumb-label
      ></v-slider>
    </v-col>
  </v-row>
</template>

<script>
import { UPDATE_INSTANCE_FIELD } from "../../../plugins/vuex";

export default {
  data: function () {
    return {
      error: false,
      errorMessages: [],
      percent: 0,
    };
  },

  props: ["id", "hint", "counter"],

  computed: {
    validation() {
      return this.$store.state.instanceValidation[this.id];
    },

    storeFieldData() {
      return this.$store.state.newInstance.fields[this.id];
    },
  },

  watch: {
    percent(val) {
      this.$store.commit(UPDATE_INSTANCE_FIELD, {
        name: this.id,
        value: val,
      });
    },

    validation(val) {
      this.error = !val.valid;
      this.errorMessages = val.reasons;
    },

    storeFieldData(value) {
      if (this.percent != value) {
        this.percent = value;
      }
    },

    text(value) {
      this.$store.commit(UPDATE_INSTANCE_FIELD, {
        name: this.id,
        value: value,
      });
    },
  },

  mounted: function () {
    this.percent = this.storeFieldData;
  },
};
</script>