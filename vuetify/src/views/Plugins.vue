<template>
  <v-card flat class="pa-5">
    <v-layout row wrap justify-end>
      <v-flex md8 sm12>
        <div class="caption grey--text">Name</div>
        <div>Shelly sth</div>
      </v-flex>
      <v-flex md3 sm8>
        <div class="caption grey--text">Version</div>
        <div>1.0</div>
      </v-flex>
      <v-flex md1 sm4>
        <v-chip outlined @click="blinds" color="green">Active</v-chip>
      </v-flex>
      <v-flex md12 sm12>
        <div class="caption grey--text">Description</div>
        <div
          class="text-justify"
        >Lorem ipsum dolor sit amet consectetur adipisicing elit. Minima quos accusantium, quasi quae quidem aspernatur accusamus, fuga, voluptatum a maxime modi cum earum ipsa eveniet odit unde et eius error.</div>
      </v-flex>
    </v-layout>
  </v-card>
</template>

<script>
export default {
  methods: {
    blinds() {
      alert("Toggling Blinds...");
    },
  }
};
</script>