<template>
  <div>


    <v-row>
      <v-col sm="11" md="11" lg="11" xl="11">
        <v-breadcrumbs :items="breadcrumbs">
          <template v-slot:divider>
            <v-icon>mdi-forward</v-icon>
          </template>
        </v-breadcrumbs>
      </v-col>
      <v-col sm="1" md="1" lg="1" xl="1">
        <v-btn
          class="mx-2"
          fab
          dark
          small
          color="pink"
        >
        <v-icon dark>
          mdi-plus
        </v-icon>
      </v-btn>
      </v-col>
    </v-row>

    <v-row v-for="n in Math.ceil(configurables.length / 3)" :key="n">
      <v-col v-for="i in [0,1,2]" :key="i" sm="12" md="6" lg="4" xl="2">
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

    <v-card tile v-for="instance in instances" :key="instance.id">
      <v-list-item two-line>
        <v-list-item-content>
          <v-list-item-title>{{instance.fields['name']}}</v-list-item-title>
          <v-list-item-subtitle>{{instance.fields['description']}}</v-list-item-subtitle>
        </v-list-item-content>
        
    <v-btn icon>
      <v-icon>mdi-pencil</v-icon>
    </v-btn>
          <v-btn icon>
      <v-icon>mdi-delete</v-icon>
    </v-btn>
      </v-list-item>
    </v-card>
  </div>
</template>

<script>
  import { client } from "../rest.js"
  // import store from '../plugins/vuex'
  // import {NEW_INSTANCE, RESET_INSTANCE} from '../plugins/vuex'


  export default {
    data: () => ({
    }),
    computed: {
      breadcrumbs() {
        var breadcrumbs = []

        var selectedClass = this.getConfigurableClazz()
        
        var selectedConfigurable = this.getConfigurableByClazz(selectedClass)
        var isLast = true
        while (selectedConfigurable != null) {
          breadcrumbs.push({
            text: selectedConfigurable.titleRes,
            disabled: isLast,
            href: '/configurables/'+selectedConfigurable.class
          })

          selectedConfigurable = this.getConfigurableByClazz(selectedConfigurable.parentClass)
          isLast = false
        }

        breadcrumbs.push({
          text: 'House',
          disabled: false,
          href: '/configurables/null',
        })

        return breadcrumbs.reverse()

      },

      configurables() {
        var clazz = this.getConfigurableClazz()
        var filterFunction = (clazz === "null") 
          ? function(x) { return x.parentClass == null } 
          : function(x) { return x.parentClass === clazz }
        
        return this.$store.state.configurables.filter(filterFunction);
      },

      instances : function() {
        return this.$store.state.instances
      }
    },
    methods: {
      getConfigurableClazz: function() {
        return this.$route.params.clazz
      },

      getConfigurableByClazz: function(clazz) {
        var result = null
        this.$store.state.configurables.forEach(element => {
          if (element.class == clazz) {
            result = element
          }
        })

        return result
      },

      browse: function(configurable) {
        this.$router.push({ name: 'configurables', params: { clazz: configurable.class } })
        this.refresh()

        // if (configurable.descendants.length > 0) {
        //   this.$router.push({ name: 'configurables', params: { clazz: configurable.class } })
        // } else {
        //   this.$router.push({ name: 'instances', params: { clazz: configurable.class } })
        // }
      },

      refresh() {
        client.getConfigurables();
        client.getInstancesOfClazz(this.getConfigurableClazz());
      }
    },
    mounted: function() {
      this.refresh()
    }
  }
</script>