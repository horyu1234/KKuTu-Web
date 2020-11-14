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
import styled from "styled-components";

export const LogoArea = styled.div`
    float: left;
    padding-left: 70px;
`

export const LogoText = styled.h3`
    width: 200px;
    text-align: center;
    text-shadow: 0 0 4px #333;
    font-weight: bold;
`

export const LogoImg = styled.img`
    margin-top: -20px;
`

export const MenuBar = styled.div`
    float: right;
    margin-top: 25px;
`

export const MenuBarText = styled.a`
    display: block;
    float: left;
    padding: 10px 5px;
    border-radius: 5px;
    margin: 0 3px;
    width: 120px;
    font-size: 15px;
    text-align: center;
    box-shadow: 0 1px 1px #333;
    background-color: ${props => props.color};
`

export const JJoriping = styled.div`
    width: 500px;
    margin-top: 40px;
`

export const JJoEyeL = styled.img`
    position: relative;
    top: 11px;
    left: -270px;
`

export const JJoNose = styled.img`
    position: relative;
    top: 9px;
    left: -170px;
`

export const JJoEyeR = styled.img`
    position: relative;
    top: 11px;
    left: -70px;
`

export const JJoDisplayBar = styled.div`
    padding: 20px 5px 5px 5px;
    border: 2px solid #000000;
    border-bottom-left-radius: 10px;
    border-bottom-right-radius: 10px;
    margin-top: -10px;
    width: 325px;
    height: 85px;
    background-color: #DEAF56;
`

export const GameStartButton = styled.button`
    float: left;
    width: 315px;
    height: 70px;
    font-size: 20px;
    font-weight: bold;
    border: none;
    padding: 8px 5px;
    border-radius: 10px;
    text-align: center;
    color: #EEEEEE;
    background-color: rgba(0, 0, 0, 0.7);
    display: block;
    box-sizing: inherit;
    font-family: inherit;
    transition: all 200ms ease;
    
    &:hover {
        font-size: 24px;
        background-color: rgba(0, 0, 0, 0.5);
    }
`
