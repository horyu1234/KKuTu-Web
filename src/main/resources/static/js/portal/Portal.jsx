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

import React, {useEffect} from "react";
import $ from 'jquery';
import Separator from "../components/Separator";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faSyncAlt} from "@fortawesome/free-solid-svg-icons"
import '../../css/in_portal.css';
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
    MenuBarText,
    MiddlePanel,
    ServerListBox,
    ServerListTitle,
    ServerRefresh,
    ServerTotal,
    TopLeftPanel,
    TopRightPanel,
    UpdateLog
} from "./PortalStyle";

const Portal = () => {
    useEffect(() => {
        $("#Background").removeAttr('src').addClass("jt-image").css({
            'background-image': "url('https://cdn.jsdelivr.net/npm/kkutuio@latest/img/kkutu/gamebg.png')",
            'background-size': "200px 200px"
        });
    }, [])

    return (
        <>
            <Separator height={5}/>
            <TopLeftPanel>
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
                        <GameStartButton>게임시작</GameStartButton>
                    </JJoDisplayBar>
                </JJoriping>
            </TopLeftPanel>

            <TopRightPanel>
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
            </TopRightPanel>

            <MiddlePanel>
                <UpdateLog width="100%" height="400px" src="/kkutu_bulletin.html"/>
            </MiddlePanel>
            <Separator height={5}/>

            {/*<th:block*/}
            {/*    layout:replace="~{view/kkutu/product::product(id='TLXFQZ',title=#{portal.ad.title},createWithShown=true)}">*/}
            {/*    <th:block layout:fragment="content">*/}
            {/*        <ins className="kakao_ad_area" data-ad-height="90"*/}
            {/*             data-ad-unit="DAN-1jyd80fuvhwmb"*/}
            {/*             data-ad-width="728"*/}
            {/*             style="display:none;"></ins>*/}
            {/*        <script async src="//t1.daumcdn.net/kas/static/ba.min.js" type="text/javascript"></script>*/}
            {/*    </th:block>*/}
            {/*</th:block>*/}
            <Separator height={10}/>
        </>
    );
}

export default Portal;
