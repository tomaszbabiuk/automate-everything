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
        :fieldRef="field.ref"
        :values="field.values"
        v-bind:is="configurableClassToFormComponent(field.type)"
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
    configurableClassToFormComponent: function (type) {
      return "configurable-" + type.toLowerCase() + "field";
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
