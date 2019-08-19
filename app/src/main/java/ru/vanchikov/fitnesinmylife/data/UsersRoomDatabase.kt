package ru.vanchikov.fitnesinmylife.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ru.vanchikov.fitnesinmylife.data.model.LoggedInUser
import ru.vanchikov.fitnesinmylife.data.model.UserWays
import ru.vanchikov.fitnesinmylife.data.model.Ways


/**
 * This is the backend. The database. This used to be done by the OpenHelper.
 * The fact that this has very few comments emphasizes its coolness.
 */
@Database(entities = [LoggedInUser::class,UserWays::class,Ways::class], version = 1)
abstract class UsersRoomDatabase : RoomDatabase() {

    abstract fun getUsersDao(): LoginDataSource

    abstract fun getWaysDao(): WaysDao

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
                    "word_database"
                )
                    // Wipes and rebuilds instead of migrating if no Migration object.
                    // Migration is not part of this codelab.
                    .fallbackToDestructiveMigration()
                    .addCallback(UsersRoomDatabaseCallback(scope))
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
             * For this sample, we clear the database every time it is created or opened.
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
        suspend fun populateDatabase(thisDao: LoginDataSource) {
            // Start the app with a clean database every time.
            // Not needed if you only populate on creation.
            thisDao.deleteAll()
            val NewLoggedInUser1 = LoggedInUser("Alex","Onechi","qqqqqq", "a@b.c")
            val NewLoggedInUser2 = LoggedInUser("Djosh","Onetwo","qqqqqq", "b@b.c")
            val NewLoggedInUser3 = LoggedInUser("Kerdan","Onethree","qqqqqq", "c@b.c")
            val NewLoggedInUser4 = LoggedInUser("Okes","Onefour","qqqqqq", "d@b.c")

            thisDao.insert(NewLoggedInUser1)
            thisDao.insert(NewLoggedInUser2)
            thisDao.insert(NewLoggedInUser3)
            thisDao.insert(NewLoggedInUser4)


        }
    }

}
