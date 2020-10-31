const path = require('path');
const BundleAnalyzerPlugin = require('webpack-bundle-analyzer').BundleAnalyzerPlugin;
const UglifyJsPlugin = require("uglifyjs-webpack-plugin");

const devPath = path.resolve(__dirname, 'src/main/resources/static/js/');
const deployPath = path.resolve(__dirname, 'build/resources/main/static/js/');

module.exports = {
    entry: {
        vendor: ['@babel/polyfill'],
        in_game_kkutu_help: path.resolve(devPath, 'in_game_kkutu_help.js'),
        in_portal: path.resolve(devPath, 'in_portal.js'),
        in_login: path.resolve(devPath, 'in_login.js')
    },
    output: {
        path: deployPath,
        filename: '[name].min.js',
        library: ['KKuTu', '[name]'],
        libraryTarget: "umd"
    },
    module: {
        rules: [
            {
                test: /\.js$/,
                include: [
                    devPath,
                ],
                exclude: /node_modules/,
                use: {
                    loader: 'babel-loader',
                    options: {
                        presets: ['@babel/preset-env'],
                        plugins: ['@babel/plugin-proposal-class-properties']
                    },
                },
            }
        ],
    },
    devtool: 'inline-source-map',
    mode: 'development',
    plugins: [
        new BundleAnalyzerPlugin({
            analyzerMode: 'static',
            openAnalyzer: false,
            reportFilename: 'bundle-analyze-report.html'
        })
    ],
    optimization: {
        minimizer: [new UglifyJsPlugin()]
    },
    externals: {
        jquery: 'jQuery'
    }
};
