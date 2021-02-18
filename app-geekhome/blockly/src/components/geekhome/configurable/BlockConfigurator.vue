<template>
  <v-sheet elevation="1" height="90vh" width="100vw">
    <div class="blocklyDiv" ref="blocklyDiv"></div>
  </v-sheet>
</template>
<script>
import Blockly from "blockly";
import store, { UPDATE_INSTANCE_AUTOMATION } from '../../../plugins/vuex'

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
        toolbox: {
  "kind": "categoryToolbox",
  "contents": [
    {
      "kind": "category",
      "name": "Control",
      "contents": [
        {
          "kind": "block",
          "type": "controls_if"
        },
        {
          "kind": "block",
          "type": "controls_whileUntil"
        },
        {
          "kind": "block",
          "type": "controls_for"
        }
      ]
    },
    {
      "kind": "category",
      "name": "Logic",
      "contents": [
        {
          "kind": "block",
          "type": "logic_compare"
        },
        {
          "kind": "block",
          "type": "logic_operation"
        },
        {
          "kind": "block",
          "type": "logic_boolean"
        }
      ]
    }
  ]
}
      },
      blocks: null
    };
  },
  methods: {
    reloadBlocks(xml) {
      this.workspace.clear();
      if (xml != null) {
        Blockly.Xml.domToWorkspace(Blockly.Xml.textToDom(xml), this.workspace);
      }
    }
  },
  mounted: function () {
    console.log("mounted")
    this.workspace = Blockly.inject(this.$refs["blocklyDiv"], this.options);
    this.reloadBlocks(this.$store.state.newInstance.automation)

    function onBlockChange(event) {
      if (
        event.type == Blockly.Events.BLOCK_CREATE || Blockly.Events.BLOCK_CHANGE
      ) {
        var workspace = Blockly.Workspace.getById(event.workspaceId);
        var xml = Blockly.Xml.workspaceToDom(workspace);
        var xml_text = Blockly.Xml.domToText(xml);
        store.commit(UPDATE_INSTANCE_AUTOMATION, xml_text);
      }
    }

    this.workspace.addChangeListener(onBlockChange);
  },
};

var conditionBlock = {
  type: "ae_condition",
  message0: "Warunek zewnętrzny: %1",
  args0: [
    {
      type: "field_dropdown",
      name: "CONDITION_ID",
      options: [
        ["Zmierzch w Krakowie", "cond101"],
        ["Obecność w domu", "cond202"],
        ["Nieobecność w domu", "cond303"],
      ],
    },
  ],
  inputsInline: true,
  output: "Boolean",
  colour: 345,
  tooltip: "",
  helpUrl: "",
};

var repeatBlock = {
  type: "ae_repeat",
  message0: "Powtarzaj co %1",
  args0: [
    {
      type: "field_dropdown",
      name: "NAME",
      options: [
        ["sekundę", "1"],
        ["minutę", "60"],
        ["godzinę", "3600"],
      ],
    },
  ],
  nextStatement: "Boolean",
  colour: 315,
  tooltip: "",
  helpUrl: "",
};

var changeStateBlock = {
  type: "ae_change_state",
  message0: "%1",
  args0: [
    {
      type: "field_dropdown",
      name: "NAME",
      options: [
        ["Zmień stan tego urządzenia na WŁ", "ON"],
        ["Zmień stan tego urządzenia na WYŁ", "OFF"],
      ],
    },
  ],
  previousStatement: null,
  nextStatement: null,
  colour: 230,
  tooltip: "",
  helpUrl: "",
};

Blockly.Blocks["ae_condition"] = {
  init: function () {
    this.jsonInit(conditionBlock);
  },
};

Blockly.Blocks["ae_repeat"] = {
  init: function () {
    this.jsonInit(repeatBlock);
  },
};

Blockly.Blocks["ae_change_state"] = {
  init: function () {
    this.jsonInit(changeStateBlock);
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