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
import {
    GameStartButton,
    JJoDisplayBar,
    JJoEyeL,
    JJoEyeR,
    JJoNose,
    JJoriping,
    LogoArea,
    LogoImg,
    LogoText,
    MenuBar,
    MenuBarText
} from "./TopLeftPanelStyle";

const TopLeftPanel = ({onGameStart}) => {
    return (
        <div style={{float: 'left', width: '100%'}}>
            <LogoArea>
                <LogoText>끄투리오 - 글자로 놀자!</LogoText>
                <LogoImg src="/img/kkutu/short_logo.png" alt="끄투리오"/>
            </LogoArea>
            <MenuBar>
                <MenuBarText color="#7289DA" target="_blank" href="//discord.gg/hzZa2YsfZQ">공식 디스코드</MenuBarText>
            </MenuBar>
            <JJoriping>
                <JJoEyeL src="/img/jjoeyeL.png" alt="캐릭터 왼쪽 눈"/><JJoNose
                src="/img/jjonose.png" alt="캐릭터 코"/><JJoEyeR
                src="/img/jjoeyeR.png" alt="캐릭터 오른쪽 눈"/>
                <JJoDisplayBar>
                    <GameStartButton onClick={onGameStart}>게임시작</GameStartButton>
                </JJoDisplayBar>
            </JJoriping>
        </div>
    );
}

export default TopLeftPanel