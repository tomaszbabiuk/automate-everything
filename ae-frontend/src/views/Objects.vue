<template>
  <div class="mb-16">
    <div v-if="loading">
      <v-skeleton-loader v-for="i in [0, 1, 2]" :key="i" class="mx-auto float-left ml-5 mt-5" min-width="300"
        type="card"></v-skeleton-loader>
    </div>
    <div v-else>
      <v-breadcrumbs :items="breadcrumbs" v-if="breadcrumbs.length > 1">
        <template v-slot:divider>
          <v-icon>mdi-forward</v-icon>
        </template>
      </v-breadcrumbs>

      <v-dialog v-model="instanceDialog.show" persistent fullscreen transition="dialog-bottom-transition"
        :retain-focus="false">
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
                  {{ $vuetify.lang.t("$vuetify.objects.data") }}
                </v-tab>
                <v-tab v-if="configurable.editableIcon">
                  {{ $vuetify.lang.t("$vuetify.objects.icon") }}
                </v-tab>
                <v-tab v-if="configurable.taggable">
                  {{ $vuetify.lang.t("$vuetify.objects.tags") }}
                </v-tab>
                <v-tab v-if="configurable.hasAutomation">
                  {{ $vuetify.lang.t("$vuetify.objects.automation") }}
                </v-tab>

                <v-tab-item>
                  <configurable-form :clazz="getConfigurableClazz()"></configurable-form>
                </v-tab-item>
                <v-tab-item v-if="configurable.editableIcon">
                  <configurable-iconselector></configurable-iconselector>
                </v-tab-item>
                <v-tab-item v-if="configurable.taggable">
                  <configurable-tagsselector></configurable-tagsselector>
                </v-tab-item>
                <v-tab-item v-if="configurable.hasAutomation">
                  <configurable-blockconfigurator ref="blockly" :configurableClazz="configurable.clazz"
                    @focus.native="console.log('focus in')"></configurable-blockconfigurator>
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

      <v-container v-if="canAddInstances()">
        <v-card tile v-for="instance in filteredInstances" :key="instance.id">
          <v-list v-if="configurable">
            <v-list-item two-line>
              <v-list-item-icon v-if="instance.iconId === null && configurable.editableIcon">
                <v-icon x-large left>$vuetify.icon.empty</v-icon>
              </v-list-item-icon>
              <v-list-item-icon v-if="instance.iconId !== null && configurable.editableIcon">
                <img left :src="'/rest/icons/' + instance.iconId + '/raw'" width="40" height="40" />
              </v-list-item-icon>
              <v-list-item-content>
                <v-list-item-title v-if="instance.fields['name'] != null">{{
                    instance.fields["name"]
                }}</v-list-item-title>
                <v-list-item-subtitle v-if="instance.fields['description'] != null" class="mb-4">{{
                    instance.fields["description"]
                }}
                </v-list-item-subtitle>
                <v-list-item-subtitle v-for="field in configurable.fields" :key="field.name">
                  <div v-if="field.type === 'QrCode' && instance.fields[field.name].length > 0">
                    <p class="text-center">
                      {{ field.hint }}
                    </p>
                    <p class="text-center">
                      <external-qrcode :value="instance.fields[field.name]" size="150"></external-qrcode>
                    </p>
                  </div>

                  <div v-if="
                    field.name !== 'name' &&
                    field.name !== 'description' &&
                    field.type !== 'QrCode'
                  ">
                    {{ field.hint }}:
                    {{ displayField(field, instance.fields[field.name]) }}
                  </div>
                </v-list-item-subtitle>
              </v-list-item-content>

              <v-btn icon v-if="!configurable.generated">
                <v-icon @click="showEditInstanceDialog(instance)">mdi-pencil</v-icon>
              </v-btn>
              <v-btn icon>
                <v-icon @click="showDeleteInstanceDialog(instance.id)">mdi-delete</v-icon>
              </v-btn>
            </v-list-item>
          </v-list>
          <v-card-actions v-if="instance.tagIds.length > 0">
            <v-chip v-for="tagId in instance.tagIds" :key="tagId" class="mr-2">{{ findTagName(tagId) }}</v-chip>
          </v-card-actions>
        </v-card>

        <div v-if="filteredInstances.length == 0" class="text-center">
          {{ $vuetify.lang.t("$vuetify.noDataText") }}
        </div>
      </v-container>

      <v-container v-else>
        <v-row>
          <v-card class="mx-auto float-left ml-5 mt-5 d-flex flex-column" max-width="344" min-width="344"
            v-for="configurable in configurables" :key="configurable.clazz">
            <v-card-title class="headline d-flex flex-row justify-space-between align-stretch">
              <div style="transform: scale(0.7)" v-html="configurable.iconRaw"></div>
              <v-tooltip bottom class="n5">
                <template v-slot:activator="{ on, attrs }">
                  <v-btn icon v-bind="attrs" v-on="on" class="align-baseline">
                    <v-icon>mdi-information-outline</v-icon>
                  </v-btn>
                </template>
                <span> {{ configurable.descriptionRes }}</span>
              </v-tooltip>
            </v-card-title>

            <v-card-subtitle class="headline">
              {{ configurable.titleRes }}
            </v-card-subtitle>

            <v-spacer></v-spacer>
            <v-card-actions>
              <v-btn text @click="browse(configurable)">
                {{ $vuetify.lang.t("$vuetify.common.browse") }}
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-row>
        <v-row>
          <v-btn-toggle v-model="groupingMode" class="ma-5">
            <v-btn>
              <v-icon large>$vuetify.icon.grouping_separately</v-icon>
            </v-btn>
            
            <v-btn>
              <v-icon large>$vuetify.icon.grouping_together</v-icon>
            </v-btn>
          </v-btn-toggle>
        </v-row>
      </v-container>


      <v-dialog v-model="deleteDialog.show" max-width="500px">
        <v-card>
          <v-card-title class="headline">{{
              $vuetify.lang.t("$vuetify.common.delete_question")
          }}</v-card-title>
          <v-card-subtitle v-if="dependencies.length > 0">
            {{ $vuetify.lang.t("$vuetify.objects.dependencies_detected_note") }}
          </v-card-subtitle>
          <v-card-text class="text-center">
            <v-progress-circular v-if="deleteDialog.loading" indeterminate size="64"></v-progress-circular>

            <v-chip v-for="dependency in dependencies" :key="dependency.id" class="ma-2" color="red" text-color="white">
              {{ dependency.name }}
            </v-chip>
          </v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="blue darken-1" text @click="closeDeleteDialog()">{{
                $vuetify.lang.t("$vuetify.common.cancel")
            }}</v-btn>
            <v-btn color="blue darken-1" text @click="deleteDialog.action()" :disabled="deleteDialog.loading">{{
                $vuetify.lang.t("$vuetify.common.ok")
            }}</v-btn>
            <v-spacer></v-spacer>
          </v-card-actions>
        </v-card>
      </v-dialog>

      <v-btn v-if="canAddInstances()" fab dark large color="primary" fixed right bottom class="ma-4"
        @click="addOrGenerateNewInstance()">
        <v-icon dark>mdi-plus</v-icon>
      </v-btn>
    </div>
  </div>
</template>

<script>
import { client } from "../rest.js";
import { temp } from "../temp.js";

import store, {
  CLEAR_INSTANCES,
  CLEAR_DEPENDENCIES,
  RESET_INSTANCE,
  EDIT_INSTANCE,
} from "../plugins/vuex";

export default {
  data: function () {
    return {
      groupingMode: 0,
      loading: true,
      instanceDialog: {
        show: false,
        title: "",
        action: function () {
          // this is intentional
        },
        activeTab: 0,
        overlay: false,
        actionText: "",
      },
      deleteDialog: {
        loading: false,
        show: false,
        action: function () {
          //this is intentional
        },
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
          href: "/objects/" + selectedConfigurable.clazz,
        });

        selectedConfigurable = this.getConfigurableByClazz(
          selectedConfigurable.parentClazz
        );
        isLast = false;
      }

      breadcrumbs.push({
        text: this.$vuetify.lang.t("$vuetify.navigation.objects"),
        disabled: false,
        href: "/objects/null",
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

    allInstances: function () {
      return this.$store.state.instances;
    },

    dependencies: function () {
      return this.$store.state.dependencies;
    },

    filteredInstances: function () {
      return this.allInstances.filter((x) => {
        return x.clazz === this.getConfigurableClazz();
      });
    },

    configurable: function () {
      var clazz = this.getConfigurableClazz();
      return this.getConfigurableByClazz(clazz);
    },
  },
  methods: {
    findInstanceNameById: function (id) {
      var y = this.allInstances.find((x) => x.id === id);
      if (y != null) {
        return y.fields["name"];
      } else {
        return this.$vuetify.lang.t("$vuetify.objects.deleted");
      }
    },

    displayField: function (fieldDefinition, fieldValue) {
      if (fieldValue == null) {
        return "-";
      }

      if (fieldDefinition.type == "Boolean") {
        if (fieldValue == "1") {
          return this.$vuetify.lang.t("$vuetify.common.yes");
        } else {
          return this.$vuetify.lang.t("$vuetify.common.no");
        }
      }

      if (fieldDefinition.type == "InstanceReference") {
        return fieldValue
          .split(",")
          .map((x) => this.findInstanceNameById(eval(x)))
          .join(", ");
      }

      if (fieldDefinition.type == "Temperature") {
        return (
          temp.kelvinsToDisplayTemperature(fieldValue) +
          " " +
          temp.obtainTemperatureUnit().title
        );
      }

      return fieldValue;
    },

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
        name: "objects",
        params: { clazz: configurable.clazz },
      });
      await this.refresh();
    },

    refreshConfigurables: async function () {
      return client.getConfigurables();
    },

    refreshInstances: async function () {
      return client.getAllInstances();
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

    addOrGenerateNewInstance: function () {
      console.log('generate')
      if (this.configurable.generated) {
        client.generateConfigurable(this.getConfigurableClazz());
      } else {
        this.showAddNewInstanceDialog();
      }
    },

    showAddNewInstanceDialog: function () {
      store.commit(RESET_INSTANCE, this.configurable);

      this.instanceDialog.show = true;
      this.instanceDialog.title = this.configurable.addNewRes;
      this.instanceDialog.actionText = this.$vuetify.lang.t(
        "$vuetify.objects.add"
      );
      this.instanceDialog.action = this.addInstance;
      this.instanceDialog.activeTab = 0;
    },

    closeInstanceDialog: function () {
      this.instanceDialog.show = false;
    },

    showDeleteInstanceDialog: async function (instanceId) {
      this.$store.commit(CLEAR_DEPENDENCIES);

      this.deleteDialog = {
        loading: true,
        action: this.deleteInstance,
        instanceId: instanceId,
        show: true,
      };

      var dialog = this.deleteDialog;
      await client
        .getDependencies(instanceId)
        .then(() => (dialog.loading = false));
    },

    closeDeleteDialog: function () {
      this.deleteDialog.show = false;
    },

    showEditInstanceDialog: function (instance) {
      this.instanceDialog.title = this.configurable.editRes;
      this.instanceDialog.actionText = this.$vuetify.lang.t(
        "$vuetify.objects.edit"
      );
      this.instanceDialog.action = this.editInstance;
      this.instanceDialog.activeTab = 0;
      this.instanceDialog.instance = instance;

      store.commit(RESET_INSTANCE, this.configurable);
      store.commit(EDIT_INSTANCE, instance);

      if (this.$refs.blockly != null) {
        this.$refs.blockly.reloadBlocks(instance.automation);
      }

      this.instanceDialog.show = true;
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