package hong.checklist.DB

import androidx.room.*

@Dao
interface ProfileDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE) // 기본키가 똑같으면 덮어 쓴다
    fun insert(profile : ProfileEntity)

    @Query("SELECT * FROM profile")
    fun getProfile() : List<ProfileEntity>

    @Query("DELETE FROM profile")
    fun deleteProfile()
}