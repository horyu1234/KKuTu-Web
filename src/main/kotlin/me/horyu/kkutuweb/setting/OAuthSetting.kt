package me.horyu.kkutuweb.setting

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import me.horyu.kkutuweb.oauth.VendorType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
class OAuthSetting(
        @Autowired private val objectMapper: ObjectMapper
) {
    private lateinit var settingNode: JsonNode

    @PostConstruct
    fun init() {
        val resource = ClassPathResource("oauth.json")

        val br = resource.inputStream.bufferedReader()
        br.use { reader ->
            val jsonText = reader.readText()
            val jsonNode = objectMapper.readTree(jsonText)

            settingNode = jsonNode
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
