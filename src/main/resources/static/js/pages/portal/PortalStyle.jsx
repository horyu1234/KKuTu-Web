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

export const JJoriping = styled.div`
  width: 500px;
  margin-top: 40px;
`

export const MiddlePanel = styled.div`
  float: left;
  margin-top: 10px;

  ${isBrowser && `
       width: 100%;
    `} ${isMobile && `
       width: calc(100% - 20px);
       margin-left: 10px;
    `}
`

export const EventNotice = styled.iframe`
  margin-bottom: 10px;
  border: 3px solid rgb(223, 171, 1);
  background-color: rgb(238, 238, 238);
`

export const UpdateLog = styled.iframe`
  margin-bottom: 5px;
  border: 1px solid rgb(204, 204, 204);
  background-color: rgb(238, 238, 238);
`
