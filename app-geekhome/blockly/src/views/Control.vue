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
      max-width="344"
      v-for="automationUnit in automationUnits"
      :key="automationUnit.id"
      :color="matchColor(automationUnit)"
    >
      <v-list-item three-line>
        <v-list-item-content>
          <v-list-item-title class="headline mb-1">
            {{ automationUnit.evaluationResult.interfaceValue }}
          </v-list-item-title>
          <v-list-item-subtitle>{{
            automationUnit.instance.fields["name"]
          }}</v-list-item-subtitle>
        </v-list-item-content>

        <v-list-item-avatar
          v-if="automationUnit.instance.iconId === null"
          tile
          size="80"
        >
          <v-icon x-large right>$vuetify.icon.empty</v-icon>
        </v-list-item-avatar>
        <v-list-item-avatar tile size="80" v-else>
          <img
            left
            :src="'/rest/icons/' + automationUnit.instance.iconId + '/raw'"
            width="50"
            height="50"
          />
        </v-list-item-avatar>
      </v-list-item>
      <v-card-actions>
        <div v-if="automationUnit.evaluationResult.nextStates != null">
          <v-btn
            v-for="state in automationUnit.evaluationResult.nextStates"
            :key="state.id"
            @click="changeState(automationUnit.instance, state)"
            >{{ state.name }}</v-btn
          >
        </div>
        <v-spacer></v-spacer>
        <v-btn
          icon
          @click="automationUnit.show = !automationUnit.show"
          v-if="automationUnit.evaluationResult.descriptions.length > 0"
        >
          <v-icon>{{
            automationUnit.show ? "mdi-chevron-up" : "mdi-chevron-down"
          }}</v-icon>
        </v-btn>
      </v-card-actions>
      <v-expand-transition>
        <div
          v-show="automationUnit.show"
          v-if="automationUnit.evaluationResult.descriptions.length > 0"
        >
          <v-divider></v-divider>
          <v-card-text>
            <div
              v-for="(description, index) in automationUnit.evaluationResult
                .descriptions"
              :key="index"
            >
              {{ description }}
            </div>
          </v-card-text>
        </div>
      </v-expand-transition>
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
    };
  },
  
  computed: {
    automationUnits() {
      return this.$store.state.automationUnits;
    },
  },

  methods: {
    extractFieldDefinition: function (fieldName) {
      return this.configurable.fields.filter((element) => {
        return element.name === fieldName;
      });
    },

    matchColor(automationUnit) {
      if (automationUnit.evaluationResult.error != null) {
        return "orange";
      }

      if (automationUnit.evaluationResult.isSignaled) {
        return "blue";
      }

      return "white";
    },

    changeState(instance, state) {
      console.log("Changing state " + instance.id + " " + state.id);
      client.changeState(instance.id, state.id);
    },
  },

  mounted: async function () {
    var that = this;
    await client.getAutomationUnits().then(function () {
      that.loading = false;
    });
  },
};
</script>
