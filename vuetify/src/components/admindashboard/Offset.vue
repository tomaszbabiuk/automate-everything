<template>
  <div
    :style="styles"
    :class="classes"
    class="v-offset"
  >
    <slot />
  </div>
</template>

<script>
export default {
  data: function () {
    return {
      count: 0
    }
  }
}
</script>