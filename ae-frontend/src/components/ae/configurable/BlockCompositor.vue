<template>
  <v-sheet elevation="1" height="90vh" width="100vw">
    <div class="blocklyDiv" ref="blocklyDiv"></div>
  </v-sheet>
</template>
<script>
import Blockly from "blockly";
import store, { UPDATE_INSTANCE_COMPOSITION } from "../../../plugins/vuex";
import { client } from "../../../rest.js";

export default {
  data() {
    return {
      options: {
        media: "/media/",
        trashcan: true,
        grid: {
          spacing: 25,
          length: 3,
          colour: "#fff",
          snap: true,
        },
        toolbox: null,
      },
      
      blocks: null,
    };
  },
  props: ["configurableClazz"],
  methods: {
    reloadWorkspace() {
      document.getElementsByClassName('blocklyDiv')[0].innerHTML = ""

      client.getCompositionToolboxWithCallback(this.configurableClazz, this.$store.state.newInstance.id, (data) => {
        this.setupWorkspace(data.toolbox);
        this.setupBlocks(data.blocks);
        this.reloadBlocks(this.$store.state.newInstance.composition);
      });
    },

    reloadBlocks(xml) {
      this.workspace.clear();

      if (xml != null) {
        Blockly.Xml.domToWorkspace(Blockly.Xml.textToDom(xml), this.workspace);
      }
    },

    setupWorkspace(toolbox) {
      this.options.toolbox = toolbox;
      this.options.maxInstances = {
        single: 1
      };
      this.workspace = Blockly.inject(this.$refs["blocklyDiv"], this.options);

      function onBlockChange(event) {
        if (
          event.type == Blockly.Events.BLOCK_CREATE ||
          Blockly.Events.BLOCK_CHANGE
        ) {
          var workspace = Blockly.Workspace.getById(event.workspaceId);
          var xml = Blockly.Xml.workspaceToDom(workspace);
          var xml_text = Blockly.Xml.domToText(xml);
          store.commit(UPDATE_INSTANCE_COMPOSITION, xml_text);
        }
      }

      this.workspace.addChangeListener(onBlockChange);
    },

    setupBlocks(blocks) {
      blocks.forEach(element => {
        Blockly.Blocks[element.type] = {
          init: function () {
            this.jsonInit(element);
          },
        };
      });
    }
  },
  mounted: function () {
    this.reloadWorkspace();
  },
};
</script>

<style scoped>
.blocklyDiv {
  height: 100%;
  width: 100%;
  text-align: left;
}
</style>