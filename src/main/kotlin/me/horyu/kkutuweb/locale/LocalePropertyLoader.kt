package me.horyu.kkutuweb.locale

import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.Cacheable
import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.stereotype.Component
import java.util.*

@Component
class LocalePropertyLoader(
        @Value("\${spring.messages.basename}") private val baseName: String
) {
    private val messageSource: ExposedResourceBundleMessageSource

    init {
        messageSource = ExposedResourceBundleMessageSource()
        messageSource.setBasename(baseName)
        messageSource.setDefaultEncoding("UTF-8")
        messageSource.setFallbackToSystemLocale(false)
    }

    @Cacheable(value = ["messageProperties"], key = "#locale")
    fun getMessages(locale: Locale): Map<String, String> {
        val bundle = messageSource.getResourceBundle(baseName, locale)

        val messages = HashMap<String, String>()
        for (key in bundle.keySet()) {
            messages[key] = bundle.getString(key)
        }

        return messages
    }

    inner class ExposedResourceBundleMessageSource : ResourceBundleMessageSource() {
        public override fun getResourceBundle(basename: String, locale: Locale): ResourceBundle {
            return super.getResourceBundle(basename, locale)!!
        }
    }
}