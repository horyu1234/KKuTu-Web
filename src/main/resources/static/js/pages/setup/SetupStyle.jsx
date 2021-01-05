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

export const SetupTitle = styled.h1`
  padding-top: 30px;
  margin: 30px 0;
  text-align: center;
  font-weight: normal;
  font-size: 24px;
`

export const HelpTitle = styled.p`
  text-align: center;
  font-size: 18px;
  margin-bottom: 0;
  color: #4CAF50;
`

export const HelpText = styled.p`
  text-align: center;
  font-size: 14px;
`

export const NickInput = styled.input`
  margin: 0 auto;
  width: 435px;
  height: 25px;
  box-sizing: inherit;
  outline: none;
  ${props => props.hasError && `
    border: 2px solid #E57373;
    background-color: #FFCDD2;
  `}
`

export const ErrorFeedback = styled.p`
  text-align: center;
  font-size: 14px;
  color: #E57373;
`

export const OkBtn = styled.button`
  margin: 20px auto 0;
  width: 200px;
  height: 20px;
  cursor: pointer;
`