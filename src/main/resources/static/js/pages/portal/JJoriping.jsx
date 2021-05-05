/*
 * KKuTu-Web (https://github.com/horyu1234/KKuTu-Web)
 * Copyright (C) 2021. horyu1234(admin@horyu.me)
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
    JJoripingGlobalStyle,
    Memorial0416
} from "./JJoripingStyle";

const JJoriping = ({onGameStart}) => {
    const IMAGE_CDN_URL = 'https://cdn.jsdelivr.net/gh/horyu1234/KKuTu-Web@kkutuio/src/main/resources/static/img';
    return (
        <>
            <JJoripingGlobalStyle/>
            <JJoripingContainer>
                {/*<Memorial0416 src={`${IMAGE_CDN_URL}/0416.png`} alt="0416"/>*/}
                <JJoEyeL src={`${IMAGE_CDN_URL}/jjoeyeL.png`} alt="캐릭터 왼쪽 눈"/>
                <JJoNose src={`${IMAGE_CDN_URL}/jjonose.png`} alt="캐릭터 코"/>
                <JJoEyeR src={`${IMAGE_CDN_URL}/jjoeyeR.png`} alt="캐릭터 오른쪽 눈"/>
                <JJoDisplayBar>
                    <JJoDisplay onClick={onGameStart}>게임 시작</JJoDisplay>
                </JJoDisplayBar>
            </JJoripingContainer>
        </>
    );
}

export default JJoriping