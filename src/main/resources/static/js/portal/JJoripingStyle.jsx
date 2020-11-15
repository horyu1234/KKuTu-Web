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
import styled, {createGlobalStyle} from "styled-components";

export const JJoripingGlobalStyle = createGlobalStyle`
   body * {
       box-sizing: border-box;
       user-select: none;
       outline: none;
   }
`

export const JJoripingContainer = styled.div`
   position: relative;
   width: 340px;
   height: 115px;
   background-color: #DEAF56;
   margin-top: 100px;
   border: 2px solid #000000;
   border-bottom-left-radius: 10px;
   border-bottom-right-radius: 10px;
`

export const JJoEyeL = styled.img`
   position: absolute;
   pointer-events: none;
   top: -30px;
   left: -2px;
`

export const JJoNose = styled.img`
   position: absolute;
   pointer-events: none;
   top: 5px;
   left: 50%;
   transform: translateX(-50%);
`

export const JJoEyeR = styled.img`
   position: absolute;
   pointer-events: none;
   top: -30px;
   right: -2px;
`

export const JJoDisplayBar = styled.div`
   width: 100%;
   height: 100%;
   background-color: #DEAF56;
   padding: 20px 5px 5px 5px;
   border-radius: 10px;
`

export const JJoDisplay = styled.button`
   width: 100%;
   height: 100%;
   cursor: pointer;
   color: #EEE;
   font-size: 20px;
   border: 0 none;
   background-color: rgba(0, 0, 0, 0.7);
   border-radius: 10px;
   font-weight: bold;
   transition: all 200ms ease;
   
   &:hover {
       font-size: 24px;
       background-color: rgba(0, 0, 0, 0.5);
   }
`