package hong.checklist.DB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = arrayOf(TodoEntity::class, ProfileEntity::class ), version = 1)
@TypeConverters(TodoConverters::class)
abstract class CheckListDatabase : RoomDatabase() {
    abstract fun todoDAO() : TodoDAO
    abstract fun profileDAO() : ProfileDAO

    // 싱글턴
    companion object{
        var INSTANCE : CheckListDatabase?= null
        fun getInstance(context : Context) : CheckListDatabase? {
            if(INSTANCE == null){
                synchronized(CheckListDatabase::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        CheckListDatabase::class.java,"todo.db")
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }

            return INSTANCE
        }

    }
}