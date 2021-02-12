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
package me.horyu.kkutuweb.word

import com.fasterxml.jackson.annotation.JsonFormat

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class WordType(val typeCode: String, val typeName: String) {
    CLASS_0("0", "품사없음"),
    CLASS_1("1", "명사"),
    CLASS_2("2", "대명사"),
    CLASS_3("3", "수사"),
    CLASS_4("4", "조사"),
    CLASS_5("5", "동사"),
    CLASS_6("6", "형용사"),
    CLASS_7("7", "관형사"),
    CLASS_8("8", "부사"),
    CLASS_9("9", "감탄사"),
    CLASS_10("10", "접사"),
    CLASS_11("11", "의존명사"),
    CLASS_12("12", "보조동사"),
    CLASS_13("13", "보조형용사"),
    CLASS_14("14", "어미"),
    CLASS_15("15", "관형사·명사"),
    CLASS_16("16", "수사·관형사"),
    CLASS_17("17", "명사·부사"),
    CLASS_18("18", "감탄사·명사"),
    CLASS_19("19", "대명사·부사"),
    CLASS_20("20", "대명사·감탄사"),
    CLASS_21("21", "동사·형용사"),
    CLASS_22("22", "관형사·감탄사"),
    CLASS_23("23", "부사·감탄사"),
    CLASS_24("24", "의존명사·조사"),
    CLASS_25("25", "수사·관형사·명사"),
    CLASS_26("26", "대명사·관형사"),
    CLASS_N("n", "명사"),
    CLASS_A("a", "형용사 (a)"),
    CLASS_S("s", "형용사 (s)"),
    CLASS_V("v", "동사"),
    CLASS_R("r", "부사"),
    CLASS_VI("vi", "자동사"),
    CLASS_VT("vt", "타동사"),
    CLASS_P("p", "대명사"),
    CLASS_INT("int", "감탄사"),
    CLASS_PREP("prep", "전치사"),
    CLASS_AUX("aux", "조동사"),
    INJEONG("INJEONG", "어인정");

    companion object {
        fun findByCode(code: String): WordType? {
            for (wordType in values()) {
                if (wordType.typeCode == code) {
                    return wordType
                }
            }
            return null
        }
    }
}