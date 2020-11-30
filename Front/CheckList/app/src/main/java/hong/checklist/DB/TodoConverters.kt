package hong.checklist.DB

import androidx.room.TypeConverter
import com.google.gson.Gson

class TodoConverters {

    @TypeConverter
    fun listToJson(value: List<TodoContents>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String) = Gson().fromJson(value, Array<TodoContents>::class.java).toList()
}