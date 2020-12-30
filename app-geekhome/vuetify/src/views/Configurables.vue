<template>
  <div>
    <v-breadcrumbs :items="breadcrumbs">
      <template v-slot:divider>
        <v-icon>mdi-forward</v-icon>
      </template>
    </v-breadcrumbs>

    <v-dialog
      v-model="instanceDialog.show"
      persistent
      fullscreen
      transition="dialog-bottom-transition"
    >
      <v-card>
        <v-toolbar dark color="primary">
          <v-btn icon dark @click="closeInstanceDialog()">
            <v-icon>mdi-close</v-icon>
          </v-btn>
          <v-toolbar-title>{{ instanceDialog.title }}</v-toolbar-title>
          <v-spacer></v-spacer>
          <v-toolbar-items>
            <v-btn dark text @click="instanceDialog.action()">{{
              $vuetify.lang.t("$vuetify.configurables.add")
            }}</v-btn>
          </v-toolbar-items>
        </v-toolbar>

        <v-container>
          <v-form ref="form" v-if="configurable != null">
            <component
              v-for="field in configurable.fields"
              :key="field.name"
              :hint="field.hint"
              :counter="field.maxSize"
              :required="field.required"
              :id="field.name"
              :initialValue="null"
              v-bind:is="configurableClassToFormComponent(field.class)"
            >
            </component>
          </v-form>
              <v-overlay :value="formOverlay">
              <v-progress-circular
                indeterminate
                size="64"
              ></v-progress-circular>
            </v-overlay>
        </v-container>
      </v-card>
    </v-dialog>

    <v-row v-for="n in Math.ceil(configurables.length / 3)" :key="n">
      <v-col v-for="i in [0, 1, 2]" :key="i" sm="12" md="6" lg="4" xl="2">
        <v-card v-if="(n - 1) * 3 + i < configurables.length">
          <v-card-title class="headline">
            <div
              style="transform: scale(0.5)"
              v-html="configurables[(n - 1) * 3 + i].iconRaw"
            ></div>
            {{ configurables[(n - 1) * 3 + i].titleRes }}
          </v-card-title>

          <v-card-subtitle>{{
            configurables[(n - 1) * 3 + i].descriptionRes
          }}</v-card-subtitle>

          <v-card-actions>
            <v-btn text @click="browse(configurables[(n - 1) * 3 + i])">
              Browse
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-col>
    </v-row>

    <v-row>
      <v-col>
    </v-row>
    <v-card tile v-for="instance in instances" :key="instance.id" class="mb-12">
      <v-list-item two-line>
        <v-list-item-content>
          <v-list-item-title>{{ instance.fields["name"] }}</v-list-item-title>
          <v-list-item-subtitle>{{
            instance.fields["description"]
          }}</v-list-item-subtitle>
        </v-list-item-content>

        <v-btn icon>
          <v-icon>mdi-pencil</v-icon>
        </v-btn>
        <v-btn icon>
          <v-icon>mdi-delete</v-icon>
        </v-btn>
      </v-list-item>
    </v-card>


    <v-btn
      v-if="configurable !== null && configurable.fields !== null"
      fab
      dark
      large
      color="primary"
      fixed
      right
      bottom
      class="ma-4"
      @click="showAddNewInstanceDialog()"
    >
      <v-icon dark>mdi-plus</v-icon>
    </v-btn>
  </div>
</template>

<script>
import { client } from "../rest.js";
import store from "../plugins/vuex";
import { NEW_INSTANCE, RESET_INSTANCE } from "../plugins/vuex";

export default {
  data: function () {
    return {
      formOverlay: false,
      instanceDialog: {
        show: false,
        title: "title",
        action: function () { }
      },
    };
  },
  computed: {
    breadcrumbs() {
      var breadcrumbs = [];

      var selectedClass = this.getConfigurableClazz();

      var selectedConfigurable = this.getConfigurableByClazz(selectedClass);
      var isLast = true;
      while (selectedConfigurable != null) {
        breadcrumbs.push({
          text: selectedConfigurable.titleRes,
          disabled: isLast,
          href: "/configurables/" + selectedConfigurable.class,
        });

        selectedConfigurable = this.getConfigurableByClazz(
          selectedConfigurable.parentClass
        );
        isLast = false;
      }

      breadcrumbs.push({
        text: "House",
        disabled: false,
        href: "/configurables/null",
      });

      return breadcrumbs.reverse();
    },

    configurables() {
      var clazz = this.getConfigurableClazz();
      var filterFunction =
        clazz === "null"
          ? function (x) {
              return x.parentClass == null;
            }
          : function (x) {
              return x.parentClass === clazz;
            };

      return this.$store.state.configurables.filter(filterFunction);
    },

    instances: function () {
      return this.$store.state.instances;
    },

    configurable: function () {
      var clazz = this.getConfigurableClazz();
      return this.getConfigurableByClazz(clazz);
    },
  },
  methods: {
    getConfigurableClazz: function () {
      return this.$route.params.clazz;
    },

    getConfigurableByClazz: function (clazz) {
      var result = null;
      this.$store.state.configurables.forEach((element) => {
        if (element.class == clazz) {
          result = element;
        }
      });

      return result;
    },

    configurableClassToFormComponent: function (clazz) {
      return "configurable-" + clazz.toLowerCase();
    },

    browse: function (configurable) {
      this.$router.push({
        name: "configurables",
        params: { clazz: configurable.class },
      });
      this.refresh();
    },

    refresh() {
      client.getConfigurables();
      client.getInstancesOfClazz(this.getConfigurableClazz());
    },

    showAddNewInstanceDialog: function () {
      store.commit(NEW_INSTANCE, this.configurable);

      this.instanceDialog = {
        show: true,
        title: "Add new instance",
        action: this.addInstance,
      };
    },

    closeInstanceDialog: function () {
      store.commit(RESET_INSTANCE);
      this.instanceDialog.show = false;
    },

    addInstance: function () {
      this.formOverlay = true
      client.postNewInstance(store.state.newInstance, (validationResult) => {
        var isFormValid = true
        for (const field in validationResult) {
            if (!validationResult[field].valid) {
              isFormValid = false
            }
        }

        if (isFormValid) {
          this.instanceDialog.show = false
        }

        this.formOverlay = false
      });
    },
  },
  mounted: function () {
    this.refresh();
    store.commit(RESET_INSTANCE);
  },
};
</script>