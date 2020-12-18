<template>
  <div>
    <v-banner single-line>
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
          {{$vuetify.lang.t('$vuetify.tags.add_tag')}}
        </v-btn>

        <v-btn text color="primary" @click.stop="openAddCategoryDialog()">
          {{$vuetify.lang.t('$vuetify.tags.add_category')}}
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
          <v-form v-model="dialog.valid" ref="form" lazy-validation>
            <v-text-field
              :rules="[v => !!v || $vuetify.lang.t('$vuetify.validation.field_required'), v => (v && v.length <= 50) || $vuetify.lang.t('$vuetify.validation.field_lessThan50')]"
              ref="name"
              v-model="dialog.name"
              :label="$vuetify.lang.t('$vuetify.common.name')"
              required="true"
              counter="50"
              validate-on-blur
            ></v-text-field>
          </v-form>
        </v-container>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
import { client } from "../rest.js"
import Vue from 'vue'

export default {
  data: () => ({
    categoryDialog: false,
    dialog: {
      valid: true,
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
    focusOnNameLazy: function() {
      setTimeout(() => {
        this.$refs.name.focus()
      }, 200)
    },
    closeDialog: function () {
      this.dialog.show = false;
    },
    openAddCategoryDialog: function () {
      this.dialog = {
        name: '',
        titleText: this.$vuetify.lang.t('$vuetify.tags.add_category'),
        actionText: this.$vuetify.lang.t('$vuetify.common.add'),
        action: this.addNewCategory,
        show: true
      }
      this.focusOnNameLazy()
    },
    openAddTagDialog: function () {
      this.dialog = {
        name: '',
        titleText: this.$vuetify.lang.t('$vuetify.tags.add_tag'),
        actionText: this.$vuetify.lang.t('$vuetify.common.add'),
        action: this.addNewTag,
        show: true
      }
      this.focusOnNameLazy()
    },
    openEditActiveTagDialog: function () {
      this.dialog = {
        name: this.active[0].name,
        titleText: this.$vuetify.lang.t('$vuetify.tags.edit_tag'),
        actionText: this.$vuetify.lang.t('$vuetify.common.edit'),
        action: this.editSelectedTag,
        show: true
      }
      this.focusOnNameLazy()
    },
    addNewCategory: function () {
      if (this.$refs.form.validate()) {
        let tagDto = {
          id: null,
          parentId: null,
          name: this.dialog.name,
        }
        client.postTag(tagDto);
        this.closeDialog();
      }
    },
    addNewTag: function () {
      if (this.$refs.form.validate()) {
        var tagDto = {
          id: null,
          parentId: this.active[0].id,
          name: this.dialog.name,
        };

        client.postTag(tagDto);
        this.closeDialog();
      }
    },
    editSelectedTag: function() {
      if (this.$refs.form.validate()) {
        var tagDto = {
          name: this.dialog.name,
          id: this.active[0].id,
          parentId: this.active[0].parentId
        };

        client.putTag(tagDto);
        this.closeDialog();
      }
    },
    removeActiveTag: function() {
      client.deleteTag(this.active[0].id)
      Vue.delete(this.active, 0)
    }
  },
  mounted: function () {
    client.getTags()
  },
};
</script>