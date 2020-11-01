/*
 * KKuTu-Web (https://github.com/horyu1234/KKuTu-Web)
 * Copyright (C) 2020. horyu1234(admin@horyu.me)
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
