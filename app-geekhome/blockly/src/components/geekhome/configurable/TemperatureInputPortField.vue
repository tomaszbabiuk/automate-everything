<template>
  <div>
        <configurable-genericportfield
          :id="id"
          :hint="hint"
          :required="required"
          :counter="counter"
          :portFilter="portFilter"
        />
  </div>
</template>
<script>
export default {
data: function() {
    return {
      portFilter: {
        canRead: true,
        canWrite: false,
        valueType: "Temperature"
      }
    };
  },
  props: ["id", "hint", "required", "counter"],
}
</script>
