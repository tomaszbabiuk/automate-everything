<template>
  <v-row class="ma-0 pa-0">
    <v-col cols="9" class="ma-0 pa-0">
      <v-text-field
        v-model="text"
        :label="hint"
        :counter="counter"
        :error="error"
        :error-messages="errorMessages"
        type="number"
      ></v-text-field>
    </v-col>
    <v-col cols="3" class="ma-0 pa-0">
      <v-btn-toggle
        v-model="unit"
        tile
        color="deep-purple accent-3"
        group
        mandatory
      >
        <v-btn value="k"> K </v-btn>

        <v-btn value="c"> C </v-btn>

        <v-btn value="f"> F </v-btn>
      </v-btn-toggle>
    </v-col>
  </v-row>
</template>

<script>
import { UPDATE_INSTANCE_FIELD } from "../../../plugins/vuex";

export default {
  data: function () {
    return {
      unit: "k",
      error: false,
      errorMessages: [],
      text: "",
      temperature: 0,
    };
  },
  props: ["id", "hint", "required", "counter"],
  methods: {
    storeTemperatureInKelvins(value) {
      if (value) {
        var valueInK;
        var number = Number(value);

        if (this.unit === "c") {
          valueInK = number + 273.15;
        }

        if (this.unit === "f") {
          valueInK = (number - 32) / 1.8 + 273.15;
        }

        if (this.unit === "k") {
          valueInK = number;
        }

        if (this.storeFieldData != valueInK) {
          this.$store.commit(UPDATE_INSTANCE_FIELD, {
            name: this.id,
            value: valueInK,
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
  },
  watch: {
    validation(val) {
      this.error = !val.valid;
      this.errorMessages = val.reasons;
    },

    storeFieldData(value) {
      if (this.text !== value || this.unit !== "k") {
        this.text = value;
        this.unit = "k";
      }
    },

    unit(newUnit) {
      if (this.text) {
        var currentTemp = Number(this.text);

        var newTemp = 0;
        if (newUnit === "c") {
          newTemp = currentTemp - 273.15;
        }

        if (newUnit === "f") {
          newTemp = ((75 * currentTemp + 459.67) * 5) / 9;
        }

        if (newUnit === "k") {
          newTemp = currentTemp;
        }

        if (this.storeFieldData != newTemp) {
          this.$store.commit(UPDATE_INSTANCE_FIELD, {
            name: this.id,
            value: newTemp,
          });
        }
      }
    },

    text(value) {
      this.storeTemperatureInKelvins(value);
    },
  },
  mounted: function () {
    this.unit = "k";
    this.text = this.storeFieldData;
  },
};
</script>