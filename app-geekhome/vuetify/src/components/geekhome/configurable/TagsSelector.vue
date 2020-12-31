<template>
  <v-simple-table>
    <template v-slot:default>
      <tbody>
        <tr v-for="tagCategory in tags" :key="tagCategory.id">
          <td>{{ tagCategory.name }}:</td>
          <td>
            <v-chip
              v-for="tag in tagCategory.children"
              :key="tag.id"
              class="ma-2"
              @click="selectUnselectTag(tag.id)"
              label           
              :color = "isTagIncluded(tag.id) ? 'primary' : ''" >
              {{ tag.name }}
            </v-chip>
          </td>
        </tr>
      </tbody>
    </template>
  </v-simple-table>
</template>

<script>
import { client } from "../../../rest.js";
import { UPDATE_INSTANCE_ADD_TAG, UPDATE_INSTANCE_REMOVE_TAG } from "../../../plugins/vuex";

export default {
  computed: {
    tags: function () {
      return this.$store.state.tags;
    },

    selectedTagIds() {
      return this.$store.state.newInstance.tagIds
    }
  },
  methods: {
    selectTag: function (tagId) {
      this.$store.commit(UPDATE_INSTANCE_ADD_TAG, tagId);
    },

    unselectTag: function (tagId) {
      this.$store.commit(UPDATE_INSTANCE_REMOVE_TAG, tagId);
    },

    selectUnselectTag: function(tagId) {
      if (this.isTagIncluded(tagId)) {
        this.unselectTag(tagId)
      } else {
        this.selectTag(tagId)
      }
    },

    isTagIncluded: function(tagId) {
      return this.$store.state.newInstance.tagIds.includes(tagId)
    }
  },
  mounted: function () {
    client.getTags();
  },
};
</script>