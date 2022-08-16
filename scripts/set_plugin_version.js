const commander = require('commander');
const fs = require('fs');

const program = new commander.Command();

program.option('-v, --version <version>', 'new version to be set');

program.parse(process.argv);

(async () => {
  const options = program.opts();

  const version = options.version;

  // bumping version
  console.log('--------------------------------------------------');
  console.log(`Plugin.xml: bumping version to: ${version}`);

  const pluginXmlPath = './plugin.xml';
  let text = fs.readFileSync(pluginXmlPath, 'utf-8');

  text = text.replace(
    /<plugin id="cordova-plugin-advanced-imagepicker" version=".+" xmlns="http:\/\/apache.org\/cordova\/ns\/plugins\/1.0">/,
    `<plugin id="cordova-plugin-advanced-imagepicker" version="${version}" xmlns="http://apache.org/cordova/ns/plugins/1.0">`);

  fs.writeFileSync(pluginXmlPath, text, 'utf-8');

  console.log('--------------------------------------------------');
})();
