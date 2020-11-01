package me.horyu.kkutuweb.setting

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import me.horyu.kkutuweb.oauth.VendorType
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

    fun getSetting(): Map<VendorType, OAuthVendorSetting> {
        val settings = HashMap<VendorType, OAuthVendorSetting>()
        for (oAuthVendorName in settingNode.fieldNames()) {
            val isEnable = settingNode[oAuthVendorName]["enable"].booleanValue()
            if (!isEnable) continue

            val vendorType = VendorType.fromName(oAuthVendorName)!!
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
