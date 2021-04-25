<template>
  <div>
    <v-row v-for="n in Math.ceil(automationUnits.length / 3)" :key="n">
      <v-col v-for="i in [0, 1, 2]" :key="i" sm="12" md="6" lg="4" xl="2">
        <v-card
          :color="matchColor(automationUnits[(n - 1) * 3 + i])"
          v-if="(n - 1) * 3 + i < automationUnits.length"
        >
          <v-list-item three-line>
            <v-list-item-content>
              <v-list-item-title class="headline mb-1">
                {{
                  automationUnits[(n - 1) * 3 + i].evaluationResult
                    .interfaceValue
                }}
              </v-list-item-title>
              <v-list-item-subtitle>{{
                automationUnits[(n - 1) * 3 + i].instance.fields["name"]
              }}</v-list-item-subtitle>
            </v-list-item-content>

            <v-list-item-avatar
              v-if="automationUnits[(n - 1) * 3 + i].instance.iconId === null"
              tile
              size="80"
            >
              <v-icon x-large right>$vuetify.icon.empty</v-icon>
            </v-list-item-avatar>
            <v-list-item-avatar tile size="80" v-else>
              <img
                left
                :src="
                  '/rest/icons/' +
                  automationUnits[(n - 1) * 3 + i].instance.iconId +
                  '/raw'
                "
                width="50"
                height="50"
              />
            </v-list-item-avatar>
          </v-list-item>
          <v-card-actions >
            <div v-if="automationUnits[(n - 1) * 3 + i].evaluationResult.nextStates != null">
              <v-btn v-for="state in automationUnits[(n - 1) * 3 + i].evaluationResult.nextStates" :key="state.id">{{state.name}}</v-btn>
            </div>
            <v-spacer></v-spacer>

            <v-btn icon @click="automationUnits[(n - 1) * 3 + i].show = !automationUnits[(n - 1) * 3 + i].show">
              <v-icon>{{
                automationUnits[(n - 1) * 3 + i].show ? "mdi-chevron-up" : "mdi-chevron-down"
              }}</v-icon>
            </v-btn>
          </v-card-actions>
          <v-expand-transition>
      <div v-show="automationUnits[(n - 1) * 3 + i].show">
        <v-divider></v-divider>

        <v-card-text>
          (no descriptions)
        </v-card-text>
      </div>
    </v-expand-transition>
        </v-card>
      </v-col>
    </v-row>
  </div>
</template>

<script>
import { client } from "../rest.js";

export default {
  data: function () {
    return {};
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
      if (automationUnit.condition === "InitError") {
        return "orange";
      }

      if (automationUnit.condition === "AutomationError") {
        return "orange";
      }

      return "white";
    },
  },
  mounted: function () {
    client.getAutomationUnits();
  },
};
</script>
