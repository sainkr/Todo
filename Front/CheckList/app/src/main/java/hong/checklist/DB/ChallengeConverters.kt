package hong.checklist.DB

import androidx.room.TypeConverter
import com.google.gson.Gson

class ChallengeConverters {
    @TypeConverter
    fun listToJson(value: List<ChallengeContents>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String) = Gson().fromJson(value, Array<ChallengeContents>::class.java).toList()
}