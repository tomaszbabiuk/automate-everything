<template>
  <div>
    <v-treeview
    dense
    :items="configurables"
    item-key="class"
    item-text="class"
  ></v-treeview>
  </div>
</template>

<script>
import { client } from "../rest.js"

export default {
  data: function() {
    return {
      items: [
        { clazz: "name1", id: "id1" },
        { clazz: "name2", id: "id2" },
      ]
    }
  },
  computed: {
    configurables () {
      return this.$store.state.configurables
    }
  },
  mounted: function() {
    client.getRootConfigurables()
  }
};
</script>