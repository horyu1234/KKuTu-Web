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

package me.horyu.kkutuweb.locale

import me.horyu.kkutuweb.shop.ShopService
import me.horyu.kkutuweb.shop.response.ResponseGoodDetail
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.servlet.support.RequestContextUtils
import java.util.*
import javax.servlet.http.HttpServletRequest

@ControllerAdvice
class LocaleControllerAdvice(
        @Autowired private val localePropertyLoader: LocalePropertyLoader,
        @Autowired private val shopService: ShopService
) {
    @ModelAttribute("language")
    fun getLanguageCode(request: HttpServletRequest): String {
        return getLocale(request).language
    }

    @ModelAttribute("messages")
    fun getMessages(request: HttpServletRequest): Map<String, String> {
        return localePropertyLoader.getMessages(getLocale(request))
    }

    @ModelAttribute("goodDetails")
    fun getGoodDetails(request: HttpServletRequest): Map<String, ResponseGoodDetail> {
        return shopService.getGoodDetails()
    }

    private fun getLocale(request: HttpServletRequest): Locale {
        return RequestContextUtils.getLocale(request)
    }
}