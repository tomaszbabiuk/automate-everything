<template>
  <v-data-table
    :headers="headers"
    :items="items"
    item-key="id"
    sort-by="calories"
    class="elevation-1"
    show-select
    single-select
  >
    <template v-slot:top>
      <v-toolbar flat>
        <v-toolbar-title>Icons</v-toolbar-title>
        <v-spacer></v-spacer>


        
        <v-dialog v-model="dialog" max-width="500px">
          <template v-slot:activator="{ on, attrs }">
            <v-btn
              right
              color="primary"
              dark
              class="mb-2"
              v-bind="attrs"
              v-on="on"
            >
              New category
            </v-btn>
          </template>
          <v-card>
            <v-card-title>
              <span class="headline">{{ formTitle }}</span>
            </v-card-title>

            <v-card-text>
              <v-container>
                <v-row>
                  <v-col cols="12" sm="6" md="4">
                    <v-text-field
                      v-model="editedItem.name"
                      label="Dessert name"
                    ></v-text-field>
                  </v-col>
                  <v-col cols="12" sm="6" md="4">
                    <v-text-field
                      v-model="editedItem.calories"
                      label="Calories"
                    ></v-text-field>
                  </v-col>
                  <v-col cols="12" sm="6" md="4">
                    <v-text-field
                      v-model="editedItem.fat"
                      label="Fat (g)"
                    ></v-text-field>
                  </v-col>
                  <v-col cols="12" sm="6" md="4">
                    <v-text-field
                      v-model="editedItem.carbs"
                      label="Carbs (g)"
                    ></v-text-field>
                  </v-col>
                  <v-col cols="12" sm="6" md="4">
                    <v-text-field
                      v-model="editedItem.protein"
                      label="Protein (g)"
                    ></v-text-field>
                  </v-col>
                </v-row>
              </v-container>
            </v-card-text>

            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn color="blue darken-1" text @click="close"> Cancel </v-btn>
              <v-btn color="blue darken-1" text @click="save"> Save </v-btn>
            </v-card-actions>
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
    <template v-slot:[`item.icons`]="">
      <v-chip
        class="ma-2"
        x-large
        @click:close="chip2 = false"
        @click="console.log('dupa')"
        close
        label
        outlined
      >
        <img left src="/rest/svg" width="50" height="50" />
      </v-chip>

      <v-chip
        class="ma-2"
        x-large
        @click:close="chip2 = false"
        close
        label
        outlined
      >
        <img left src="/rest/svg" width="50" height="50" />
      </v-chip>

      <v-chip
        class="ma-2"
        x-large
        @click:close="chip2 = false"
        close
        label
        outlined
      >
        <img left src="/rest/svg" width="50" height="50" />
      </v-chip>

      <v-chip
        class="ma-2"
        x-large
        @click:close="chip2 = false"
        close
        label
        outlined
      >
        <img left src="/rest/svg" width="50" height="50" />
      </v-chip>

      <v-chip
        class="ma-2"
        x-large
        @click:close="chip2 = false"
        close
        label
        outlined
      >
        <img left src="/rest/svg" width="50" height="50" />
      </v-chip>

      <v-chip
        class="ma-2"
        x-large
        @click:close="chip2 = false"
        close
        label
        outlined
      >
        <img left src="/rest/svg" width="50" height="50" />
      </v-chip>
    </template>
    <template v-slot:[`item.actions`]="{ item }" }>
      <nobr>
        <v-icon small class="mr-2" @click="addIcon(item)"> mdi-plus </v-icon>
        <v-icon small class="mr-2" @click="editIconCategory(item)">
          mdi-pencil</v-icon
        >
        <v-icon small @click="deleteIconCategory(item)"> mdi-delete </v-icon>
      </nobr>
    </template>
    <template v-slot:no-data>
      <v-btn color="primary" @click="initialize"> Reset </v-btn>
    </template>
  </v-data-table>
</template>

<script>
import { client } from "../rest.js"

export default {
  data: () => ({
    dialog: false,
    dialogDelete: false,
    headers: [
      {
        text: "Name",
        align: "start",
        sortable: false,
        value: "name",
      },
      {
        text: "Icons",
        value: "icons",
        sortable: false,
      },
      {
        text: "Actions",
        value: "actions",
        sortable: false,
        align: "end",
      },
    ],
    editedIndex: -1,
    editedItem: {
      name: "",
      calories: 0,
      fat: 0,
      carbs: 0,
      protein: 0,
    },
    defaultItem: {
      name: "",
      calories: 0,
      fat: 0,
      carbs: 0,
      protein: 0,
    },
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
    client.getIconCategories()
  },
};
</script>