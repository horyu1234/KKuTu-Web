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
import '../../css/in_portal.css';
import {MiddlePanel, UpdateLog} from "./PortalStyle";
import Product from "../components/Product";
import KakaoAd from "../components/KakaoAd";
import TopLeftPanel from "./TopLeftPanel";
import TopRightPanel from "./TopRightPanel";

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
            <TopLeftPanel/>
            <TopRightPanel/>

            <MiddlePanel>
                <UpdateLog width="100%" height="400px" src="/kkutu_bulletin.html"/>
            </MiddlePanel>
            <Separator height={5}/>

            <Product id="qwShKF" title="광고" createWithShown={true}>
                <KakaoAd width={728} height={90} unit="DAN-1jyd80fuvhwmb"/>
            </Product>
            <Separator height={10}/>
        </>
    );
}

export default Portal;
