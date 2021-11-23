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
import styled from "styled-components";
import {isBrowser, isMobile} from 'react-device-detect'

export const TopLeftPanelContainer = styled.div`
  ${isBrowser && `
      float: left;
      width: 100%;
   `} ${isMobile && `
      margin-left: calc(50% - 170px);
   `}
`

export const LogoArea = styled.div`
  ${isBrowser && `
      float: left;
      padding-left: 70px;
   `} ${isMobile && `
      position:absolute;
      left:50%;
      top:70px;
      transform:translateX(-50%);
   `}
`

export const LogoText = styled.h3`
  width: 200px;
  text-align: center;
  text-shadow: 0 0 4px #333;
  font-weight: bold;
`

export const LogoImg = styled.img`
  margin-top: -20px;
  user-select: none;
  pointer-events: none;
`

export const MenuBar = styled.div`
  ${isBrowser && `
      float: right;
   `} ${isMobile && `
      float: left;
      width: 200px;
      margin: 10px 0px 10px 105px;
   `}
`

export const MenuBarText = styled.a`
  display: block;
  float: left;
  padding: 10px 5px;
  border-radius: 5px;
  margin: 0 3px 5px 3px;
  width: 120px;
  font-size: 15px;
  text-align: center;
  box-shadow: 0 1px 1px #333;
  background-color: ${props => props.color};
`
