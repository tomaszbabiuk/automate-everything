<template>
  <v-data-table
    :headers="headers"
    :items="items"
    item-key="id"
    class="elevation-1"
  >
    <template v-slot:top>
      <v-toolbar flat>
        <v-toolbar-title>{{ $vuetify.lang.t("$vuetify.tags.tags") }}</v-toolbar-title>
        <v-spacer></v-spacer>
        <v-btn
          right
          color="primary"
          dark
          class="mb-2"
          @click.stop="openAddCategoryDialog()"
        >
          {{ $vuetify.lang.t("$vuetify.tags.add_category") }}
        </v-btn>

        <v-dialog
          v-model="tagDialog.show"
          persistent
          transition="dialog-bottom-transition"
          width="500"
        >
          <v-card>
            <v-toolbar dark color="primary">
              <v-btn icon dark @click="closeTagDialog()">
                <v-icon>mdi-close</v-icon>
              </v-btn>
              <v-toolbar-title>{{ tagDialog.titleText }}</v-toolbar-title>
              <v-spacer></v-spacer>
              <v-toolbar-items>
                <v-btn dark text @click="tagDialog.action()">
                  {{ tagDialog.actionText }}
                </v-btn>
              </v-toolbar-items>
            </v-toolbar>

            <v-container>
              <v-form
                v-model="tagDialog.valid"
                ref="tagForm"
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
                  v-model="tagDialog.name"
                  :label="$vuetify.lang.t('$vuetify.common.name')"
                  required="true"
                  counter="50"
                  validate-on-blur
                ></v-text-field>
              </v-form>
            </v-container>
          </v-card>
        </v-dialog>

        <v-dialog v-model="deleteDialog.show" max-width="500px">
          <v-card>
            <v-card-title class="headline"
              >{{ $vuetify.lang.t("$vuetify.common.delete_question") }}</v-card-title
            >
            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn color="blue darken-1" text @click="closeDeleteDialog()"
                >{{ $vuetify.lang.t("$vuetify.common.cancel") }}</v-btn
              >
              <v-btn color="blue darken-1" text @click="deleteDialog.action()"
                >{{ $vuetify.lang.t("$vuetify.common.ok") }}</v-btn
              >
              <v-spacer></v-spacer>
            </v-card-actions>
          </v-card>
        </v-dialog>
      </v-toolbar>
    </template>
    <template v-slot:[`item.children`]="{ item }">
      <v-chip
        v-for="tag in item.children"
        :key="tag.id"
        class="ma-2"
        @click:close="openDeleteTagDialog(tag.id)"
        @click="openEditTagDialog(item.id, tag)"
        close
        outlined
      >
        {{tag.name}}
      </v-chip>
    </template>
    <template v-slot:[`item.actions`]="{ item }" }>
      <nobr>
        <v-icon class="mr-2" @click="openAddTagDialog(item)">mdi-plus</v-icon>
        <v-icon class="mr-2" @click="openEditCategoryDialog(item)">mdi-pencil</v-icon>
        <v-icon @click="openDeleteCategoryDialog(item)"> mdi-delete </v-icon>
      </nobr>
    </template>
    <template v-slot:no-data>
      {{ $vuetify.lang.t("$vuetify.noDataText") }}
    </template>
  </v-data-table>
</template>

<script>
import { client } from "../rest.js";

export default {
  data: () => ({
    tagDialog: {
      iconCategoryId: -1,
      valid: true,
      show: false,
      titleText: "",
      actionText: "",
      name: "",
      action: function () {},
    },
    iconDialog: {
      iconId: -1,
      iconCategoryId: -1,
      valid: true,
      show: false,
      titleText: "",
      actionText: "",
      raw: "",
      action: function () {},
    },
    deleteDialog:  {
      show: false,
      action: function() {},
      iconId: -1,
      iconCategoryId: -1,
    },
    headers: [],
    componentKey: 0,
  }),

  computed: {
    items: function () {
      return this.$store.state.tags;
    },

    formTitle() {
      return this.editedIndex === -1 ? "New Item" : "Edit Item";
    },
  },

  methods: {
    focusOnNameLazy: function () {
      setTimeout(() => {
        this.$refs.name.focus()
      }, 200);
    },
    closeTagDialog: function () {
      this.tagDialog.show = false
    },
    closeIconDialog: function () {
      this.iconDialog.show = false
    },
    closeDeleteDialog: function() {
      this.deleteDialog.show = false
    },
    openAddCategoryDialog: function () {
      this.tagDialog = {
        name: "",
        titleText: this.$vuetify.lang.t("$vuetify.tags.add_category"),
        actionText: this.$vuetify.lang.t("$vuetify.common.add"),
        action: this.addNewCategory,
        show: true,
      };
      this.focusOnNameLazy();
    },
    addNewCategory: function () {
      if (this.$refs.tagForm.validate()) {
        let tagDto = {
          parentId: null,
          id: null,
          name: this.tagDialog.name,
        };
        client.postTag(tagDto);
        this.closeTagDialog();
      }
    },
    openEditCategoryDialog: function (tagDto) {
      this.tagDialog = {
        tagId: tagDto.id,
        name: tagDto.name,
        titleText: this.$vuetify.lang.t("$vuetify.tags.edit_category"),
        actionText: this.$vuetify.lang.t("$vuetify.common.edit"),
        action: this.editCategory,
        show: true,
      };
      this.focusOnNameLazy();
    },
    editCategory: function () {
      if (this.$refs.tagForm.validate()) {
        let tagDto = {
          id: this.tagDialog.tagId,
          name: this.tagDialog.name,
          parentId: null
        };
        client.putTag(tagDto);
        this.closeTagDialog();
      }
    },
    openDeleteCategoryDialog: function(tagDto) {
      this.deleteDialog = {
        action: this.deleteCategory,
        tagId: tagDto.id,
        show: true,
      };
    },
    deleteCategory: function () {
      let id = this.deleteDialog.tagId
      client.deleteTag(id)
      this.closeDeleteDialog();
    },
    openAddTagDialog: function (tagDto) {
      this.tagDialog = {
        tagId: tagDto.id,
        name: "",
        titleText: this.$vuetify.lang.t("$vuetify.tags.add_tag"),
        actionText: this.$vuetify.lang.t("$vuetify.common.add"),
        action: this.addNewTag,
        show: true,
      };
      this.focusOnNameLazy()
    },
    addNewTag: function () {
      if (this.$refs.tagForm.validate()) {
        let tagDto = {
          id: null,
          parentId: this.tagDialog.tagId,
          name: this.tagDialog.name,
        };
        client.postTag(tagDto);
        this.closeTagDialog();
      }
    },
    openEditTagDialog: async function (parentId, tagDto) {
      this.tagDialog = {
        tagId: tagDto.id,
        tagParentId: parentId,
        name: tagDto.name,
        titleText: this.$vuetify.lang.t("$vuetify.tags.edit_tag"),
        actionText: this.$vuetify.lang.t("$vuetify.common.edit"),
        action: this.editTag,
        show: true,
      };

      this.focusOnNameLazy()
    },
    editTag: function () {
      if (this.$refs.tagForm.validate()) {
        let tagDto = {
          parentId: this.tagDialog.tagParentId,
          id: this.tagDialog.tagId,
          name: this.tagDialog.name,
        };
        client.putTag(tagDto);
        this.closeTagDialog()
      }
    },
    openDeleteTagDialog: function(tagId) {
      this.deleteDialog = {
        action: this.deleteTag,
        tagId: tagId,
        show: true,
      };
    },
    deleteTag: function() {
      let id = this.deleteDialog.tagId
      client.deleteTag(id)
      this.closeDeleteDialog()
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
        text: this.$vuetify.lang.t("$vuetify.tags.tags"),
        value: "children",
        sortable: false,
      },
      {
        text: this.$vuetify.lang.t("$vuetify.common.actions"),
        value: "actions",
        sortable: false,
        align: "end",
      },
    ];

    client.getTags()
  },
};
</script>