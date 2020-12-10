<template>
  <div>
    <v-breadcrumbs :items="breadcrumbs">
      <template v-slot:divider>
        <v-icon>mdi-forward</v-icon>
      </template>
    </v-breadcrumbs>

      <v-row v-for="n in Math.ceil(configurables.length / 3)" :key="n">
        <v-col v-for="i in [0,1,2]" :key="i" sm=12 md="6" lg="4" xl="2">
          <v-card v-if="(n-1)*3+i<configurables.length">
            <v-card-title class="headline">
              <div style="transform: scale(0.5);" v-html="configurables[(n-1)*3+i].iconRaw"></div>
              {{configurables[(n-1)*3+i].titleRes}}
            </v-card-title>

            <v-card-subtitle>{{configurables[(n-1)*3+i].descriptionRes}}</v-card-subtitle>

            <v-card-actions>
              <v-btn text  @click="browse(configurables[(n-1)*3+i])">
                Browse
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-col>
      </v-row>
  </div>
</template>

<script>
  import { client } from "../rest.js"
  // import store from '../plugins/vuex'
  // import {NEW_INSTANCE, RESET_INSTANCE} from '../plugins/vuex'


  export default {
    data: () => ({
      breadcrumbs: [
        {
          text: 'House',
          disabled: false,
          href: '/configurables/0',
        },
        {
          text: 'Devices',
          disabled: false,
          href: 'breadcrumbs_link_1',
        },
        {
          text: 'On/Off Devices',
          disabled: true,
          href: 'breadcrumbs_link_2',
        },
      ],
    }),
    computed: {
      configurableChunks() {
        return this.chunkArray(this.$store.state.configurables);
      },
      configurables() {
        return this.$store.state.configurables;
      }
    },
    methods: {
      browse: function(configurable) {
        if (configurable.descendants.length > 0) {
          this.$router.push({ name: 'configurables', params: { clazz: configurable.class } })
        } else {
          this.$router.push({ name: 'instances', params: { clazz: configurable.class } })
        }
      }
    },
    mounted: function() {
      client.getConfigurables();
    }
  }
</script>