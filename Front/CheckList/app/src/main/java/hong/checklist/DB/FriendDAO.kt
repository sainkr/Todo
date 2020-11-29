package hong.checklist.DB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FriendDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE) // 기본키가 똑같으면 덮어 쓴다
    fun insert(friend: FriendEntity)

    @Query("SELECT * FROM friend")
    fun getFriend() : List<FriendEntity>

}