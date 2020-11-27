package hong.checklist.DB

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profile")
data class ProfileEntity (
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id : String,

    @ColumnInfo(name = "password")
    var password : String,

    @ColumnInfo(name = "name")
    var name : String
)