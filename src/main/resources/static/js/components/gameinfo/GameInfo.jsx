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
import './gameInfo.css'
import GameGradeAllImg from '../../../img/game_grade_all.png';

const GameInfo = () => {
    const gradeDetailUrl = 'https://www.grac.or.kr/Statistics/Popup/Pop_StatisticsDetails.aspx?371e798f34f8dfd4a541d1f1f3960c41a6c813a6a053e8e5ec12581d53453bb0';
    const windowFeatures = 'width=800,height=587,fullscreen=no,resizeable=no,menubar=no,toolbar=no,location=no,directories=no,status=no';

    const handleClick = () => {
        window.open(gradeDetailUrl, 'GameDetail', windowFeatures);
    }

    return (
        <>
            <div style={{display: 'flex'}}>
                <img src={GameGradeAllImg} alt="전체 이용가" width="53" height="71"/>
                <table className="game-info-table" onClick={handleClick}>
                    <tbody>
                    <tr>
                        <td>제명</td>
                        <td>끄투리오</td>
                        <td>신청자·상호</td>
                        <td>류현오</td>
                    </tr>
                    <tr>
                        <td>이용등급</td>
                        <td>전체 이용가</td>
                        <td>등급분류번호</td>
                        <td>제 CC-NP-210121-005 호</td>
                    </tr>
                    <tr>
                        <td>등급분류 일자</td>
                        <td>2021년 01월 21일</td>
                        <td/>
                        <td/>
                        {/*<td>제작·배급업 신고번호</td>*/}
                        {/*<td>-</td>*/}
                    </tr>
                    </tbody>
                </table>
            </div>
        </>
    )
}

export default GameInfo;