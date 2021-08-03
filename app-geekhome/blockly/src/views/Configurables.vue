<template>
  <div class="mb-16">
    <div v-if="loading">
      <v-skeleton-loader
        v-for="i in [0, 1, 2]"
        :key="i"
        class="mx-auto float-left ml-5 mt-5"
        min-width="300"
        type="card"
      ></v-skeleton-loader>
    </div>
    <div v-else>
      <v-breadcrumbs :items="breadcrumbs" v-if="breadcrumbs.length > 1">
        <template v-slot:divider>
          <v-icon>mdi-forward</v-icon>
        </template>
      </v-breadcrumbs>

      <v-dialog
        v-model="instanceDialog.show"
        persistent
        fullscreen
        transition="dialog-bottom-transition"
        :retain-focus="false"
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
                instanceDialog.actionText
              }}</v-btn>
            </v-toolbar-items>
            <template v-slot:extension>
              <v-tabs v-model="instanceDialog.activeTab">
                <v-tab>
                  {{ $vuetify.lang.t("$vuetify.configurables.data") }}
                </v-tab>
                <v-tab v-if="configurable.editableIcon">
                  {{ $vuetify.lang.t("$vuetify.configurables.icon") }}
                </v-tab>
                <v-tab v-if="configurable.taggable">
                  {{ $vuetify.lang.t("$vuetify.configurables.tags") }}
                </v-tab>
                <v-tab v-if="configurable.hasAutomation">
                  {{ $vuetify.lang.t("$vuetify.configurables.automation") }}
                </v-tab>

                <v-tab-item>
                  <configurable-form
                    :clazz="getConfigurableClazz()"
                  ></configurable-form>
                </v-tab-item>
                <v-tab-item v-if="configurable.editableIcon">
                  <configurable-iconselector></configurable-iconselector>
                </v-tab-item>
                <v-tab-item v-if="configurable.taggable">
                  <configurable-tagsselector></configurable-tagsselector>
                </v-tab-item>
                <v-tab-item v-if="configurable.hasAutomation">
                  <configurable-blockconfigurator
                    ref="blockly"
                    :configurableClazz="configurable.clazz"
                    @focus.native="console.log('focus in')"
                  ></configurable-blockconfigurator>
                </v-tab-item>
              </v-tabs>
            </template>
          </v-toolbar>

          <v-container>
            <v-overlay :value="instanceDialog.overlay">
              <v-progress-circular
                indeterminate
                size="64"
              ></v-progress-circular>
            </v-overlay>
          </v-container>
        </v-card>
      </v-dialog>

      <div>
        <v-card
          class="mx-auto float-left ml-5 mt-5 d-flex flex-column"
          max-width="344"
          min-width="344"
          min-height="240"
          v-for="configurable in configurables"
          :key="configurable.clazz"
        >
          <v-card-title class="headline">
            <div
              style="transform: scale(0.7)"
              v-html="configurable.iconRaw"
            ></div>
          </v-card-title>
          <v-card-subtitle class="headline">
            {{ configurable.titleRes }}
          </v-card-subtitle>

          <v-card-subtitle>{{ configurable.descriptionRes }}</v-card-subtitle>

          <v-spacer></v-spacer>
          <v-card-actions>
            <v-btn text @click="browse(configurable)">
              {{ $vuetify.lang.t("$vuetify.common.browse") }}
            </v-btn>
          </v-card-actions>
        </v-card>
      </div>

      <div v-if="canAddInstances()">
        <v-card tile v-for="instance in instances" :key="instance.id">
          <v-list>
            <v-list-item two-line>
              <v-list-item-icon v-if="instance.iconId === null">
                <v-icon x-large left>$vuetify.icon.empty</v-icon>
              </v-list-item-icon>
              <v-list-item-icon v-else>
                <img
                  left
                  :src="'/rest/icons/' + instance.iconId + '/raw'"
                  width="40"
                  height="40"
                />
              </v-list-item-icon>
              <v-list-item-content>
                <v-list-item-title>{{
                  instance.fields["name"]
                }}</v-list-item-title>
                <v-list-item-subtitle class="mb-4"
                  >{{ instance.fields["description"] }}
                </v-list-item-subtitle>
                <v-list-item-subtitle
                  v-for="configurable in configurable.fields"
                  :key="configurable.name"
                  ><div
                    v-if="
                      configurable.name !== 'name' &&
                      configurable.name !== 'description'
                    "
                  >
                    {{ configurable.hint }}:
                    {{ instance.fields[configurable.name] }}
                  </div>
                </v-list-item-subtitle>
              </v-list-item-content>

              <v-btn icon>
                <v-icon @click="showEditInstanceDialog(instance)"
                  >mdi-pencil</v-icon
                >
              </v-btn>
              <v-btn icon>
                <v-icon @click="showDeleteInstanceDialog(instance.id)"
                  >mdi-delete</v-icon
                >
              </v-btn>
            </v-list-item>
          </v-list>
          <v-card-actions v-if="instance.tagIds.length > 0">
            <v-chip
              v-for="tagId in instance.tagIds"
              :key="tagId"
              class="mr-2"
              >{{ findTagName(tagId) }}</v-chip
            >
          </v-card-actions>
        </v-card>

        <div v-if="instances.length == 0" class="text-center">
          {{ $vuetify.lang.t("$vuetify.noDataText") }}
        </div>
      </div>

      <v-dialog v-model="deleteDialog.show" max-width="500px">
        <v-card>
          <v-card-title class="headline">{{
            $vuetify.lang.t("$vuetify.common.delete_question")
          }}</v-card-title>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="blue darken-1" text @click="closeDeleteDialog()">{{
              $vuetify.lang.t("$vuetify.common.cancel")
            }}</v-btn>
            <v-btn color="blue darken-1" text @click="deleteDialog.action()">{{
              $vuetify.lang.t("$vuetify.common.ok")
            }}</v-btn>
            <v-spacer></v-spacer>
          </v-card-actions>
        </v-card>
      </v-dialog>

      <v-btn
        v-if="canAddInstances()"
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
  </div>
</template>

<script>
import { client } from "../rest.js";
import store from "../plugins/vuex";
import {
  CLEAR_INSTANCES,
  RESET_INSTANCE,
  EDIT_INSTANCE,
} from "../plugins/vuex";

export default {
  data: function () {
    return {
      loading: true,
      instanceDialog: {
        show: false,
        title: "",
        action: function () {},
        activeTab: 0,
        overlay: false,
        actionText: "",
      },
      deleteDialog: {
        show: false,
        action: function () {},
        instanceId: null,
        iconCategoryId: null,
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
          href: "/configurables/" + selectedConfigurable.clazz,
        });

        selectedConfigurable = this.getConfigurableByClazz(
          selectedConfigurable.parentClazz
        );
        isLast = false;
      }

      breadcrumbs.push({
        text: this.$vuetify.lang.t("$vuetify.navigation.objects"),
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
              return x.parentClazz == null;
            }
          : function (x) {
              return x.parentClazz === clazz;
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
    extractOtherFields: function (instance) {
      var result = {};
      Object.entries(instance.fields).forEach(([key, value]) => {
        if (key !== "name" && key != "description") {
          result[key] = value;
        }
      });

      return result;
    },

    extractFieldDefinition: function (fieldName) {
      return this.configurable.fields.filter((element) => {
        return element.name === fieldName;
      });
    },

    canAddInstances: function () {
      return this.configurable !== null && this.configurable.fields !== null;
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

    browse: async function (configurable) {
      this.$router.push({
        name: "configurables",
        params: { clazz: configurable.clazz },
      });
      await this.refresh();
    },

    refreshConfigurables: async function () {
      return client.getConfigurables();
    },

    refreshInstances: async function () {
      return client.getInstancesOfClazz(this.getConfigurableClazz());
    },

    refreshTags: async function () {
      return client.getTags();
    },

    refreshPorts: async function () {
      return client.getPorts();
    },

    refresh: async function () {
      var that = this;
      await Promise.all([
        this.refreshConfigurables(),
        this.refreshInstances(),
        this.refreshTags(),
        this.refreshPorts(),
      ]).then(() => (that.loading = false));
    },

    findTagName: function (tagId) {
      var result = tagId;
      this.$store.state.tags.forEach((category) => {
        category.children.forEach((tag) => {
          if (tag.id === tagId) {
            result = tag.name;
            return true;
          }
        });
      });

      return result;
    },

    showAddNewInstanceDialog: function () {
      store.commit(RESET_INSTANCE, this.configurable);

      this.instanceDialog.show = true;
      this.instanceDialog.title = this.configurable.addNewRes;
      this.instanceDialog.actionText = this.$vuetify.lang.t(
        "$vuetify.configurables.add"
      );
      this.instanceDialog.action = this.addInstance;
      this.instanceDialog.activeTab = 0;
    },

    closeInstanceDialog: function () {
      this.instanceDialog.show = false;
    },

    showDeleteInstanceDialog: function (instanceId) {
      this.deleteDialog = {
        action: this.deleteInstance,
        instanceId: instanceId,
        show: true,
      };
    },

    closeDeleteDialog: function () {
      this.deleteDialog.show = false;
    },

    showEditInstanceDialog: function (instance) {
      this.instanceDialog.show = true;
      this.instanceDialog.title = this.configurable.editRes;
      this.instanceDialog.actionText = this.$vuetify.lang.t(
        "$vuetify.configurables.edit"
      );
      this.instanceDialog.action = this.editInstance;
      this.instanceDialog.activeTab = 0;
      this.instanceDialog.instance = instance;

      setTimeout(() => {
        store.commit(RESET_INSTANCE, this.configurable);
        store.commit(EDIT_INSTANCE, instance);
      }, 500);

      if (this.$refs.blockly != null) {
        this.$refs.blockly.reloadBlocks(instance.automation);
      }
    },

    handleValidationResult: async function (validationResult) {
      var isFormValid = true;
      for (const field in validationResult) {
        if (!validationResult[field].valid) {
          isFormValid = false;
        }
      }

      if (isFormValid) {
        this.instanceDialog.show = false;
        this.loading = true;
        await this.refreshInstances();
        this.loading = false;
      } else {
        this.instanceDialog.activeTab = 0;
      }

      this.instanceDialog.overlay = false;
    },

    addInstance: function () {
      this.instanceDialog.overlay = true;

      client.postNewInstance(
        store.state.newInstance,
        this.handleValidationResult
      );
    },

    editInstance: function () {
      this.instanceDialog.overlay = true;
      client.putInstance(store.state.newInstance, this.handleValidationResult);
    },

    deleteInstance: function () {
      var instanceId = this.deleteDialog.instanceId;
      client.deleteInstance(instanceId);
      this.closeDeleteDialog();
    },
  },
  watch: {
    $route() {
      this.$store.commit(CLEAR_INSTANCES);
    },
  },
  mounted: async function () {
    await this.refresh();
  },
};
</script>