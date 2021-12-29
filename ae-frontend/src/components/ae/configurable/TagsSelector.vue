<template>
  <v-simple-table v-if="tags.length > 0">
    <template v-slot:default>
      <tbody>
        <tr v-for="tagCategory in tags" :key="tagCategory.id">
          <td>{{ tagCategory.name }}:</td>
          <td v-if="tagCategory.children.length > 0">
            <v-chip
              v-for="tag in tagCategory.children"
              :key="tag.id"
              class="mr-2"
              @click="selectUnselectTag(tag.id)"
              :color="isTagIncluded(tag.id) ? 'primary' : ''"
            >
              {{ tag.name }}
            </v-chip>
          </td>
          <td v-else>
            {{ $vuetify.lang.t("$vuetify.tags.no_tags_in_category") }}
          </td>
        </tr>
      </tbody>
    </template>
  </v-simple-table>
  <v-simple-table v-else>
    <template v-slot:default>
      <tbody>
        <tr>
          <td class="text-center">
            {{ $vuetify.lang.t("$vuetify.tags.no_tags") }}
          </td>
        </tr>
      </tbody>
    </template>
  </v-simple-table>
</template>

<script>
import { client } from "../../../rest.js";
import {
  UPDATE_INSTANCE_ADD_TAG,
  UPDATE_INSTANCE_REMOVE_TAG,
} from "../../../plugins/vuex";

export default {
  computed: {
    tags: function () {
      return this.$store.state.tags;
    },

    selectedTagIds() {
      return this.$store.state.newInstance.tagIds;
    },
  },
  methods: {
    selectTag: function (tagId) {
      this.$store.commit(UPDATE_INSTANCE_ADD_TAG, tagId);
    },

    unselectTag: function (tagId) {
      this.$store.commit(UPDATE_INSTANCE_REMOVE_TAG, tagId);
    },

    selectUnselectTag: function (tagId) {
      if (this.isTagIncluded(tagId)) {
        this.unselectTag(tagId);
      } else {
        this.selectTag(tagId);
      }
    },

    isTagIncluded: function (tagId) {
      return this.$store.state.newInstance.tagIds.includes(tagId);
    },
  },
  mounted: function () {
    client.getTags();
  },
};
</script>