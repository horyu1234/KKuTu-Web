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

package me.horyu.kkutuweb.setting

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import me.horyu.kkutuweb.oauth.AuthVendor
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.stereotype.Component
import java.nio.file.Files
import java.nio.file.Paths
import javax.annotation.PostConstruct

@Component
class OAuthSetting(
        @Autowired private val applicationArguments: ApplicationArguments,
        @Autowired private val objectMapper: ObjectMapper
) {
    private val logger = LoggerFactory.getLogger(OAuthSetting::class.java)
    private lateinit var settingNode: JsonNode

    @PostConstruct
    fun init() {
        val optionValues = applicationArguments.getOptionValues("SETTING_DIR")
        if (optionValues.isNullOrEmpty()) {
            logger.error("프로그램 실행 인수에 SETTING_DIR 값이 누락되었습니다.")
        }

        val settingDir = optionValues[0]
        Files.newInputStream(Paths.get(settingDir, "oauth.json")).use {
            val br = it.bufferedReader()
            br.use { reader ->
                val jsonText = reader.readText()
                val jsonNode = objectMapper.readTree(jsonText)

                settingNode = jsonNode
            }
        }
    }

    fun getSetting(): Map<AuthVendor, OAuthVendorSetting> {
        val settings = HashMap<AuthVendor, OAuthVendorSetting>()
        for (oAuthVendorName in settingNode.fieldNames()) {
            val isEnable = settingNode[oAuthVendorName]["enable"].booleanValue()
            if (!isEnable) continue

            val vendorType = AuthVendor.fromName(oAuthVendorName)!!
            settings[vendorType] = OAuthVendorSetting(
                order = settingNode[oAuthVendorName]["order"].shortValue(),
                clientId = settingNode[oAuthVendorName]["client-id"].textValue(),
                clientSecret = settingNode[oAuthVendorName]["client-secret"].textValue(),
                callbackUrl = settingNode[oAuthVendorName]["callback-url"].textValue()
            )
        }

        return settings.toList().sortedBy { (_, value) -> value.order }.toMap()
    }
}
