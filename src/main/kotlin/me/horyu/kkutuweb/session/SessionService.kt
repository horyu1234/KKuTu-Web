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

package me.horyu.kkutuweb.session

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class SessionService(
        @Autowired private val sessionDao: SessionDao
) {
    @Scheduled(fixedDelay = 600000)
    fun removeExpiredSessions() {
        val sessions = sessionDao.list()
        val expiredSessions = sessions.filter {
            it.createdAt < System.currentTimeMillis() - 3600 * 12 * 1000
        }

        for (expiredSession in expiredSessions) {
            sessionDao.remove(expiredSession.id)
        }
    }
}