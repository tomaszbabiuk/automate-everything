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
          <v-list-item-title class="text-wrap">{{
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
        <v-list-item-action>
          <v-list-item-action-text
            v-if="message.read"
            v-text="formatTimeAgoProxy(message.timestamp)"
          ></v-list-item-action-text>
          <v-list-item-action-text
            v-else
            class="font-weight-bold"
            v-text="formatTimeAgoProxy(message.timestamp)"
          ></v-list-item-action-text>
        </v-list-item-action>
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
    <v-dialog v-model="deleteAllDialog.show" max-width="500px">
      <v-card>
        <v-card-title class="headline">{{
          $vuetify.lang.t("$vuetify.inbox.delete_all_question")
        }}</v-card-title>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn
            color="blue darken-1"
            text
            @click="closeDeleteAllDialog()"
            >{{ $vuetify.lang.t("$vuetify.common.cancel") }}</v-btn
          >
          <v-btn
            color="blue darken-1"
            text
            @click="deleteAll()"
            >{{ $vuetify.lang.t("$vuetify.common.ok") }}</v-btn
          >
          <v-spacer></v-spacer>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <div class="text-center">
      <v-btn
        v-if="inboxMessages.length == 0 && inboxTotalCount == 0 && !loading"
        class="mt-5"
        disabled
        >{{ $vuetify.lang.t("$vuetify.noDataText") }}
      </v-btn>

      <v-btn
        class="mt-5"
        color="primary"
        @click="loadMore()"
        v-if="hasMoreItems"
        >{{ $vuetify.lang.t("$vuetify.common.load_more") }} ({{
          inboxTotalCount - inboxMessages.length
        }})
      </v-btn>

      <v-menu offset-y>
        <template v-slot:activator="{ on, attrs }">
          <v-btn class="mt-5 ms-5" color="secondary" dark v-bind="attrs" v-on="on"> {{
              $vuetify.lang.t("$vuetify.common.other")
            }} </v-btn>
        </template>
        <v-list>
          <v-list-item>
            <v-list-item-title @click="markAllRead()">{{
              $vuetify.lang.t("$vuetify.inbox.mark_all_read")
            }}</v-list-item-title>
          </v-list-item>
          <v-list-item>
            <v-list-item-title @click="openDeleteAllDialog()">{{
              $vuetify.lang.t("$vuetify.inbox.delete_all")
            }}</v-list-item-title>
          </v-list-item>
        </v-list>
      </v-menu>
    </div>
  </div>
</template>

<script>
import { client } from "../rest.js";
import { formatTimeAgo } from "../elapsed.js";

export default {
  data: function () {
    return {
      now: 0,
      pageLimit: 10,
      loading: true,
      deleteMessageDialog: {
        messageId: null,
        show: false,
      },
      deleteAllDialog: {
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

    openDeleteAllDialog: function () {
      this.deleteAllDialog = {
        show: true,
      };
    },

    loadMore: function () {
      var offset = this.inboxMessages.length;
      this.loadPage(false, offset);
    },

    closeDeleteMessageDialog: function () {
      this.deleteMessageDialog.show = false;
    },

    closeDeleteAllDialog: function () {
      this.deleteAllDialog.show = false;
    },

    deleteMessage: function (messageId) {
      client.deleteInboxMessage(messageId);
      if (this.inboxMessages.length < 5) {
        this.loadMore();
      }
      this.closeDeleteMessageDialog();
    },

    markMessageAsRead: function (inboxMessageDto) {
      client.markInboxMessageAsRead(inboxMessageDto.id);
    },

    loadPage: async function (clear, offset) {
      var that = this;
      await client
        .getInboxMessages(clear, this.pageLimit, offset)
        .then(function () {
          that.loading = false;
        });
    },

    formatTimeAgoProxy: function (timestamp) {
      return formatTimeAgo(this.now, timestamp);
    },

    deleteAll: function() {
      this.closeDeleteAllDialog();
      client.deleteAllInboxMessages();
    },

    markAllRead: function() {
      client.markAllInboxMessagesAsRead();
    }
  },

  computed: {
    inboxMessages() {
      return this.$store.state.inboxMessages;
    },

    inboxTotalCount() {
      return this.$store.state.inboxTotalCount;
    },

    inboxUnreadCount() {
      return this.$store.state.inboxUnreadCount;
    },

    hasMoreItems() {
      return this.inboxMessages.length < this.inboxTotalCount;
    },
  },

  watch: {
    now: {
      handler() {
        setTimeout(() => {
          var now = new Date().getTime();
          this.now = now;
          this.$forceUpdate();
        }, 1000);
      },
      immediate: true,
    },
  },

  mounted: function () {
    this.loadPage(true, 0);
  },
};
</script>