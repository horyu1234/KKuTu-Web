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

import $ from 'jquery';

export default class Portal {
    _$stage = {};
    _serverMax = 400;
    _serverList = [];

    constructor() {
        this._bindEvents();
    }

    _bindEvents = () => {
        this._$stage.start.prop('disabled', true).on('click', () => {
            if ($("#account-info").html() === Messages['portal.js.login']) {
                this._joinServer(0);
                return;
            }

            for (let i = 0.9; i < 1; i += 0.01) {
                for (let server in this._serverList) {
                    if (this._serverList[server] < i * this._serverMax) {
                        this._joinServer(server);
                        return;
                    }
                }
            }
        });
    }
}