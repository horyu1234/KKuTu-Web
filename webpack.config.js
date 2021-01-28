/*
 * KKuTu-Web (https://github.com/horyu1234/KKuTu-Web)
 * Copyright (C) 2021. horyu1234(admin@horyu.me)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

const path = require('path');
const BundleAnalyzerPlugin = require('webpack-bundle-analyzer').BundleAnalyzerPlugin;
const TerserPlugin = require("terser-webpack-plugin");

const devPath = path.resolve(__dirname, 'src/main/resources/static/js/');
const deployPath = path.resolve(__dirname, 'build/resources/main/static/js/');

module.exports = (env, options) => {
    const mode = options.mode || 'development';
    console.log(`Build in ${mode} mode.`);

    return {
        entry: {
            vendor: ['@babel/polyfill'],
            appEntry: path.resolve(devPath, 'AppEntry.jsx')
        },
        output: {
            path: deployPath,
            filename: '[name].min.js'
        },
        module: {
            rules: [
                {
                    test: /\.(js|jsx)$/,
                    include: [
                        devPath
                    ],
                    exclude: /node_modules/,
                    use: {
                        loader: 'babel-loader',
                        options: {
                            presets: ['@babel/preset-env', '@babel/preset-react'],
                            plugins: ['@babel/plugin-proposal-class-properties']
                        },
                    }
                },
                {
                    test: /\.css$/,
                    exclude: /node_modules/,
                    use: ['style-loader', 'css-loader']
                },
                {
                    test: /\.(png|jpe?g|gif|svg)$/i,
                    exclude: /node_modules/,
                    use: ['file-loader']
                }
            ]
        },
        resolve: {
            extensions: ['.js', '.jsx']
        },
        devtool: mode === 'development' ? 'inline-source-map' : false,
        mode: mode,
        plugins: [
            new BundleAnalyzerPlugin({
                analyzerMode: 'static',
                openAnalyzer: false,
                reportFilename: 'bundle-analyze-report.html'
            })
        ],
        optimization: {
            minimizer: [new TerserPlugin()]
        },
        externals: {
            jquery: 'jQuery'
        }
    }
}