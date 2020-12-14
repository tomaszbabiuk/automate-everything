<template>
  <div>
    <v-row>
      <v-col col="5">
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
      </v-col>

      <v-divider vertical></v-divider>

      <v-col v-if="active.length > 0">
        <div v-if="active[0].parentId === null">Selected category:</div>
        <div v-else>Selected tag:</div>
        <div class="text-h3">{{ active[0].name }}</div>
        <v-divider></v-divider>
        <v-btn
          v-if="active[0].parentId === null"
          class="ma-2"
          depressed
          @click.stop="openNewTagDialog()"
          color="primary"
        >
          Add tag
        </v-btn>
        <v-btn depressed class="ma-2" @click.stop="openNewTagDialog()">
          Edit
        </v-btn>
        <v-btn class="ma-2" depressed @click.stop="openNewTagDialog()">
          Remove
        </v-btn>
        or
        <v-btn
          class="ma-2"
          depressed
          @click.stop="openNewCategoryDialog()"
          color="primary"
        >
          Add category
        </v-btn>
      </v-col>
      <v-col v-else>
        <v-btn
          class="ma-2"
          depressed
          @click.stop="openNewCategoryDialog()"
          color="primary"
        >
          Add category
        </v-btn>
      </v-col>
    </v-row>

    <v-dialog
      v-model="tagDialog"
      persistent
      transition="dialog-bottom-transition"
      width="500"
    >
      <v-card>
        <v-toolbar dark color="primary">
          <v-btn icon dark @click="closeNewTagDialog()">
            <v-icon>mdi-close</v-icon>
          </v-btn>
          <v-toolbar-title>New tag</v-toolbar-title>
          <v-spacer></v-spacer>
          <v-toolbar-items>
            <v-btn dark text @click="addNewTag()">{{
              $vuetify.lang.t("$vuetify.configurables.add")
            }}</v-btn>
          </v-toolbar-items>
        </v-toolbar>

        <v-container>
          <v-form ref="form">
            <v-text-field
              v-model="tagName"
              label="Name"
              :required="true"
              :counter="50"
            ></v-text-field>
          </v-form>
        </v-container>
      </v-card>
    </v-dialog>

    <v-dialog
      v-model="categoryDialog"
      persistent
      transition="dialog-bottom-transition"
      width="500"
    >
      <v-card>
        <v-toolbar dark color="primary">
          <v-btn icon dark @click="closeNewCategoryDialog()">
            <v-icon>mdi-close</v-icon>
          </v-btn>
          <v-toolbar-title>New tag category</v-toolbar-title>
          <v-spacer></v-spacer>
          <v-toolbar-items>
            <v-btn dark text @click="addNewCategory()">{{
              $vuetify.lang.t("$vuetify.configurables.add")
            }}</v-btn>
          </v-toolbar-items>
        </v-toolbar>

        <v-container>
          <v-form ref="form">
            <v-text-field
              v-model="tagName"
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
export default {
  data: () => ({
    categoryDialog: false,
    tagDialog: false,
    tagName: "",
    active: [],
    items: [
      {
        id: 1,
        name: "Applications",
        parentId: null,
        children: [
          { id: 2, name: "Calendar : app", parentId: 1 },
          { id: 3, name: "Chrome : app", parentId: 1 },
          { id: 4, name: "Webstorm : app", parentId: 1 },
        ],
      },
      {
        id: 15,
        parentId: null,
        name: "Downloads",
        children: [
          { id: 16, name: "October : pdf", parentId: 15 },
          { id: 17, name: "November : pdf", parentId: 15 },
          { id: 18, name: "Tutorial : html", parentId: 15 },
        ],
      },
    ],
  }),
  methods: {
    openNewCategoryDialog: function () {
      this.categoryDialog = true;
    },
    closeNewCategoryDialog: function () {
      this.categoryDialog = false;
    },
    add: function () {
      this.items.push({
        id: 16,
        name: this.tagName,
        children: [],
      });
      
      this.closeNewCategoryDialog()
    },
    openNewTagDialog: function () {
      this.tagDialog = true;
    },
    closeNewTagDialog: function () {
      this.tagDialog = false;
    },
    addNewTag: function () {
      this.items[0].children.push({
        id: 17,
        name: this.tagName,
        children: [],
      });
      
      this.closeNewTagDialog()
    },
  },
  mounted: function () {
    console.log(this.getSelectedItem(15));
  },
};
</script>