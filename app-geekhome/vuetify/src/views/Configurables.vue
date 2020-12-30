<template>
  <div class="mb-16">
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
          <template v-slot:extension>
            <v-tabs v-model="instanceDialog.activeTab">
              <v-tab> Data </v-tab>
              <v-tab> Icon </v-tab>
              <v-tab> Tags </v-tab>

              <v-tab-item class="ma-4">
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
              </v-tab-item>
              <v-tab-item>
                <v-simple-table>
                  <template v-slot:default>
                    <thead>
                      <tr>
                        <th class="text-left">Kategoria</th>
                        <th class="text-left">Ikony</th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr v-for="item in iconCategories" :key="item.id">
                        <td>{{ item.name }}</td>
                        <td>
                          <v-chip
                            v-for="iconId in item.iconIds"
                            :key="iconId"
                            class="ma-2"
                            x-large
                            @click="
                              openEditIconDialog({
                                iconId: iconId,
                                iconCategoryId: item.id,
                              })
                            "
                            label
                            outlined
                          >
                            <img
                              :key="componentKey"
                              left
                              :src="
                                '/rest/icons/' +
                                iconId +
                                '/raw?' +
                                item.refreshCounter
                              "
                              width="50"
                              height="50"
                            />
                          </v-chip>
                        </td>
                      </tr>
                    </tbody>
                  </template>
                </v-simple-table>
              </v-tab-item>
            </v-tabs>
          </template>
        </v-toolbar>

        <v-container>
          <v-overlay :value="instanceDialog.overlay">
            <v-progress-circular indeterminate size="64"></v-progress-circular>
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

    <v-card tile v-for="instance in instances" :key="instance.id">
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
      instanceDialog: {
        show: false,
        title: "title",
        action: function () {},
        activeTab: 0,
        overlay: false,
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

    iconCategories: function () {
      return this.$store.state.iconCategories;
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

      this.instanceDialog.show = true;
      this.instanceDialog.title = "Add new instance";
      this.instanceDialog.action = this.addInstance;
      this.instanceDialog.activeTab = 0;
    },

    closeInstanceDialog: function () {
      store.commit(RESET_INSTANCE);
      this.instanceDialog.show = false;
    },

    addInstance: function () {
      this.instanceDialog.overlay = true;
      client.postNewInstance(store.state.newInstance, (validationResult) => {
        var isFormValid = true;
        for (const field in validationResult) {
          if (!validationResult[field].valid) {
            isFormValid = false;
          }
        }

        if (isFormValid) {
          this.instanceDialog.show = false;
        } else {
          this.instanceDialog.activeTab = 0;
        }

        this.instanceDialog.overlay = false;
      });
    },
  },
  mounted: function () {
    this.refresh();
    store.commit(RESET_INSTANCE);

    client.getIconCategories();
  },
};
</script>