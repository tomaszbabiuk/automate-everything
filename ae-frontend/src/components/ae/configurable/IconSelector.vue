<template>
  <v-simple-table v-if="iconCategories.length > 0">
    <template v-slot:default>
      <tbody>
        <tr v-for="item in iconCategories" :key="item.id">
          <td>{{ item.name }}:</td>
          <td v-if="item.iconIds.length > 0">
            <v-chip
              v-for="iconId in item.iconIds"
              :key="iconId"
              class="ma-2"
              x-large
              @click="selectIcon(iconId)"
              label
              :outlined="iconId == selectedIconId ? false : true"
              :color="iconId == selectedIconId ? 'primary' : ''"
            >
              <img
                left
                :src="'/rest/icons/' + iconId + '/raw'"
                width="50"
                height="50"
              />
            </v-chip>
          </td>
          <td v-else>{{ $vuetify.lang.t("$vuetify.icons.no_icons_in_category") }}</td>
        </tr>
      </tbody>
    </template>
  </v-simple-table>
  <v-simple-table v-else>
    <template v-slot:default>
      <tbody>
        <tr>
          <td class="text-center">{{ $vuetify.lang.t("$vuetify.icons.no_icons") }}</td>
        </tr>
      </tbody>
    </template>
  </v-simple-table>
</template>

<script>
import { client } from "../../../rest.js";
import { UPDATE_INSTANCE_ICON } from "../../../plugins/vuex";

export default {
  computed: {
    iconCategories: function () {
      return this.$store.state.iconCategories;
    },
    selectedIconId() {
      return this.$store.state.newInstance.iconId;
    },
  },
  methods: {
    selectIcon: function (iconId) {
      this.$store.commit(UPDATE_INSTANCE_ICON, iconId);
    },
  },
  mounted: function () {
    client.getIconCategories();
  },
};
</script>