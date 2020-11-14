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

import React from "react";
import {ServerListBox, ServerListTitle, ServerRefresh, ServerTotal} from "./TopRightPanelStyle";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faSyncAlt} from "@fortawesome/free-solid-svg-icons";

const TopRightPanel = () => {
    return (
        <div style={{float: 'right', width: '50%', marginTop: '-125px'}}>
            <ServerListBox>
                <ServerListTitle>
                    <ServerRefresh>
                        <FontAwesomeIcon icon={faSyncAlt} size="sm"/>
                        <div className="expl" style={{width: 'initial'}}>
                            <h5>채널 상태를 새로고침합니다.</h5>
                        </div>
                    </ServerRefresh>
                    <label>채널 목록</label>
                    <ServerTotal>총 N명</ServerTotal>
                </ServerListTitle>
                <div/>
            </ServerListBox>
        </div>
    );
}

export default TopRightPanel