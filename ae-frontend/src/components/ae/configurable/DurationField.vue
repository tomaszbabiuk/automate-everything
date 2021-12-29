<template>
  <div>
    <v-dialog
      ref="dialog"
      v-model="modal"
      :return-value.sync="time"
      persistent
      width="290px"
    >
      <template v-slot:activator="{ on, attrs }">
        <v-text-field
          v-model="time"
          :label="hint"
          :error="error"
          :error-messages="errorMessages"
          prepend-icon="mdi-clock-time-four-outline"
          readonly
          v-bind="attrs"
          v-on="on"
        ></v-text-field>
      </template>
      <v-time-picker
        v-if="modal"
        v-model="time"
        full-width
        format="24hr"
        use-seconds
      >
        <v-spacer></v-spacer>
        <v-btn text color="primary" @click="modal = false">
          {{ $vuetify.lang.t("$vuetify.common.cancel") }}
        </v-btn>
        <v-btn text color="primary" @click="$refs.dialog.save(time)">
          {{ $vuetify.lang.t("$vuetify.common.ok") }}
        </v-btn>
      </v-time-picker>
    </v-dialog>
  </div>
</template>

<script>
import { UPDATE_INSTANCE_FIELD } from "../../../plugins/vuex";

export default {
  data: function () {
    return {
      modal: false,
      error: false,
      errorMessages: [],
      time: "00:00:00",
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

  methods: {
    storeTime: function(val) {
      this.$store.commit(UPDATE_INSTANCE_FIELD, {
        name: this.id,
        value: val,
      });
    }
  },

  watch: {
    validation(val) {
      this.error = !val.valid;
      this.errorMessages = val.reasons;
    },

    storeFieldData(value) {
      if (this.time != value) {
        this.time = value;
      }
    },

    time(value) {
      this.storeTime(value);
    },
  },

  mounted: function () {
    this.time = this.storeFieldData;
  },
};
</script>