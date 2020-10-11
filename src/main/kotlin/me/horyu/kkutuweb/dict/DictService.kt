package me.horyu.kkutuweb.dict

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DictService(
        @Autowired private val wordDao: WordDao
) {
    fun getWord(id: String, lang: String): String {
        val word = when (lang) {
            "ko" -> {
                wordDao.getKoreanWord(id) ?: return "{\"error\":404}"
            }
            "en" -> {
                wordDao.getEnglishWord(id) ?: return "{\"error\":404}"
            }
            else -> {
                return "{\"error\":400}"
            }
        }

        return "{\"word\":\"$word\",\"mean\":\"${word.mean}\",\"theme\":\"${word.theme}\",\"type\":\"${word.type}\"}"
    }
}
