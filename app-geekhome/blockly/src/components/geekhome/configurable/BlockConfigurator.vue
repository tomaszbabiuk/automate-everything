<template>
<v-sheet
  elevation="1"
  height="90vh"
  width="100vw"
>
    <BlocklyComponent id="blockly1" :options="options">
      <block type="controls_ifelse"></block>
      <block type="logic_compare"></block>
      <block type="logic_operation"></block>
      <block type="controls_repeat_ext">
          <value name="TIMES">
              <shadow type="math_number">
                  <field name="NUM">10</field>
              </shadow>
          </value>
      </block>
            <block type="controls_repeat_ext">
          <value name="TIMES">
              <shadow type="math_number">
                  <field name="NUM">10</field>
              </shadow>
          </value>
      </block>
            <block type="controls_repeat_ext">
          <value name="TIMES">
              <shadow type="math_number">
                  <field name="NUM">10</field>
              </shadow>
          </value>
      </block>
      <block type="text_charAt">
          <value name="VALUE">
              <block type="variables_get">
                  <field name="VAR">text</field>
              </block>
          </value>
      </block>
    </BlocklyComponent>
</v-sheet>
</template>
<script>
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
</script>