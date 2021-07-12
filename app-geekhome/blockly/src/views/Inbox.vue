<template>
<div>
  <v-card class="mx-auto">
    <v-skeleton-loader v-if="loading" type="table"></v-skeleton-loader>
    <v-list-item
      two-line
      v-for="message in inboxMessages"
      :key="message.id"
      v-else
    >
      <v-list-item-content>
        <v-list-item-title class="text-wrap">{{ message.subject }}</v-list-item-title>
        <v-list-item-subtitle class="text-wrap">{{ message.body }}</v-list-item-subtitle>
      </v-list-item-content>
      <v-list-item-avatar>
          <v-icon
            class="grey lighten-1"
            dark
            @click="openDeleteMessageDialog(message)"
          >
            mdi-delete
          </v-icon>
        </v-list-item-avatar>
    </v-list-item>
  </v-card>
      <v-dialog v-model="deleteMessageDialog.show" max-width="500px">
      <v-card>
        <v-card-title class="headline">{{
          $vuetify.lang.t("$vuetify.inbox.delete_message_question")
        }}</v-card-title>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="blue darken-1" text @click="closeDeleteMessageDialog()">{{
            $vuetify.lang.t("$vuetify.common.cancel")
          }}</v-btn>
          <v-btn color="blue darken-1" text @click="deleteMessage(deleteMessageDialog.messageId)">{{
            $vuetify.lang.t("$vuetify.common.ok")
          }}</v-btn>
          <v-spacer></v-spacer>
        </v-card-actions>
      </v-card>
    </v-dialog>
</div>
</template>

<script>
import { client } from "../rest.js";

export default {
  data: function () {
    return {
      loading: true,
      deleteMessageDialog: {
        messageId: null,
        show: false,
      },
    };
  },

  methods: {

    openDeleteMessageDialog: function(inboxMessageDto) {
      this.deleteMessageDialog = {
        messageId: inboxMessageDto.id,
        show: true,
      };
    },

    closeDeleteMessageDialog: function() {
      this.deleteMessageDialog.show = false;
    },

    deleteMessage: function(messageId) {
      client.deleteInboxMessage(messageId)
      this.closeDeleteMessageDialog()
    }

  },

  computed: {
    inboxMessages() {
      return this.$store.state.inboxMessages;
    },
  },

  mounted: async function () {
    console.log("dupa");
    var that = this;
    await client.getInboxMessages().then(function () {
      that.loading = false;
    });
  },
};
</script>