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