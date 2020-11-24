package hong.checklist.DB

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface TodoDAO {
    @Insert(onConflict = REPLACE) // 기본키가 똑같으면 덮어 쓴다
    fun insert(todo : TodoEntity)

    @Query("SELECT * FROM todo WHERE date = :today")
    fun getContent(today: String) : List<TodoEntity>

    @Delete
    fun delete(todo : TodoEntity)

}