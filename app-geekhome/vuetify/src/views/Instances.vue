<template>
  <div>
    <v-breadcrumbs :items="breadcrumbs">
      <template v-slot:divider>
        <v-icon>mdi-forward</v-icon>
      </template>
    </v-breadcrumbs>
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
      instances : function() {
        return this.$store.state.instances
      }
    },
    methods: {
      getConfigurableClazz: function() {
        return this.$route.params.clazz
      }
    },
    mounted: function() {
      client.getInstancesOfClazz(this.getConfigurableClazz());
    }
  }
</script>