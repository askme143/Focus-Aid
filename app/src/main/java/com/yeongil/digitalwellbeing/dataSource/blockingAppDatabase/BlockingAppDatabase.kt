package com.yeongil.digitalwellbeing.dataSource.blockingAppDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.yeongil.digitalwellbeing.dataSource.blockingAppDatabase.converters.Converters
import com.yeongil.digitalwellbeing.dataSource.blockingAppDatabase.dao.BlockingAppDao
import com.yeongil.digitalwellbeing.dataSource.blockingAppDatabase.dto.BlockingAppDto

@Database(
    entities = [
        BlockingAppDto::class,
    ],
    version = 1
)
@TypeConverters(Converters::class)
abstract class BlockingAppDatabase : RoomDatabase() {
    abstract fun blockingAppDao(): BlockingAppDao

    companion object {
        @Volatile
        private var INSTANCE: BlockingAppDatabase? = null

        fun getInstance(context: Context): BlockingAppDatabase {
            return INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                BlockingAppDatabase::class.java,
                "app_block_db"
            ).build().also {
                INSTANCE = it
            }
        }
    }
}