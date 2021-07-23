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
        <!-- <div>
<v-select
          :items="automationUnit.evaluationResult.nextStates"
          item-text="name"
          label="Change state..."
          outlined
          dense
          solo
        ></v-select>
</div> -->

        <div v-if="automationUnit.evaluationResult.nextStates != null">
          <v-btn-toggle
            dense
            v-model="selectedStates['instance_' + automationUnit.instance.id]"
          >
            <v-btn
              v-for="state in automationUnit.evaluationResult.nextStates.states"
              :key="state.name"
              :elevated="state.id == 'on'"
              @click="changeState(automationUnit.instance, state)"
              >{{ state.name }}</v-btn
            >
          </v-btn-toggle>
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
import Vue from "vue";

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
        return "blue lighten-1";
      }

      return "white";
    },

    matchMinWidth(automationUnit) {
      if (
        automationUnit.evaluationResult.nextStates != null &&
        automationUnit.evaluationResult.nextStates.extendedWidth
      ) {
        return "708";
      }

      return "344";
    },

    changeState(instance, state) {
      console.log("Changing state " + instance.id + " " + state.id);
      client.changeState(instance.id, state.id);
    },

    findStateIndex(automationUnit, stateId) {
      var index = 0;
      var result = -1;
      automationUnit.evaluationResult.nextStates.states.forEach((element) => {
        if (element.id == stateId) {
          result = index;
          return
        }

        index++;
      })

      return result;
    }
  },

  mounted: async function () {
    var that = this;
    await client.getAutomationUnits().then(function () {
      that.loading = false;
    });

    this.automationUnits.forEach((element) => {
      if (element.evaluationResult.nextStates != null) {
        var stateId = element.evaluationResult.nextStates.current;
        var stateIndex = this.findStateIndex(element, stateId);
        Vue.set(that.selectedStates, "instance_" + element.instance.id, stateIndex);
      } else {
        Vue.set(that.selectedStates, "instance_" + element.instance.id, -1);
      }
    });
  },
};
</script>
