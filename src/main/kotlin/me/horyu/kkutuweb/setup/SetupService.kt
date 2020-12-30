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

package me.horyu.kkutuweb.setup

import me.horyu.kkutuweb.extension.getIp
import me.horyu.kkutuweb.login.LoginService
import me.horyu.kkutuweb.result.ActionResult
import me.horyu.kkutuweb.result.NickChangeResult
import me.horyu.kkutuweb.result.RestResult
import me.horyu.kkutuweb.session.SessionProfile
import me.horyu.kkutuweb.user.UserDao
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpSession

@Service
class SetupService(
    @Autowired private val loginService: LoginService,
    @Autowired private val userDao: UserDao
) {
    private val logger = LoggerFactory.getLogger(SetupService::class.java)
    private val badWordRegex =
        "(느으*[^가-힣]*금마?|니[^가-힣]*([엄앰엠])|(ㅄ|ㅅㅂ|ㅂㅅ)|미친([년놈])?|([병븅빙])[^가-힣]*신|보[^가-힣]*지|([새섀쌔썌])[^가-힣]*([기끼])|섹[^가-힣]*스|([시씨쉬쒸])이*입?[^가-힣]*([발빨벌뻘팔펄])|십[^가-힣]*새|씹|([애에])[^가-힣]*미|자[^가-힣]*지|존[^가-힣]*나|창[^가-힣]*([녀년놈])|:kd active|느으?금마?|니([엄앰엠])|미친([년놈])|([병븅빙])신|([새섀쌔썌])([기끼])|섹스|([시씨쉬쒸])이*입?([발빨벌뻘팔펄])|십새|([애에])미|존나|좆|죶|지랄|창([녀년놈])|fuck|sex|엉덩이|노무현|응디|운지|노알라|부엉이바위|뇌물현|응딩이|이기야|응기잇|\\\\^\\\\^ㅣ|조([가까])|멍([충청])이)".toRegex()
    private val nickRegex = "^[가-힣a-zA-Z0-9][가-힣a-zA-Z0-9 _-]*[가-힣a-zA-Z0-9]\$".toRegex()
    private val bannedWordRegex = "(호류)|(김도훈)|(해티)".toRegex()
    private val similarityRegex = "[-_ ]*".toRegex()

    fun needSetup(sessionProfile: SessionProfile): Boolean {
        val user = userDao.getUser(sessionProfile.id) ?: return true
        return user.nickname == null
    }

    fun setupNick(request: HttpServletRequest, session: HttpSession, nick: String): ActionResult {
        val sessionProfile = loginService.getSessionProfile(session)
        val isGuest = sessionProfile == null

        if (isGuest || !needSetup(sessionProfile!!)) {
            return ActionResult(false, RestResult.BAD_REQUEST.name)
        }

        if (nick.length < 2 || nick.length > 18 || nick.isBlank()) {
            return ActionResult(false, NickChangeResult.INVALID_LENGTH.errorCode)
        }

        if (!nick.matches(nickRegex)) {
            return ActionResult(false, NickChangeResult.INVALID_PATTERN.errorCode)
        }

        if (nick.contains(badWordRegex)) {
            return ActionResult(false, NickChangeResult.HAS_BAD_WORDS.errorCode)
        }

        if (nick.contains(bannedWordRegex)) {
            return ActionResult(false, NickChangeResult.HAS_BANNED_WORDS.errorCode)
        }

        val similarityNick = similarityRegex.replace(nick, "")
        val similarityNicks = userDao.getSimilarityNicks()

        if (similarityNicks.contains(similarityNick)) {
            return ActionResult(false, NickChangeResult.ALREADY_USING.errorCode)
        }

        if (userDao.getUser(sessionProfile.id) == null) {
            userDao.newUser(sessionProfile.id, nick, similarityNick)

            logger.info("[${request.getIp()}] 신규 유저의 초기 닉네임을 설정했습니다. - 닉네임: $nick")
        } else {
            userDao.updateUser(
                sessionProfile.id, mapOf(
                    "nickname" to nick,
                    "\"meanableNick\"" to similarityNick
                )
            )

            logger.info("[${request.getIp()}] 기존 유저의 초기 닉네임을 설정했습니다. - 닉네임: $nick")
        }

        return ActionResult.success()
    }
}