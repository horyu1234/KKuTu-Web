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
import {LogoArea, LogoImg, LogoText, MenuBar, MenuBarText, TopLeftPanelContainer} from "./TopLeftPanelStyle";
import {BrowserView, MobileView} from 'react-device-detect';
import JJoriping from "./JJoriping";

const TopLeftPanel = ({onGameStart}) => {
    return (
        <TopLeftPanelContainer>
            <LogoArea>
                <LogoText>끄투리오 - 글자로 놀자!</LogoText>
                <LogoImg src="/img/kkutu/short_logo.png" alt="끄투리오"/>
            </LogoArea>
            <BrowserView>
                <MenuBar>
                    <MenuBarText color="#7289DA" target="_blank" href="//discord.gg/hzZa2YsfZQ">공식 디스코드</MenuBarText>
                </MenuBar>
            </BrowserView>
            <JJoriping onGameStart={onGameStart}/>
            <MobileView>
                <MenuBar>
                    <MenuBarText color="#7289DA" target="_blank" href="//discord.gg/hzZa2YsfZQ">공식 디스코드</MenuBarText>
                </MenuBar>
            </MobileView>
        </TopLeftPanelContainer>
    );
}

export default TopLeftPanel