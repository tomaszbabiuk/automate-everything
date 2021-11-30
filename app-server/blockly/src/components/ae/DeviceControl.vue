<template>
  <div>
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
    <v-list-item v-if="automationUnit.type == 'Controller'">
      <v-list-item-content class="pt-9 pb-0">
        <div>
          <v-slider
            v-model="level"
            hint="Changing"
            :max="automationUnit.valueRange.max"
            :min="automationUnit.valueRange.min"
            :step="automationUnit.valueRange.step"
            thumb-label
          ></v-slider>
        </div>
      </v-list-item-content>
    </v-list-item>
    <v-card-actions>
      <div v-if="automationUnit.type == 'States'">
        <div v-if="automationUnit.evaluationResult.nextStates != null">
          <v-btn
            v-for="state in automationUnit.evaluationResult.nextStates.states"
            :key="state.id"
            text
            :color="
              automationUnit.evaluationResult.isSignaled ? 'black' : 'primary'
            "
            :disabled="
              state.id == automationUnit.evaluationResult.nextStates.current
            "
            @click="changeState(automationUnit.instance, state)"
            >{{ state.action }}</v-btn
          >
        </div>
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
  </div>
</template>
<script>
import { client } from "../../rest.js";

export default {
  data: function () {
    return {
      level: 0,
      levelThrottlingTimeout: null,
    };
  },

  props: ["automationUnit"],

  computed: {
    automationUnitInStore() {
      return this.$store.state.automationUnits.find((element) => {
        return element.instance.id == this.automationUnit.instance.id;
      });
    },
  },

  watch: {
    level() {
      clearTimeout(this.levelThrottlingTimeout);
      this.levelThrottlingTimeout = setTimeout(
        this.controlLevel,
        200
      );
    },

    automationUnitInStore() {
      this.level = this.automationUnitInStore.evaluationResult.decimalValue
    }
  },

  methods: {
    controlLevel: function () {
      if (
        this.automationUnit.evaluationResult.decimalValue != this.level
      ) {
        client.control(
          this.automationUnit.instance.id,
          this.level
        );
      }
    },

    changeState: function (instance, state) {
      client.changeState(instance.id, state.id);
    },
  },

  mounted: function () {
    this.level = this.automationUnit.evaluationResult.decimalValue;
  },
};
</script>