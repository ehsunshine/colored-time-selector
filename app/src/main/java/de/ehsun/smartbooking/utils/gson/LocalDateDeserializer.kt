package de.ehsun.smartbooking.utils.gson

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.internal.bind.util.ISO8601Utils
import org.joda.time.LocalDate
import java.lang.reflect.Type
import java.text.ParseException
import java.text.ParsePosition

class LocalDateDeserializer : JsonDeserializer<LocalDate> {

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): LocalDate? {
        return when {
            json != null -> tryParse(json.asString)
            else -> null
        }
    }

    private fun tryParse(text: String): LocalDate? {
        return try {
            LocalDate.fromDateFields(ISO8601Utils.parse(text, ParsePosition(0)))
        } catch (ex: ParseException) {
            null
        }
    }
}