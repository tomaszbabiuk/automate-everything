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
            <v-btn dark text @click="addNewTagCategory()">{{
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
import { client } from "../rest.js"


export default {
  data: () => ({
    categoryDialog: false,
    tagDialog: false,
    tagName: "",
    active: [],
  }),
  computed: {
    items : function() {
      return this.$store.state.tags
    }
  },
  methods: {
    openNewCategoryDialog: function () {
      this.tagName = ''
      this.categoryDialog = true;
    },
    closeNewCategoryDialog: function () {
      this.categoryDialog = false;
    },
    addNewTagCategory: function () {
      var newTag = {
        parentId: null,
        name: this.tagName,
        children: []
      };

      client.postNewTag(newTag)
      
      this.closeNewCategoryDialog()
    },
    openNewTagDialog: function () {
      this.tagName = ''
      this.tagDialog = true;
    },
    closeNewTagDialog: function () {
      this.tagDialog = false;
    },
    addNewTag: function () {
      var newTag = {
        parentId: this.active[0].id,
        name: this.tagName,
        children: []        
      };

      client.postNewTag(newTag)
      
      this.closeNewTagDialog()
    },
  },
  mounted: function () {
    client.getTags()
  },
};
</script>