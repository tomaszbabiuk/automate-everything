<template>
  <div class="control">
    <h1 class="subheading grey--text">Control</h1>
    <v-container class="my-5">
        <v-layout row wrap>
          <v-flex md4>
            <material-stats-card
              color="green"
              icon="$vuetify.icon.bell"
              title="Revenue"
              value="$34,245"
              sub-icon="$vuetify.icon.bell"
              sub-text="Last 24 Hours"
            >
            <v-card-actions>
              <v-btn text>Button</v-btn>
              <v-btn text>Button</v-btn>
            </v-card-actions>
            </material-stats-card>
          </v-flex>
        </v-layout>

    </v-container>
  </div>
</template>
