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
          multiple
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
    }
  },

  watch: {
    validation(val) {
      this.error = !val.valid
      this.errorMessages = val.reasons
    },

    storeFieldData(ids) {      
      this.selected = ids.split(",").map(x=> {
        return {
          id: eval(x), 
          name: this.findById(eval(x))
        } 
      });
    },

    selected(value) {
      var onlyIds = value.map(x => x.id).join(',')
      this.$store.commit(UPDATE_INSTANCE_FIELD, { 
        name: this.id,
        value: onlyIds
      })
    }
  },

  mounted: function() {
    this.selected = this.storeFieldData.split(",").map(x=> {
      return {
        id: eval(x), 
        name: this.findById(eval(x))
      } 
    });
  }
};
</script>