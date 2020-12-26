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
import {
    GraphBar,
    GraphLabel,
    ServerContainer,
    ServerEnter,
    ServerName,
    ServerPeople,
    ServerStatus
} from "./ServerStyle";

const Server = ({index, count, max, onClick}) => {
    const [isInitializing, setInitializing] = useState(true);
    const [messages, setMessages] = useState({});
    const [isOnline, setOnline] = useState(false);
    const [status, setStatus] = useState('offline');
    const [peopleText, setPeopleText] = useState('');
    const [percent, setPercent] = useState(0);

    useEffect(() => {
        setMessages(Messages);
        setInitializing(false);

        const isOnline = count !== null;
        const percent = count / max * 100;

        setOnline(isOnline);
        setPeopleText(isOnline ? count + " / " + max : '-');
        setPercent(percent);

        if (isOnline) {
            if (percent >= 99) setStatus("confusion");
            else if (percent >= 90) setStatus("usually");
            else if (percent < 90) setStatus("online");
        } else setStatus("offline")
    }, [count])

    if (isInitializing) return <h1>Page Initializing...</h1>
    return (
        <ServerContainer onClick={isOnline ? onClick : undefined}>
            <ServerStatus status={status}/>
            <ServerName>{messages['server.' + index]}</ServerName>
            <ServerPeople>
                <GraphBar percent={percent}/>
                <GraphLabel>{peopleText}</GraphLabel>
            </ServerPeople>
            <ServerEnter>{isOnline ? messages['portal.js.serverEnter'] : '-'}</ServerEnter>
        </ServerContainer>
    );
}

export default Server