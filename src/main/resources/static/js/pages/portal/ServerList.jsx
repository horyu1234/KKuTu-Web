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

import React, {useEffect, useState} from "react";
import sum from "lodash/sum";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faSyncAlt} from "@fortawesome/free-solid-svg-icons/faSyncAlt";
import Axios from "axios";
import {ServerListBox, ServerListTitle, ServerRefresh, ServerTotal} from "./ServerListStyle";
import Server from "./Server";

const ServerList = ({joinServer, onServerListUpdate}) => {
    const [isInitializing, setInitializing] = useState(true);
    const [messages, setMessages] = useState({});
    const [servers, setServers] = useState([]);
    const [totalPlayers, setTotalPlayers] = useState(0);
    const [maxUserPerServer, setMaxUserPerServer] = useState(400);
    const [refreshing, setRefreshing] = useState(false);

    useEffect(() => {
        setMessages(Messages);
        updateServerList();
        setInitializing(false);
    }, [])

    const updateServerList = () => {
        if (refreshing) return;
        setRefreshing(true);

        getServerList().then(res => {
            const data = res.data;
            setServers(data.list);
            setTotalPlayers(sum(data.list));
            setMaxUserPerServer(data.max);

            onServerListUpdate(data.list, data.max);

            setTimeout(() => setRefreshing(false), 500);
            setInterval(updateServerList, 60000);
        })
    }

    const getServerList = () => {
        return Axios.get('/servers');
    }

    if (isInitializing) return <h1>Page Initializing...</h1>
    return (
        <ServerListBox>
            <ServerListTitle>
                <ServerRefresh onClick={updateServerList}>
                    <FontAwesomeIcon icon={faSyncAlt} size="sm" spin={refreshing}/>
                    <div className="expl" style={{width: 'initial'}}>
                        <h5>{messages['portal.server-list.refresh-tooltip']}</h5>
                    </div>
                </ServerRefresh>
                <label>{messages['portal.server-list.title']}</label>
                <ServerTotal>{messages['portal.js.totalMember'].replace('{totalPlayers}', totalPlayers)}</ServerTotal>
            </ServerListTitle>
            {
                servers.map((server, index) => (
                    <Server key={index} index={index}
                            count={server} max={maxUserPerServer}
                            onClick={() => joinServer(index)}/>
                ))
            }
        </ServerListBox>
    );
}

export default ServerList