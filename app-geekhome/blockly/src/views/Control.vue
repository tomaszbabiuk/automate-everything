<template>
  <div>
    <v-row v-for="n in Math.ceil(automationUnits.length / 3)" :key="n">
      <v-col v-for="i in [0, 1, 2]" :key="i" sm="12" md="6" lg="4" xl="2">
        <v-card :color="matchColor(automationUnits[(n - 1) * 3 + i])" v-if="(n - 1) * 3 + i < automationUnits.length">
          <v-card-title class="headline">
            {{ automationUnits[(n - 1) * 3 + i].instance.fields["name"] }}
            <v-spacer />
            <v-list-item-icon v-if="automationUnits[(n - 1) * 3 + i].instance.iconId === null">
              <v-icon x-large right>$vuetify.icon.empty</v-icon>
            </v-list-item-icon>
            <v-list-item-icon v-else>
              <img
                left
                :src="'/rest/icons/' + automationUnits[(n - 1) * 3 + i].instance.iconId + '/raw'"
                width="50"
                height="50"
              />
            </v-list-item-icon>
          </v-card-title>

          <v-card-subtitle>{{
            automationUnits[(n - 1) * 3 + i].evaluationResult.interfaceValue
          }}</v-card-subtitle>

          <v-card-actions>
            <v-btn text @click="browse(automationUnits[(n - 1) * 3 + i])">
              Browse
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-col>
    </v-row>
  </div>
</template>

<script>
import { client } from "../rest.js";

export default {
  data: function () {
    return {
    };
  },
  computed: {
    automationUnits() {
      return this.$store.state.automationUnits;
    },
  },
  methods: {
    extractFieldDefinition: function(fieldName) {
      return this.configurable.fields.filter((element) => {
        return element.name === fieldName
      })
    },

    matchColor(automationUnit) {
      if (automationUnit.state === "InitError") {
        return "orange"
      }

      if (automationUnit.state === "AutomationError") {
        return "orange"
      }

      return "white"
    }
  },
  mounted: function () {
    client.getAutomationUnits();
  },
};
</script>
