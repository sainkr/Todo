package hong.checklist.DB

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo")
data class TodoEntity (
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "date")
    var date : String,

    @ColumnInfo(name = "content_check")
    var contentList: List<TodoContents> ?= null,

    @ColumnInfo(name = "weather")
    var weather : Int,

    @ColumnInfo(name = "goal")
    var goal : Int
)