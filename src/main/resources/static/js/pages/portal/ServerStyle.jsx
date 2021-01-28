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
import styled from "styled-components"

export const ServerContainer = styled.div`
  float: left;
  margin: 2px 0;
  width: 360px;
  height: 16px;
  font-size: 13px;
  cursor: pointer;

  &:hover {
    background-color: rgba(255, 255, 255, 0.1);
  }
`

export const handleStatusColor = status => {
    switch (status) {
        case "online":
            return "#3E3";
        case "usually":
            return "#EE3";
        case "confusion":
            return "#F55";
        case "offline":
            return "#777";
        default:
            return "#fff";
    }
};

export const ServerStatus = styled.div`
  float: left;
  border-radius: 6px;
  margin: 2px;
  width: 12px;
  height: 12px;
  background-color: ${props => handleStatusColor(props.status)};
`

export const ServerName = styled.div`
  float: left;
  width: 100px;
`

export const ServerPeople = styled.div`
  float: left;
  width: 190px;
  height: 100%;
  box-shadow: 0 1px 1px #141414;
`

export const GraphBar = styled.div`
  background-image: url('/img/blue_gauge.png');
  width: ${props => props.percent}%;
  height: 100%;
`

export const GraphLabel = styled.label`
  position: relative;
  display: block;
  top: -14px;
  width: 100%;
  text-align: center;
  font-size: 11px;
`

export const ServerEnter = styled.div`
  float: left;
  width: 50px;
  text-align: center;
`