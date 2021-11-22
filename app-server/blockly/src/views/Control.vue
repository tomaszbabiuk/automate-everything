<template>
  <div>
    <div v-if="loading">
      <v-skeleton-loader
        v-for="i in [0, 1, 2]"
        :key="i"
        class="mx-auto float-left ml-5 mt-5"
        min-width="300"
        type="card"
      ></v-skeleton-loader>
    </div>
    <v-card
      v-else
      class="mx-auto float-left ml-5 mt-5"
      :min-width="matchMinWidth(automationUnit)"
      min-height="164"
      v-for="automationUnit in automationUnits"
      :key="automationUnit.id"
      :color="matchColor(automationUnit)"
    >

      <devicecontrol :automationUnit="automationUnit" />

    </v-card>
    <div v-if="automationUnits.length == 0 && !loading" class="text-center">
      {{ $vuetify.lang.t("$vuetify.noDataText") }}
    </div>
  </div>
</template>

<script>
import { client } from "../rest.js";

export default {
  data: function () {
    return {
      loading: true,
      selectedStates: {},
    };
  },

  computed: {
    automationUnits() {
      return this.$store.state.automationUnits;
    },

    automation() {
      return this.$store.state.automation;
    },
  },

  methods: {
    extractFieldDefinition: function(fieldName) {
      return this.configurable.fields.filter((element) => {
        return element.name === fieldName;
      });
    },

    matchColor: function(automationUnit) {
      if (automationUnit.evaluationResult.error != null) {
        return "orange";
      }

      if (automationUnit.evaluationResult.isSignaled) {
        return "blue lighten-1";
      }

      return "white";
    },

    matchMinWidth: function(automationUnit) {
      if (
        automationUnit.evaluationResult.nextStates != null &&
        automationUnit.evaluationResult.nextStates.extendedWidth
      ) {
        return "708";
      }

      return "344";
    },

    refresh: async function () {
      var that = this;
      await client.getAutomationUnits().then(function () {
        that.loading = false;
      });
    },
  },

  watch: {
    automation: async function() {
        await this.refresh();
    }
  },

  mounted: async function () {
    await this.refresh();
  },
};
</script>
