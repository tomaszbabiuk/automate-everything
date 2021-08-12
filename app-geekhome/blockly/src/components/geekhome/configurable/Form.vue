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
  props: ["clazz"],

  computed: {
    configurable: function () {
      return this.getConfigurableByClazz(this.clazz);
    },
  },

  methods: {
    configurableClassToFormComponent: function (clazz) {
      return "configurable-" + clazz.toLowerCase();
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
