<template>
  <div>
    <v-banner single-line sticky="true">
      <div class="text-caption" v-if="items.length > 0 && active.length == 0">Select item for more actions</div>
      <template v-slot:actions>
        <v-btn icon v-if="active.length > 0" @click.stop="openEditActiveTagDialog()">
          <v-icon dark> mdi-pencil </v-icon>
        </v-btn>

        <v-btn icon v-if="active.length > 0" @click.stop="removeActiveTag()">
          <v-icon dark> mdi-delete </v-icon>
        </v-btn>

        <v-btn
          v-if="active.length > 0 && active[0].parentId === null"
          text
          color="primary"
          @click.stop="openAddTagDialog()"
        >
          Add tag
        </v-btn>

        <v-btn text color="primary" @click.stop="openAddCategoryDialog()">
          Add category
        </v-btn>
      </template>
    </v-banner>

    <v-treeview
      activatable
      :active.sync="active"
      item-key="id"
      item-text="name"
      color="primary"
      return-object
      :items="items"
    >
    </v-treeview>

    <v-dialog
      v-model="dialog.show"
      persistent
      transition="dialog-bottom-transition"
      width="500"
    >
      <v-card>
        <v-toolbar dark color="primary">
          <v-btn icon dark @click="closeDialog()">
            <v-icon>mdi-close</v-icon>
          </v-btn>
          <v-toolbar-title>{{dialog.titleText}}</v-toolbar-title>
          <v-spacer></v-spacer>
          <v-toolbar-items>
            <v-btn dark text @click="dialog.action()">
              {{dialog.actionText}}
            </v-btn>
          </v-toolbar-items>
        </v-toolbar>

        <v-container>
          <v-form ref="form">
            <v-text-field
              v-model="dialog.name"
              label="Name"
              :required="true"
              :counter="50"
            ></v-text-field>
          </v-form>
        </v-container>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
import { client } from "../rest.js";

export default {
  data: () => ({
    categoryDialog: false,
    dialog: {
      show: false,
      titleText: 'title text',
      actionText: 'action text',
      name: '',
      action: function() { }
    },
    active: [],
  }),
  computed: {
    items: function () {
      return this.$store.state.tags;
    },
  },
  methods: {
    closeDialog: function () {
      this.dialog.show = false;
    },
    openAddCategoryDialog: function () {
      this.dialog = {
        name: '',
        titleText: "Add new category",
        actionText: "Add",
        action: this.addNewCategory,
        show: true
      }
    },
    openAddTagDialog: function () {
      this.dialog = {
        name: '',
        titleText: "Add new tag",
        actionText: "Add",
        action: this.addNewTag,
        show: true
      };
    },
    openEditActiveTagDialog: function () {
      this.dialog = {
        name: this.active[0].name,
        titleText: "Edit tag",
        actionText: "Edit",
        action: this.editSelectedTag,
        show: true
      };
    },
    addNewCategory: function () {
      let tagDto = {
        id: null,
        parentId: null,
        name: this.dialog.name,
      }
      client.postTag(tagDto);
      this.closeDialog();
    },
    addNewTag: function () {
      var tagDto = {
        id: null,
        parentId: this.active[0].id,
        name: this.dialog.name,
      };

      client.postTag(tagDto);
      this.closeDialog();
    },
    editSelectedTag: function() {
      var tagDto = {
        name: this.dialog.name,
        id: this.active[0].id,
        parentId: this.active[0].parentId
      };

      client.putTag(tagDto);
      this.closeDialog();
    },
    removeActiveTag: function() {
      client.deleteTag(this.active[0].id)
    }
  },
  mounted: function () {
    client.getTags()
  },
};
</script>