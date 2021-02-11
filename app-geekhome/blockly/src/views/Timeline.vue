<template>
  <v-timeline>
    <v-timeline-item v-for="(event, i) in events" :key="i" :color="event.color" small>
      <template v-slot:opposite>
        <span :class="`headline font-weight-bold ${event.color}--text`" v-text="event.event"></span>
      </template>
      <v-card class="elevation-2">
        <v-card-title class="headline">
          <v-icon large left>$vuetify.icons.{{event.icon}}</v-icon>
          {{ event.actor }}
        </v-card-title>
        <v-card-text>{{ event.when }}</v-card-text>
      </v-card>
    </v-timeline-item>
  </v-timeline>
</template>

<script>
export default {
  data: () => ({
    events: [
      {
        actor: "Pump 1",
        color: "cyan",
        event: "ON",
        when: "now",
        icon: 'robot'
      },
      {
        actor: "Furnace",
        color: "green",
        event: "OFF",
        when: "2 mins ago",
        icon: 'robot'
      },
      {
        actor: "Pump 2",
        color: "pink",
        event: "ON",
        when: "25 mins ago",
        icon: 'robot'
      },
      {
        actor: "Light 3",
        color: "amber",
        event: "Heating",
        when: "1h ago",
        icon: 'robot'
      },
      {
        actor: "Light 2",
        color: "orange",
        event: "Cooling",
        icon: 'robot'
      }
    ]
  })
};
</script>
