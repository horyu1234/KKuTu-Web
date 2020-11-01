package me.horyu.kkutuweb.setting

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.stereotype.Component
import java.nio.file.Files
import java.nio.file.Paths
import javax.annotation.PostConstruct

@Component
class KKuTuSetting(
        @Autowired private val applicationArguments: ApplicationArguments,
        @Autowired private val objectMapper: ObjectMapper
) {
    private val logger = LoggerFactory.getLogger(KKuTuSetting::class.java)
    private lateinit var settingNode: JsonNode

    @PostConstruct
    fun init() {
        val optionValues = applicationArguments.getOptionValues("SETTING_DIR")
        if (optionValues.isNullOrEmpty()) {
            logger.error("프로그램 실행 인수에 SETTING_DIR 값이 누락되었습니다.")
        }

        val settingDir = optionValues[0]
        Files.newInputStream(Paths.get(settingDir, "kkutu.json")).use {
            val br = it.bufferedReader()
            br.use { reader ->
                val jsonText = reader.readText()
                val jsonNode = objectMapper.readTree(jsonText)

                settingNode = jsonNode
            }
        }
    }

    fun getVersion() = settingNode["version"].textValue()!!

    fun getMaxPlayer() = settingNode["max-player"].intValue()

    fun getGameServers() = settingNode["game-servers"].toList().map {
        GameServerSetting(
                it["is-secure"].booleanValue(),
                it["public-host"].textValue(),
                it["host"].textValue(),
                it["port"].intValue(),
                it["cid"].shortValue()
        )
    }

    fun getCryptoKey() = settingNode["crypto-key"].textValue()!!

    fun getKoThemes() = settingNode["word"]["themes"]["normal"]["ko"].toList().map(JsonNode::textValue)

    fun getKoInjeongThemes() = settingNode["word"]["themes"]["injeong"]["ko"].toList().map(JsonNode::textValue)

    fun getEnThemes() = settingNode["word"]["themes"]["normal"]["en"].toList().map(JsonNode::textValue)

    fun getEnInjeongThemes() = settingNode["word"]["themes"]["injeong"]["en"].toList().map(JsonNode::textValue)

    fun getInjeongPickExcepts() = settingNode["word"]["themes"]["ijp-except"].toList().map(JsonNode::textValue)

    fun getMoremiParts() = settingNode["moremi"]["parts"].toList().map(JsonNode::textValue)

    fun getMoremiCategories() = settingNode["moremi"]["categories"].toList().map(JsonNode::textValue)

    fun getMoremiEquips() = settingNode["moremi"]["equips"].toList().map(JsonNode::textValue)

    fun getMoremiGroups(): Map<String, List<String>> {
        val resultMap = HashMap<String, List<String>>()
        for (key in settingNode["moremi"]["groups"].fieldNames()) {
            resultMap[key] = settingNode["moremi"]["groups"][key].toList().map(JsonNode::textValue)
        }

        return resultMap
    }

    fun getGameRules() = objectMapper.writeValueAsString(settingNode["game-rules"])!!

    fun getGameOptions() = objectMapper.writeValueAsString(settingNode["game-options"])!!

    fun getGameOptionMap(): Map<String, String> {
        val resultMap = HashMap<String, String>()
        for (key in settingNode["game-options"].fieldNames()) {
            resultMap[key] = settingNode["game-options"][key]["name"].textValue()
        }

        return resultMap
    }

    fun getGameModes() = settingNode["game-rules"].fieldNames().asSequence().toList()
}