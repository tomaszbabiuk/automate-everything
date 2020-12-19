<template>
  <v-data-table
    :headers="headers"
    :items="items"
    item-key="id"
    sort-by="calories"
    class="elevation-1"
  >
    <template v-slot:top>
      <v-toolbar flat>
        <v-toolbar-title>{{ $vuetify.lang.t("$vuetify.icons.icons") }}</v-toolbar-title>
        <v-spacer></v-spacer>
        <v-btn
          right
          color="primary"
          dark
          class="mb-2"
          @click.stop="openAddCategoryDialog()"
        >
          {{ $vuetify.lang.t("$vuetify.icons.add_category") }}
        </v-btn>

        <v-dialog
          v-model="categoryDialog.show"
          persistent
          transition="dialog-bottom-transition"
          width="500"
        >
          <v-card>
            <v-toolbar dark color="primary">
              <v-btn icon dark @click="closeCategoryDialog()">
                <v-icon>mdi-close</v-icon>
              </v-btn>
              <v-toolbar-title>{{ categoryDialog.titleText }}</v-toolbar-title>
              <v-spacer></v-spacer>
              <v-toolbar-items>
                <v-btn dark text @click="categoryDialog.action()">
                  {{ categoryDialog.actionText }}
                </v-btn>
              </v-toolbar-items>
            </v-toolbar>

            <v-container>
              <v-form
                v-model="categoryDialog.valid"
                ref="categoryForm"
                lazy-validation
              >
                <v-text-field
                  :rules="[
                    (v) =>
                      !!v ||
                      $vuetify.lang.t('$vuetify.validation.field_required'),
                    (v) =>
                      (v && v.length <= 50) ||
                      $vuetify.lang.t('$vuetify.validation.field_lessThan50'),
                  ]"
                  ref="name"
                  v-model="categoryDialog.name"
                  :label="$vuetify.lang.t('$vuetify.common.name')"
                  required="true"
                  counter="50"
                  validate-on-blur
                ></v-text-field>
              </v-form>
            </v-container>
          </v-card>
        </v-dialog>

        <v-dialog
          v-model="iconDialog.show"
          persistent
          transition="dialog-bottom-transition"
          width="500"
        >
          <v-card>
            <v-toolbar dark color="primary">
              <v-btn icon dark @click="closeIconDialog()">
                <v-icon>mdi-close</v-icon>
              </v-btn>
              <v-toolbar-title>{{ iconDialog.titleText }}</v-toolbar-title>
              <v-spacer></v-spacer>
              <v-toolbar-items>
                <v-btn dark text @click="iconDialog.action()">
                  {{ iconDialog.actionText }}
                </v-btn>
              </v-toolbar-items>
            </v-toolbar>

            <v-container>
              <v-form v-model="iconDialog.valid" ref="iconForm" lazy-validation>
                <v-textarea
                  :rules="[
                    (v) =>
                      !!v ||
                      $vuetify.lang.t('$vuetify.validation.field_required'),
                    (v) =>
                      (v && v.length <= 10000) ||
                      $vuetify.lang.t(
                        '$vuetify.validation.field_lessThan10000'
                      ),
                  ]"
                  ref="raw"
                  v-model="iconDialog.raw"
                  :label="$vuetify.lang.t('$vuetify.icons.raw')"
                  required="true"
                  counter="10000"
                ></v-textarea>
              </v-form>
            </v-container>
          </v-card>
        </v-dialog>

        <v-dialog v-model="dialogDelete" max-width="500px">
          <v-card>
            <v-card-title class="headline"
              >Are you sure you want to delete this item?</v-card-title
            >
            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn color="blue darken-1" text @click="closeDelete"
                >Cancel</v-btn
              >
              <v-btn color="blue darken-1" text @click="deleteItemConfirm"
                >OK</v-btn
              >
              <v-spacer></v-spacer>
            </v-card-actions>
          </v-card>
        </v-dialog>
      </v-toolbar>
    </template>
    <template v-slot:[`item.iconIds`]="{ item }">
      <v-chip
        v-for="iconId in item.iconIds"
        :key="iconId"
        class="ma-2"
        x-large
        @click:close="chip2 = false"
        @click="console.log('dupa')"
        close
        label
        outlined
      >
        <img
          left
          :src="'/rest/icons/' + iconId + '/raw'"
          width="50"
          height="50"
        />
      </v-chip>
    </template>
    <template v-slot:[`item.actions`]="{ item }" }>
      <nobr>
        <v-icon class="mr-2" @click="openAddIconDialog(item)">mdi-plus</v-icon>
        <v-icon class="mr-2" @click="openEditCategoryDialog(item)">mdi-pencil</v-icon>
        <v-icon @click="deleteIconCategory(item)"> mdi-delete </v-icon>
      </nobr>
    </template>
    <template v-slot:no-data>
      <v-btn color="primary" @click="initialize"> Reset </v-btn>
    </template>
  </v-data-table>
</template>

<script>
import { client } from "../rest.js";

export default {
  data: () => ({
    categoryDialog: {
      id: -1,
      valid: true,
      show: false,
      titleText: "",
      actionText: "",
      name: "",
      action: function () {},
    },
    iconDialog: {
      iconCategoryId: -1,
      valid: true,
      show: false,
      titleText: "",
      actionText: "",
      raw: "",
      action: function () {},
    },
    dialogDelete: false,
    headers: [],
  }),

  computed: {
    items: function () {
      return this.$store.state.iconCategories;
    },

    formTitle() {
      return this.editedIndex === -1 ? "New Item" : "Edit Item";
    },
  },

  watch: {
    dialog(val) {
      val || this.close();
    },
    dialogDelete(val) {
      val || this.closeDelete();
    },
  },

  methods: {
    focusOnNameLazy: function () {
      setTimeout(() => {
        this.$refs.name.focus();
      }, 200);
    },
    focusOnRawLazy: function () {
      setTimeout(() => {
        this.$refs.raw.focus();
      }, 200);
    },
    closeCategoryDialog: function () {
      this.categoryDialog.show = false;
    },
    closeIconDialog: function () {
      this.iconDialog.show = false;
    },
    openAddCategoryDialog: function () {
      this.categoryDialog = {
        name: "",
        titleText: this.$vuetify.lang.t("$vuetify.icons.add_category"),
        actionText: this.$vuetify.lang.t("$vuetify.common.add"),
        action: this.addNewCategory,
        show: true,
      };
      this.focusOnNameLazy();
    },
    addNewCategory: function () {
      if (this.$refs.categoryForm.validate()) {
        let categoryDto = {
          id: null,
          name: this.categoryDialog.name,
        };
        client.postIconCategory(categoryDto);
        this.closeCategoryDialog();
      }
    },
    openEditCategoryDialog: function (categoryDto) {
      this.categoryDialog = {
        name: categoryDto.name,
        titleText: this.$vuetify.lang.t("$vuetify.icons.edit_category"),
        actionText: this.$vuetify.lang.t("$vuetify.common.edit"),
        action: this.editCategory,
        id: categoryDto.id,
        show: true,
      };
      this.focusOnNameLazy();
    },
    editCategory: function () {
      if (this.$refs.categoryForm.validate()) {
        let categoryDto = {
          id: this.categoryDialog.id,
          name: this.categoryDialog.name,
        };
        client.putIconCategory(categoryDto);
        this.closeCategoryDialog();
      }
    },
    openAddIconDialog: function (categoryDto) {
      this.iconDialog = {
        id: categoryDto.id,
        raw: "",
        titleText: this.$vuetify.lang.t("$vuetify.icons.add_icon"),
        actionText: this.$vuetify.lang.t("$vuetify.common.add"),
        action: this.addNewIcon,
        show: true,
      };
      this.focusOnRawLazy();
    },
    addNewIcon: function () {
      if (this.$refs.iconForm.validate()) {
        let iconDto = {
          iconCategoryId: this.iconDialog.iconCategoryId,
          raw: this.iconDialog.raw,
        };
        client.postIcon(iconDto);
        this.closeIconDialog();
      }
    },
    editItem(item) {
      this.editedIndex = this.desserts.indexOf(item);
      this.editedItem = Object.assign({}, item);
      this.dialog = true;
    },

    deleteItem(item) {
      this.editedIndex = this.desserts.indexOf(item);
      this.editedItem = Object.assign({}, item);
      this.dialogDelete = true;
    },

    deleteItemConfirm() {
      this.desserts.splice(this.editedIndex, 1);
      this.closeDelete();
    },

    close() {
      this.dialog = false;
      this.$nextTick(() => {
        this.editedItem = Object.assign({}, this.defaultItem);
        this.editedIndex = -1;
      });
    },

    closeDelete() {
      this.dialogDelete = false;
      this.$nextTick(() => {
        this.editedItem = Object.assign({}, this.defaultItem);
        this.editedIndex = -1;
      });
    },

    save() {
      if (this.editedIndex > -1) {
        Object.assign(this.desserts[this.editedIndex], this.editedItem);
      } else {
        this.desserts.push(this.editedItem);
      }
      this.close();
    },
  },
  mounted: function () {
    this.headers = [
      {
        text: this.$vuetify.lang.t("$vuetify.common.name"),
        align: "start",
        sortable: false,
        value: "name",
      },
      {
        text: this.$vuetify.lang.t("$vuetify.icons.icons"),
        value: "iconIds",
        sortable: false,
      },
      {
        text: this.$vuetify.lang.t("$vuetify.common.actions"),
        value: "actions",
        sortable: false,
        align: "end",
      },
    ];

    client.getIconCategories();
  },
};
</script>