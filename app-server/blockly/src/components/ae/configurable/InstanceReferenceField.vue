<template>
  <div>
        <v-select
          :items="instances"
          v-model="selected" 
          item-text="name"
          item-value="id"
          :label="hint"
          :error="error" 
          :error-messages="errorMessages"
          clearable
          return-object
          :multiple="isMultiple"
          dense
        ></v-select>
  </div>
</template>

<script>
import {UPDATE_INSTANCE_FIELD} from '../../../plugins/vuex'

export default {
data: function() {
    return {
      error: false,
      errorMessages: [],
      selected: [],
      items: []
    };
  },

  props: ["id", "hint", "required", "counter", "fieldRef"],

  computed: {
    validation() {
      return this.$store.state.instanceValidation[this.id]
    },

    storeFieldData() {
      return this.$store.state.newInstance.fields[this.id]
    },  

    instances() {
      var referencedClazz = this.fieldRef.clazz

      return this.$store.state.instances
                    .filter(x => {
                      return x.clazz === referencedClazz;
                    }).map(x => {
                      return { id: x.id, name: x.fields["name"]}
                    })

    },

    isMultiple() {
      return this.fieldRef.type === "Multiple"
    }
  },

  methods: {
    findById: function(id) {
      var y = this.$store.state.instances.find(
        x => x.id == id
      )
      if (y != null) {
        return y.fields["name"]
      } else {
        return null
      }
    },

    mapSelected: function(id) {
      return {
        id: eval(id), 
        name: this.findById(eval(id))
      } 
    }
  },

  watch: {
    validation(val) {
      this.error = !val.valid
      this.errorMessages = val.reasons
    },

    storeFieldData(ids) {
      if (this.isMultiple) { 
        var actualSelection = this.selected.map(x => x.id).join(',')
        console.log(actualSelection + ' ' + ids)   
        if (ids !== actualSelection) {
          this.selected = ids.split(",").map(this.mapSelected)
        }
      } else {
        var actualSelection2 = ids.id
        if (ids != actualSelection2) {
          this.selected = this.mapSelected(ids)
        }
      }
    },

    selected(value) {
      var onlyIds = null
      if (this.isMultiple) {
        onlyIds = value.filter(x => x.id != undefined).map(x => x.id).join(',')
      } else if (value != null) {
        onlyIds = value.id
      }

      this.$store.commit(UPDATE_INSTANCE_FIELD, { 
        name: this.id,
        value: onlyIds
      })
    }
  },

  mounted: function() {
    if (this.isMultiple) {
      this.selected = this.storeFieldData.split(",").map(this.mapSelected)
    } else {
      this.selected = this.mapSelected(this.storeFieldData)
    }
  }
};
</script>