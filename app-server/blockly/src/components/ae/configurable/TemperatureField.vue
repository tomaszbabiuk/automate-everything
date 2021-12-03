<template>
  <v-text-field
    v-model="text"
    :label="hintWithTemperatureUnit"
    :counter="counter"
    :error="error"
    :error-messages="errorMessages"
    type="number"
  ></v-text-field>
</template>

<script>
import { UPDATE_INSTANCE_FIELD } from "../../../plugins/vuex";
import { temp } from "../../../temp"

export default {
  data: function () {
    return {
      error: false,
      errorMessages: [],
      text: "",
      temperature: "",
    };
  },
  props: ["id", "hint", "required", "counter"],
  methods: {
    storeTemperatureInKelvins(displayTemp) {
      var k = temp.displayTemperatureToKelvins(displayTemp);
      if (k != null) {
        if (this.storeFieldData != k) {
          this.$store.commit(UPDATE_INSTANCE_FIELD, {
            name: this.id,
            value: k,
          });
        }
      } else {
        this.$store.commit(UPDATE_INSTANCE_FIELD, {
          name: this.id,
          value: null,
        });
      }
    },
  },
  computed: {
    validation() {
      return this.$store.state.instanceValidation[this.id];
    },
    storeFieldData() {
      return this.$store.state.newInstance.fields[this.id];
    },
    hintWithTemperatureUnit() {
      return this.hint + " [" + this.temperatureUnit.title + "]";
    },
    temperatureUnit() {
      return temp.obtainTemperatureUnit();
    },
  },
  watch: {
    validation(val) {
      this.error = !val.valid;
      this.errorMessages = val.reasons;
    },

    storeFieldData(value) {
      var displayTemperature = temp.kelvinsToDisplayTemperature(value);
      if (this.text !== displayTemperature) {
        this.text = displayTemperature;
      }
    },

    text(value) {
      this.storeTemperatureInKelvins(value);
    },
  },
  mounted: function () {
    var displayTemperature = temp.kelvinsToDisplayTemperature(
      this.storeFieldData
    );
    if (displayTemperature != null) {
      this.text = displayTemperature;
    }
  },
};
</script>