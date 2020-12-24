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
    JJoDisplay,
    JJoDisplayBar,
    JJoEyeL,
    JJoEyeR,
    JJoNose,
    JJoripingContainer,
    JJoripingGlobalStyle
} from "./JJoripingStyle";

const JJoriping = ({onGameStart}) => {
    return (
        <>
            <JJoripingGlobalStyle/>
            <JJoripingContainer>
                <JJoEyeL src="/img/jjoeyeL.png" alt="캐릭터 왼쪽 눈"/>
                <JJoNose src="/img/jjonose.png" alt="캐릭터 코"/>
                <JJoEyeR src="/img/jjoeyeR.png" alt="캐릭터 오른쪽 눈"/>
                <JJoDisplayBar>
                    <JJoDisplay onClick={onGameStart}>게임 시작</JJoDisplay>
                </JJoDisplayBar>
            </JJoripingContainer>
        </>
    );
}

export default JJoriping