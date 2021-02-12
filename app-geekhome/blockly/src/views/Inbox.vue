<template>
  <div id="xxx">
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
      <block type="text_charAt">
          <value name="VALUE">
              <block type="variables_get">
                  <field name="VAR">text</field>
              </block>
          </value>
      </block>
    </BlocklyComponent>
  </div>
</template>

<script>
export default {
  data(){
    return {
      code: '',
      options: {
        media: 'media/',
        trashcan: true,

        grid:
          {
            spacing: 25,
            length: 3,
            colour: '#fff',
            snap: true,
          },
      }
    }
  }
}
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
