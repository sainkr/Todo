package hong.checklist.DB

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "friend")
data class FriendEntity (
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "code")
    var code : String,

    @ColumnInfo(name = "name")
    var name : String
)