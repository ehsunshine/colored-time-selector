package de.ehsun.smartbooking.utils.gson

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.internal.bind.util.ISO8601Utils
import org.joda.time.DateTime
import java.lang.reflect.Type
import java.text.ParseException
import java.text.ParsePosition

class DateTimeDeserializer : JsonDeserializer<DateTime> {

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): DateTime? {
        return when {
            json != null -> tryParse(json.asString)
            else -> null
        }
    }

    private fun tryParse(text: String): DateTime? {
        return try {
            DateTime(ISO8601Utils.parse(text, ParsePosition(0)))
        } catch (ex: ParseException) {
            null
        }
    }
}