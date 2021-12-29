const path = require('path');
const CopyPlugin = require('copy-webpack-plugin');

// vue.config.js
module.exports = {
  "transpileDependencies": [
    "vuetify"
  ],
  devServer: {
    port: 8080,
    proxy: 'http://localhost'
  },
  configureWebpack: {
    devtool: 'source-map',
    plugins: [
      // Copy over media resources from the Blockly package
      new CopyPlugin([
        {
          from: path.resolve(__dirname, './node_modules/blockly/media'),
          to: path.resolve(__dirname, '../output/web/media')
        }
      ])
    ]
  },
  outputDir: '../output/web'
}
