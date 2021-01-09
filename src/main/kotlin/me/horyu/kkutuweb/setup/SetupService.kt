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

package me.horyu.kkutuweb.setup

import me.horyu.kkutuweb.extension.getIp
import me.horyu.kkutuweb.extension.isGuest
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
        "(느으*[^가-힣]*금마?|니[^가-힣]*([엄앰엠])|(ㅄ|ㅅㅂ|ㅂㅅ)|미친([년놈])?|([병븅빙])[^가-힣]*신|보[^가-힣]*지|([새섀쌔썌])[^가-힣]*([기끼])|섹[^가-힣]*스|([시씨쉬쒸])이*입?[^가-힣]*([발빨벌뻘팔펄])|십[^가-힣]*새|씹|([애에])[^가-힣]*미|자[^가-힣]*지|존[^가-힣]*나|창[^가-힣]*([녀년놈])|:kd active|느으?금마?|니([엄앰엠])|미친([년놈])|([병븅빙])신|([새섀쌔썌])([기끼])|섹스|([시씨쉬쒸])이*입?([발빨벌뻘팔펄])|십새|([애에])미|존나|좆|죶|창([녀년놈])|fuck|sex|엉덩이|노무현|박근혜|문재인|문제인|응디|육변기|부엉이바위|응딩이|이기야|응기잇|\\\\^\\\\^ㅣ|조([가까])|멍([충청])이|네다통|통구이|민주화|ㅁㅈㅎ|느금마|니애미|니어미|니엄마|니애비|느그애비|느그애미|애미터|애미뒤|앰뒤|앰창|갈보|강간|개같|개년|개뇬|개미친|개부랄|개불알|개빠구리|개뼉다구|개새|개색|개쌔끼|개자석|개자슥|개자식|지랄|꼴통|느그엄마|느검마|니기미|니미|대가리|대갈|대갈빡|대갈통|대굴빡|뒈진다|뒤질|등쉰|등신|딸따뤼|딸따리|딸딸|딸딸이|똘추|매춘|몸파는|발정|배때지|병신|보지|보짓|보털|부랄|부럴|불알|븅딱|븅삼|븅쉰|븅신|빙딱|빙삼|빙시|빙신|빠구리|빠구뤼|빠꾸리|빠꾸뤼|빠순이|빠큐|뻑큐|뽀르노|뽀오지|사까쉬|사까시|상노무|상놈|새1끼|새갸|새꺄|새뀌|새끼|새뤼|새리|새캬|새키|성감대|성경험|성관계|성교육|섹하고|섹하구|섹하자|섹하장|쉬빨|쉬뻘|쉬뿔|쉬파|쉬팍|쉬팔|쉬팡|쉬펄|쉬퐁|쉬풀|스너프|스댕|스뎅|스발|스벌|스와핑|스왑|스트립|스팔|스펄|슴가|싀발|싀밸|싀벌|싀벨|싀봉|싀팍|싀팔|싀펄|시1발|싸갈통|싸까시|싸이코|싸카시|쌍너엄|쌍넌|쌍넘|쌍녀언|쌍년|쌍놈|쌍뇬|쌍눔|쌍늠|썅넘|썅년|썅놈|썅뇬|썅눔|썅늠|써글|썩을넘|썩을년|썩을놈|썩을뇬|썩을눔|썩을늠|씌([바박발방밸벌벙벨불블븰빨뻘파팍팔팡펄퐈퐝])|씌부랄|씨발|씨1발|씨팔|아가리|아가지|아갈|아괄|아구리|아구지|아구창|아굴창|자쥐|자즤|조까|조옷|주둥아리|주둥이|창녀|창년|후레|후장|3일한|SUCK|SEX|가카|간민정음|간철수|갓치|갓카|강된장남|개독교|개쌍도|개쌍디언|게이|경상디언|고무통|고무현|골좁이|규재찡|근혜찡|김치남|김치녀|까보전|꼬춘쿠키|꿈떡마쇼|낙태충|냄져|네다보|네다홍|盧|노무노무|노미넴|노부엉|노시계|노알라|노오란|노오랗|노운지|노짱|뇌물현|다이쥬|다카키마사오|땅크|로류|로린이|록또|머중|멍청도|메갈|명예자지|무현RT|미러링|박원숭|베충|베츙|보라니|보력지원|보밍|보빨|보슬|보테크|보혐|부랄발광|붸충|빵즈|빼애애액|빼애액|사타부언|새드|새부|설라디언|소라넷|숨쉴한|슨상|슨상님|씹선비|씹치|씹치남|알보칠|암베|앙망|애비충|엑윽|여혐|오유충|우덜식|우돌식|우흥|운지|욷높|웃흥|원조가카|유충|일간베스트|일게이|일밍|일밍아웃|일베|일베충|일벤저스|일벤져스|일붸|자1지|자들자들|자릉내|자취냄|자혐|장애인|재기찡|재기하다|전땅크|전라디언|젠신병자|좌고라|좌빨|좌음|좌좀|주절먹|중력절|짱깨|쪽국|챙놈|청웅|코르셋|탈김치|탈라도|통궈|투명애비|틀딱|피떡갈비|핑좆남|한남|한남또|한남충|할아보지|해상방위대|핵대중|핵펭귄|행게이|허수애비|호뽑뽑요|혼외수|홍어|홍팍|고추|ㅅㄲ|찐따|뒤져|@ㅐ미|쥬지|뷰지|육봉)".toRegex()
    private val nickRegex = "^[가-힣a-zA-Z0-9][가-힣a-zA-Z0-9 _-]*[가-힣a-zA-Z0-9]\$".toRegex()
    private val bannedWordRegex =
        "(개발자|개발진|관리자|관리진|운영자|운영진|공지|알림|시스템|admin|user|끄투리오|끄투코리아|horyu|호류|국립국어원|쪼리핑|모레미|모래미|백지현|류현오|분홍꽃|포카르|드라군|네코p|둔둔님|utella ?pluto|은둔자|김도훈|테드창)".toRegex()
    private val similarityRegex = "[-_ ]*".toRegex()

    fun needSetup(sessionProfile: SessionProfile): Boolean {
        val user = userDao.getUser(sessionProfile.id) ?: return true
        return user.nickname == null
    }

    fun setupNick(request: HttpServletRequest, session: HttpSession, nick: String): ActionResult {
        val isGuest = session.isGuest()
        val sessionProfile = loginService.getSessionProfile(session)

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

        if (nick.replace(" ", "").replace(" ", "").toLowerCase().contains(bannedWordRegex)) {
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