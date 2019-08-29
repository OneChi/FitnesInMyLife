package ru.vanchikov.fitnesinmylife.data.Database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ru.vanchikov.fitnesinmylife.data.DAO.UsersWaysDao
import ru.vanchikov.fitnesinmylife.data.model.LoggedInUser
import ru.vanchikov.fitnesinmylife.data.model.UserWays
import ru.vanchikov.fitnesinmylife.data.model.WayFix


/**
 * This is the backend. The database. This used to be done by the OpenHelper.
 * The fact that this has very few comments emphasizes its coolness.
 */
@Database(entities = [LoggedInUser::class,UserWays::class,WayFix::class], version = 2)
abstract class UsersRoomDatabase : RoomDatabase() {

    abstract fun getUsersDao(): UsersWaysDao


    companion object {
        @Volatile
        private var INSTANCE: UsersRoomDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): UsersRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UsersRoomDatabase::class.java,
                    "app_database"
                )
                    // Wipes and rebuilds instead of migrating if no Migration object.
                    .fallbackToDestructiveMigration()
                    .addCallback(
                        UsersRoomDatabaseCallback(
                            scope
                        )
                    )
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }

        private class UsersRoomDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            /**
             * Override the onOpen method to populate the database.
             */
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                // If you want to keep the data through app restarts,
                // comment out the following line.
                INSTANCE?.let { database ->
                    scope.launch {
                        populateDatabase(database.getUsersDao())
                    }
                }
            }
        }

        /**
         * Populate the database in a new coroutine.
         * If you want to start with more words, just add them.
         */
        suspend fun populateDatabase(thisWaysDao: UsersWaysDao) {
            // Start the app with a clean database every time.
            // Not needed if you only populate on creation.
            Log.d("MYDATABASE","INITDBSTART")
            //thisWaysDao.deleteAll()

            val NewLoggedInUser1 = LoggedInUser("Alex","Onechi","qqqqqq", "a@b.c")
            thisWaysDao.insert(NewLoggedInUser1)
            Log.d("MYDATABASE","INITDBEND")


        }
    }

}
