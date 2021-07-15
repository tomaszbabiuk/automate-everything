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
        <v-list-item-content v-if="message.read">
          <v-list-item-title class="text-wrap">{{ message.id }}  {{
            message.subject
          }}</v-list-item-title>
          <v-list-item-subtitle class="text-wrap">{{
            message.body
          }}</v-list-item-subtitle>
        </v-list-item-content>
        <v-list-item-content v-else>
          <v-list-item-title class="text-wrap font-weight-bold">{{
            message.subject
          }}</v-list-item-title>
          <v-list-item-subtitle class="text-wrap font-weight-bold">{{
            message.body
          }}</v-list-item-subtitle>
        </v-list-item-content>
        <v-list-item-avatar v-if="message.read">
          <v-icon
            class="grey lighten-1"
            dark
            @click="openDeleteMessageDialog(message)"
          >
            mdi-delete
          </v-icon>
        </v-list-item-avatar>
        <v-list-item-avatar v-else>
          <v-icon
            class="grey lighten-1"
            dark
            @click="markMessageAsRead(message)"
          >
            mdi-check
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
          <v-btn
            color="blue darken-1"
            text
            @click="closeDeleteMessageDialog()"
            >{{ $vuetify.lang.t("$vuetify.common.cancel") }}</v-btn
          >
          <v-btn
            color="blue darken-1"
            text
            @click="deleteMessage(deleteMessageDialog.messageId)"
            >{{ $vuetify.lang.t("$vuetify.common.ok") }}</v-btn
          >
          <v-spacer></v-spacer>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <v-pagination
      v-if="inboxCount > 0"
      v-model="page"
      :length="pageCount"
      :total-visible="pageLimit"
    ></v-pagination>
    <div v-else>{{ $vuetify.lang.t("$vuetify.noDataText") }}</div>
  </div>
</template>

<script>
import { client } from "../rest.js";

export default {
  data: function () {
    return {
      pageLimit: 3,
      page: 1,
      loading: true,
      deleteMessageDialog: {
        messageId: null,
        show: false,
      },
    };
  },

  methods: {
    openDeleteMessageDialog: function (inboxMessageDto) {
      this.deleteMessageDialog = {
        messageId: inboxMessageDto.id,
        show: true,
      };
    },

    closeDeleteMessageDialog: function () {
      this.deleteMessageDialog.show = false;
    },

    deleteMessage: function (messageId) {
      client.deleteInboxMessage(messageId);
      this.closeDeleteMessageDialog();
      this.loadPage();
    },

    markMessageAsRead: function (inboxMessageDto) {
      client.markInboxMessageAsRead(inboxMessageDto.id);
    },

    loadPage: async function () {
      var that = this;
      await client
        .getInboxMessages(this.pageLimit, (this.page - 1) * this.pageLimit)
        .then(function () {
          that.loading = false;
        });
    },
  },

  computed: {
    inboxMessages() {
      return this.$store.state.inboxMessages;
    },

    inboxCount() {
      return this.$store.state.inboxTotalCount;
    },

    pageCount() {
      return Math.ceil(this.inboxCount / this.pageLimit);
    },
  },

  watch: {
    pageCount(value) {
      console.log("page count updated to " + value);
      if (this.page > this.pageCount) {
        this.page--;
      }
    },

    page() {
      this.loadPage();
    },
  },

  mounted: function () {
    this.loadPage();
  },
};
</script>