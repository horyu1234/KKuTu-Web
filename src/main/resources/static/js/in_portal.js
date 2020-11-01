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
        this._initStage();
        this._initBackground();
        this._bindEvents();
    }

    _initStage = () => {
        this._$stage = {
            list: $("#server-list"),
            total: $("#server-total"),
            start: $("#game-start"),
            ref: $("#server-refresh"),
            refi: $("#server-refresh>i")
        };
    }

    _initBackground = () => {
        $("#Background").removeAttr('src').addClass("jt-image").css({
            'background-image': "url('https://cdn.jsdelivr.net/npm/kkutuio@latest/img/kkutu/gamebg.png')",
            'background-size': "200px 200px"
        });
    }

    _bindEvents = () => {
        $(document).on('click', '#server-list div.server', (e) => {
            const $target = $(e.currentTarget);

            const serverId = $target.attr('data-server-id');
            const serverStatus = $target.attr('data-server-status');
            if (serverStatus === 'x') return;

            this._joinServer(serverId)
        })

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

        this._$stage.ref.on('click', (e) => {
            if (this._$stage.refi.hasClass("fa-spin")) {
                return alert(Messages['portal.js.serverWait']);
            }

            this._$stage.refi.addClass("fa-spin");
            setTimeout(this._updateServers, 1000);
        });

        setInterval(() => {
            this._updateServers();
        }, 60000);

        this._updateServers();
    }

    _updateServers = () => {
        $.get("/servers", (data) => {
            this._serverList = data.list;
            this._$stage.list.empty();

            let totalPlayers = 0;
            this._serverList.forEach((server, idx) => {
                let status = (server === null) ? "x" : "o";
                const peopleText = (status === "x") ? "-" : (server + " / " + this._serverMax);
                const limp = server / this._serverMax * 100;

                totalPlayers += server || 0;
                if (status === "o") {
                    if (limp >= 99) status = "q";
                    else if (limp >= 90) status = "p";
                }

                this._$stage.list.append(
                    $("<div>")
                        .addClass("server")
                        .attr({
                            'id': `server-${idx}`,
                            'data-server-id': idx,
                            'data-server-status': status
                        })
                        .append($("<div>").addClass(`server-status ss-${status}`))
                        .append($("<div>").addClass("server-name").html(Messages[`server.${idx}`]))
                        .append($("<div>").addClass("server-people graph")
                            .append($("<div>").addClass("graph-bar").width(limp + "%"))
                            .append($("<label>").html(peopleText))
                        )
                        .append($("<div>").addClass("server-enter").html(status === 'x' ? '-' : Messages['portal.js.serverEnter']))
                );
            });

            this._$stage.total.html("&nbsp;" + Messages['portal.js.totalMember'].replace('{totalPlayers}', totalPlayers));
            this._$stage.refi.removeClass("fa-spin");
            this._$stage.start.prop('disabled', false);
        });
    }

    _joinServer = (id) => {
        location.href = "/?server=" + id;
    }
}