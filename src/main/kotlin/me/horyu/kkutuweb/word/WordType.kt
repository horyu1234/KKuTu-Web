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
    CLASS_0("0", "X"),
    CLASS_1("1", "명"),
    CLASS_2("2", "대명"),
    CLASS_3("3", "수"),
    CLASS_4("4", "조"),
    CLASS_5("5", "동"),
    CLASS_6("6", "형"),
    CLASS_7("7", "관"),
    CLASS_8("8", "부"),
    CLASS_9("9", "감"),
    CLASS_10("10", "접"),
    CLASS_11("11", "의명"),
    CLASS_12("12", "조동"),
    CLASS_13("13", "조형"),
    CLASS_14("14", "어"),
    CLASS_15("15", "관·명"),
    CLASS_16("16", "수·관"),
    CLASS_17("17", "명·부"),
    CLASS_18("18", "감·명"),
    CLASS_19("19", "대·부"),
    CLASS_20("20", "대·감"),
    CLASS_21("21", "동·형"),
    CLASS_22("22", "관·감"),
    CLASS_23("23", "부·감"),
    CLASS_24("24", "의명·조"),
    CLASS_25("25", "수·관·명"),
    CLASS_26("26", "대·관"),
    CLASS_N("n", "명"),
    CLASS_A("a", "형"),
    CLASS_S("s", "형"),
    CLASS_V("v", "동"),
    CLASS_R("r", "부"),
    CLASS_VI("vi", "자동"),
    CLASS_VT("vt", "타동"),
    CLASS_P("p", "대명"),
    CLASS_INT("int", "감"),
    CLASS_PREP("prep", "전"),
    CLASS_AUX("aux", "조동"),
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