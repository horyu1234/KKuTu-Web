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

package me.horyu.kkutuweb.geo

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

@Service
class GeoService(
    @Value("\${geo.api-key}") private val apiKey: String,
    @Autowired private val objectMapper: ObjectMapper
) {
    @Cacheable(value = ["ipGeoInfoCache"], key = "#ip")
    fun getGeoCountry(ip: String): String? {
        val response = requestHttp("https://geo.horyu.me/lookup?key=${apiKey}&ip=${ip}")
        val jsonNode = objectMapper.readTree(response)

        val geoLocation = jsonNode["geoLocation"]
        return if (geoLocation != null) geoLocation["country"].textValue() else null
    }

    fun requestHttp(url: String): String {
        val conn = URL(url).openConnection() as HttpURLConnection
        conn.requestMethod = "GET"
        conn.connectTimeout = 2000
        conn.readTimeout = 3000

        BufferedReader(InputStreamReader(conn.inputStream, Charsets.UTF_8)).use { br ->
            val readLine = br.readLine()

            conn.disconnect()
            return readLine
        }
    }
}