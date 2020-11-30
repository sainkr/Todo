package hong.checklist.DB

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "challenge")
data class ChallengeEntity (
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "code")
    var code : Int,

    @ColumnInfo(name = "name")
    var name : String,

    @ColumnInfo(name = "add")
    var add: Int,

    @ColumnInfo(name = "contents")
    var contents : List<ChallengeContents>?= null
)