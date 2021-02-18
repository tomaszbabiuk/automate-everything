<template>
  <div>
    <div class="blocklyDiv" ref="blocklyDiv">
    </div>
    <xml ref="blocklyToolbox" style="display:none">
      <slot></slot>
    </xml>
  </div>
</template>

<script>

import Blockly from 'blockly';
import store, { UPDATE_INSTANCE_AUTOMATION } from '../plugins/vuex'

export default {
  name: 'BlocklyComponent',
  props: ['options', 'xml'],
  data(){
    return {
      workspace: null
    }
  },
  watch: {
    xml(value) {
      console.log('xml changed: ' + value)
    }
  },
  mounted() {
    var options = this.$props.options || {};
    if (!options.toolbox) {
      options.toolbox = this.$refs["blocklyToolbox"];
    }
    this.workspace = Blockly.inject(this.$refs["blocklyDiv"], options);

    if (this.xml != null) {
      Blockly.Xml.domToWorkspace(Blockly.Xml.textToDom(this.xml), this.workspace);
    } else {
      this.workspace.clear();
    }

    function onBlockChange(event) {
      if ((event.type == Blockly.Events.BLOCK_CHANGE) || (event.type == Blockly.Events.BLOCK_CREATE)) {
        var workspace = Blockly.Workspace.getById(event.workspaceId)
        var xml = Blockly.Xml.workspaceToDom(workspace)
        var xml_text = Blockly.Xml.domToText(xml)
        store.commit(UPDATE_INSTANCE_AUTOMATION, xml_text)
      }
    }

    this.workspace.addChangeListener(onBlockChange);
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.blocklyDiv {
  height: 100%;
  width: 100%;
  text-align: left;
}
</style>
