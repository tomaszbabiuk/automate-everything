<template>
<v-sheet
  elevation="1"
  height="90vh"
  width="100vw"
>
    <BlocklyComponent id="blockly1" :options="options">
      <category name="Automation">
        <block type="ae_condition"></block>
        <block type="ae_repeat"></block>
        <block type="ae_change_state"></block>
      </category>
      <category name="Logic">
        <block type="logic_compare"></block>
        <block type="logic_operation"></block>
        <block type="logic_boolean"></block>
        <block type="controls_ifelse"></block>
      </category>
    </BlocklyComponent>
</v-sheet>
</template>
<script>
import Blockly from 'blockly';


export default {
  data(){
    return {
      code: '',
      options: {
        media: '/media/',
        trashcan: true,
        grid:
          {
            spacing: 25,
            length: 3,
            colour: '#fff',
            snap: true
          },
      }
    }
  }
}

var conditionBlock = {
  "type": "ae_condition",
  "message0": "Warunek zewnętrzny: %1",
  "args0": [
    {
      "type": "field_dropdown",
      "name": "CONDITION_ID",
      "options": [
        [
          "Zmierzch w Krakowie",
          "cond101"
        ],
        [
          "Obecność w domu",
          "cond202"
        ],
        [
          "Nieobecność w domu",
          "cond303"
        ]
      ]
    }
  ],
  "inputsInline": true,
  "output": "Boolean",
  "colour": 345,
  "tooltip": "",
  "helpUrl": ""
}

var repeatBlock = {
  "type": "ae_repeat",
  "message0": "Powtarzaj co %1",
  "args0": [
    {
      "type": "field_dropdown",
      "name": "NAME",
      "options": [
        [
          "sekundę",
          "1"
        ],
        [
          "minutę",
          "60"
        ],
        [
          "godzinę",
          "3600"
        ]
      ]
    }
  ],
  "nextStatement": "Boolean",
  "colour": 315,
  "tooltip": "",
  "helpUrl": ""
}

var changeStateBlock = {
  "type": "ae_change_state",
  "message0": "%1",
  "args0": [
    {
      "type": "field_dropdown",
      "name": "NAME",
      "options": [
        [
          "Zmień stan tego urządzenia na WŁ",
          "ON"
        ],
        [
          "Zmień stan tego urządzenia na WYŁ",
          "OFF"
        ]
      ]
    }
  ],
  "previousStatement": null,
  "nextStatement": null,
  "colour": 230,
  "tooltip": "",
  "helpUrl": ""
}

Blockly.Blocks['ae_condition'] = {
  init: function() {
    this.jsonInit(conditionBlock);
  }
};

Blockly.Blocks['ae_repeat'] = {
  init: function() {
    this.jsonInit(repeatBlock);
  }
};

Blockly.Blocks['ae_change_state'] = {
  init: function() {
    this.jsonInit(changeStateBlock);
  }
};
</script>

<style>
#blockly1 {
  position: absolute;
  right: 0;
  top: 0;
  width: 100%;
  height: 100%;
}
</style>