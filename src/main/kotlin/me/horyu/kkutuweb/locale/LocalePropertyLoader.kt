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

import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.Cacheable
import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.stereotype.Component
import java.util.*

@Component
class LocalePropertyLoader(
    @Value("\${spring.messages.basename}") private val baseName: String
) {
    private val messageSource: ExposedResourceBundleMessageSource

    init {
        messageSource = ExposedResourceBundleMessageSource()
        messageSource.setBasename(baseName)
        messageSource.setDefaultEncoding("UTF-8")
        messageSource.setFallbackToSystemLocale(false)
    }

    @Cacheable(value = ["messageProperties"], key = "#locale")
    fun getMessages(locale: Locale): Map<String, String> {
        val bundle = messageSource.getResourceBundle(baseName, locale)

        val messages = HashMap<String, String>()
        for (key in bundle.keySet()) {
            messages[key] = bundle.getString(key)
        }

        return messages
    }

    inner class ExposedResourceBundleMessageSource : ResourceBundleMessageSource() {
        public override fun getResourceBundle(basename: String, locale: Locale): ResourceBundle {
            return super.getResourceBundle(basename, locale)!!
        }
    }
}