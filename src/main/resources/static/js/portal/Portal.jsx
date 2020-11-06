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
import Separator from "../components/Separator";
import '../../css/in_portal.css';

const Portal = () => {
    return (
        <>
            <Separator size={5}/>
            <div style={{float: 'left', width: '100%'}}>
                <div style={{float: 'left', paddingLeft: '70px'}}>
                    <h3 id="holder">끄투리오 - 글자로 놀자!</h3>
                    <img id="logo" src="/img/kkutu/short_logo.png" alt="끄투리오"/>
                </div>
                <div id="menu-bar" style={{float: 'right', marginTop: '25px'}}>
                    <a style={{backgroundColor: '#7289DA'}} target="_blank" href="//discord.gg/hzZa2YsfZQ}">공식 디스코드</a>
                </div>
                <div className="jjoriping" style={{marginTop: '40px'}}>
                    <img className="jjoObj jjoEyeL" style={{left: '-270px'}} src="/img/jjoeyeL.png" alt="캐릭터 왼쪽 눈"/><img
                    className="jjoObj jjoNose" style={{left: '-170px'}} src="/img/jjonose.png" alt="캐릭터 코"/><img
                    className="jjoObj jjoEyeR" style={{left: '-70px'}} src="/img/jjoeyeR.png" alt="캐릭터 오른쪽 눈"/>
                    <div className="jjoDisplayBar" style={{width: '325px', height: '85px'}}>
                        <button className="jjo-display" id="game-start">게임시작</button>
                    </div>
                </div>
            </div>

            <div style={{float: 'right', marginTop: '-125px', width: '50%'}}>
                <div className="server-list-box">
                    <h3 className="server-list-title">
                        <a id="server-refresh">
                            <i className="fa fa-refresh"/>
                            <div className="expl" style={{width: 'initial'}}>
                                <h5>채널 상태를 새로고침합니다.</h5>
                            </div>
                        </a>
                        <label>채널 목록</label>
                        <label id="server-total" style={{color: '#AAA', fontSize: '13px'}}/>
                    </h3>
                    <div id="server-list"/>
                </div>
            </div>

            <div style={{
                float: 'left',
                width: 'calc(100% - 4px)',
                border: '2px solid #CCC',
                marginTop: '10px',
                backgroundColor: '#EEE'
            }}>
                <iframe id="kkutu-bulletin" width="100%" height="400px" src="/kkutu_bulletin.html"/>
            </div>
            <Separator size={5}/>

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
            <Separator size={10}/>
        </>
    );
}

export default Portal;
