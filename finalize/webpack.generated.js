/**
 * NOTICE: this is an auto-generated file
 *
 * This file has been generated by the `flow:prepare-frontend` maven goal.
 * This file will be overwritten on every run. Any custom changes should be made to webpack.config.js
 */
const fs = require('fs');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const ScriptExtHtmlWebpackPlugin = require('script-ext-html-webpack-plugin');
const CompressionPlugin = require('compression-webpack-plugin');
const ProgressPlugin = require('progress-webpack-plugin');

const path = require('path');

// the folder of app resources:
//  - flow templates for classic Flow
//  - client code with index.html and index.[ts/js] for CCDM
const frontendFolder = require('path').resolve(__dirname, 'frontend');
const fileNameOfTheFlowGeneratedMainEntryPoint = require('path').resolve(__dirname, 'target/frontend/generated-flow-imports.js');
const mavenOutputFolderForFlowBundledFiles = require('path').resolve(__dirname, 'target/classes/META-INF/VAADIN');
const useClientSideIndexFileForBootstrapping = true;
const clientSideIndexHTML = require('path').resolve(__dirname, 'target/index.html');
const clientSideIndexEntryPoint = require('path').resolve(__dirname, 'target/index');
// public path for resources, must match Flow VAADIN_BUILD
const build = 'build';
// public path for resources, must match the request used in flow to get the /build/stats.json file
const config = 'config';
// folder for outputting vaadin-bundle and other fragments
const buildFolder = `${mavenOutputFolderForFlowBundledFiles}/${build}`;
// folder for outputting stats.json
const confFolder = `${mavenOutputFolderForFlowBundledFiles}/${config}`;
// file which is used by flow to read templates for server `@Id` binding
const statsFile = `${confFolder}/stats.json`;
// make sure that build folder exists before outputting anything
const mkdirp = require('mkdirp');
mkdirp(buildFolder);
mkdirp(confFolder);

const devMode = process.argv.find(v => v.indexOf('webpack-dev-server') >= 0);
let stats;

// Open a connection with the Java dev-mode handler in order to finish
// webpack-dev-mode when it exits or crashes.
const watchDogPrefix = '--watchDogPort=';
let watchDogPort = devMode && process.argv.find(v => v.indexOf(watchDogPrefix) >= 0);
if (watchDogPort) {
  watchDogPort = watchDogPort.substr(watchDogPrefix.length);
  const runWatchDog = () => {
    var client = new require('net').Socket();

    client.on('error', function () {
      console.log("Watchdog connection error. Terminating webpack process...");
      client.destroy();
      process.exit(0);
    });
    client.on('close', function () {
      client.destroy();
      runWatchDog();
    });

    client.connect(watchDogPort, 'localhost');
  }
  runWatchDog();
}

// Compute the entries that webpack have to visit
const webPackEntries = {};
if (useClientSideIndexFileForBootstrapping) {
  webPackEntries.bundle = clientSideIndexEntryPoint;
  const dirName = path.dirname(fileNameOfTheFlowGeneratedMainEntryPoint);
  const baseName = path.basename(fileNameOfTheFlowGeneratedMainEntryPoint, '.js');
  if (fs.readdirSync(dirName).filter(fileName => !fileName.startsWith(baseName)).length) {
    // if there are vaadin exported views, add a second entry
    webPackEntries.export = fileNameOfTheFlowGeneratedMainEntryPoint;
  }
} else {
  webPackEntries.bundle = fileNameOfTheFlowGeneratedMainEntryPoint;
}

exports = {
  frontendFolder: `${frontendFolder}`,
  buildFolder: `${buildFolder}`,
  confFolder: `${confFolder}`
};

module.exports = {
  mode: 'production',
  context: frontendFolder,
  entry: webPackEntries,

  output: {
    filename: `${build}/vaadin-[name]-[contenthash].cache.js`,
    path: mavenOutputFolderForFlowBundledFiles,
    publicPath: 'VAADIN/',
  },

  resolve: {
    extensions: ['.ts', '.js'],
    alias: {
      Frontend: frontendFolder
    }
  },

  devServer: {
    // webpack-dev-server serves ./ ,  webpack-generated,  and java webapp
    contentBase: [mavenOutputFolderForFlowBundledFiles, 'src/main/webapp'],
    after: function(app, server) {
      app.get(`/stats.json`, function(req, res) {
        res.json(stats);
      });
      app.get(`/stats.hash`, function(req, res) {
        res.json(stats.hash.toString());
      });
      app.get(`/assetsByChunkName`, function(req, res) {
        res.json(stats.assetsByChunkName);
      });
      app.get(`/stop`, function(req, res) {
        // eslint-disable-next-line no-console
        console.log("Stopped 'webpack-dev-server'");
        process.exit(0);
      });
    }
  },

  module: {
    rules: [
      {
        test: /\.ts$/,
        use: [
          'awesome-typescript-loader'
        ]
      },
      {
        test: /\.css$/i,
        use: ['raw-loader']
      }
    ]
  },
  performance: {
    maxEntrypointSize: 2097152, // 2MB
    maxAssetSize: 2097152 // 2MB
  },
  plugins: [
    // Generate compressed bundles when not devMode
    !devMode && new CompressionPlugin(),
    // Give some feedback when heavy builds
    new ProgressPlugin(true),

    // Generates the stats file for flow `@Id` binding.
    function (compiler) {
      compiler.hooks.afterEmit.tapAsync("FlowIdPlugin", (compilation, done) => {
        let statsJson = compilation.getStats().toJson();
        // Get bundles as accepted keys
        let acceptedKeys = statsJson.assets.filter(asset => asset.chunks.length > 0)
          .map(asset => asset.chunks).reduce((acc, val) => acc.concat(val), []);

        // Collect all modules for the given keys
        const modules = collectModules(statsJson, acceptedKeys);

        // Collect accepted chunks and their modules
        const chunks = collectChunks(statsJson, acceptedKeys);

        let customStats = {
          hash: statsJson.hash,
          assetsByChunkName: statsJson.assetsByChunkName,
          chunks: chunks,
          modules: modules
        };

        if (!devMode) {
          // eslint-disable-next-line no-console
          console.log("         Emitted " + statsFile);
          fs.writeFile(statsFile, JSON.stringify(customStats, null, 1), done);
        } else {
          // eslint-disable-next-line no-console
          console.log("         Serving the 'stats.json' file dynamically.");

          stats = customStats;
          done();
        }
      });
    },

    // Includes JS output bundles into "index.html"
    useClientSideIndexFileForBootstrapping && new HtmlWebpackPlugin({
      template: clientSideIndexHTML,
      inject: 'head',
      chunks: ['bundle']
    }),
    useClientSideIndexFileForBootstrapping && new ScriptExtHtmlWebpackPlugin({
      defaultAttribute: 'defer'
    }),
  ].filter(Boolean)
};

/**
 * Collect chunk data for accepted chunk ids.
 * @param statsJson full stats.json content
 * @param acceptedKeys chunk ids that are accepted
 * @returns slimmed down chunks
 */
function collectChunks(statsJson, acceptedChunks) {
  const chunks = [];
  // only handle chunks if they exist for stats
  if (statsJson.chunks) {
    statsJson.chunks.forEach(function (chunk) {
      // Acc chunk if chunk id is in accepted chunks
      if (acceptedChunks.includes(chunk.id)) {
        const modules = [];
        // Add all modules for chunk as slimmed down modules
        chunk.modules.forEach(function (module) {
          const slimModule = {
            id: module.id,
            name: module.name,
            source: module.source,
          };
          modules.push(slimModule);
        });
        const slimChunk = {
          id: chunk.id,
          names: chunk.names,
          files: chunk.files,
          hash: chunk.hash,
          modules: modules
        }
        chunks.push(slimChunk);
      }
    });
  }
  return chunks;
}

/**
 * Collect all modules that are for a chunk in  acceptedChunks.
 * @param statsJson full stats.json
 * @param acceptedChunks chunk names that are accepted for modules
 * @returns slimmed down modules
 */
function collectModules(statsJson, acceptedChunks) {
  let modules = [];
  // skip if no modules defined
  if (statsJson.modules) {
    statsJson.modules.forEach(function (module) {
      // Add module if module chunks contain an accepted chunk and the module is generated-flow-imports.js module
      if (module.chunks.filter(key => acceptedChunks.includes(key)).length > 0
          && (module.name.includes("generated-flow-imports.js") || module.name.includes("generated-flow-imports-fallback.js"))) {
        let subModules = [];
        // Create sub modules only if they are available
        if (module.modules) {
          module.modules.forEach(function (module) {
            const subModule = {
              name: module.name,
              source: module.source
            };
            subModules.push(subModule);
          });
        }
        const slimModule = {
          id: module.id,
          name: module.name,
          source: module.source,
          modules: subModules
        };
        modules.push(slimModule);
      }
    });
  }
  return modules;
}
