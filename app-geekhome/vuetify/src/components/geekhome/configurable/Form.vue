<template>
  <div>
    <v-form ref="form" v-if="configurable != null">
      <component
        class="ma-4"
        v-for="field in configurable.fields"
        :key="field.name"
        :hint="field.hint"
        :counter="field.maxSize"
        :required="field.required"
        :id="field.name"
        v-bind:is="configurableClassToFormComponent(field.clazz)"
      >
      </component>
    </v-form>
  </div>
</template>

<script>
export default {
  computed: {
    configurable: function () {
      var clazz = this.getConfigurableClazz();
      return this.getConfigurableByClazz(clazz);
    },
  },
  methods: {
    configurableClassToFormComponent: function (clazz) {
      return "configurable-" + clazz.toLowerCase();
    },

    getConfigurableClazz: function () {
      return this.$route.params.clazz;
    },

    getConfigurableByClazz: function (clazz) {
      var result = null;
      this.$store.state.configurables.forEach((element) => {
        if (element.clazz == clazz) {
          result = element;
        }
      });

      return result;
    },
  },
};
</script>
