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
import ServerList from "./ServerList";
import {TopRightPanelStyleContainer} from "./TopRightPanelStyle";

const TopRightPanel = ({joinServer, onServerListUpdate}) => {
    return (
        <TopRightPanelStyleContainer>
            <ServerList joinServer={joinServer} onServerListUpdate={onServerListUpdate}/>
        </TopRightPanelStyleContainer>
    );
}

export default TopRightPanel