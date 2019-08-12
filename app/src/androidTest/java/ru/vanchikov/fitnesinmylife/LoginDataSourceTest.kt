package ru.vanchikov.fitnesinmylife

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import junit.framework.Assert
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ru.vanchikov.fitnesinmylife.data.LoginDataSource
import ru.vanchikov.fitnesinmylife.data.UsersRoomDatabase
import ru.vanchikov.fitnesinmylife.data.model.LoggedInUser
import java.io.IOException


@RunWith(AndroidJUnit4::class)
class WordDaoTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var wordDao: LoginDataSource
    private lateinit var db: UsersRoomDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getTargetContext()
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, UsersRoomDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        wordDao = db.getDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetWord() = runBlocking {
        val NewLoggedInUser = LoggedInUser("Alex","Onechi","qqqqqq", "a@b.c")

        wordDao.insert(NewLoggedInUser)
        val allWords = wordDao.getAlphabetizedUsers().waitForValue()
        Assert.assertEquals(allWords[0].userId, NewLoggedInUser.userId)
    }

    @Test
    @Throws(Exception::class)
    fun getAllWords() = runBlocking {
        val NewLoggedInUser1 = LoggedInUser("Alex","Onechi","qqqqqq", "a@b.c")

        wordDao.insert(NewLoggedInUser1)
        val NewLoggedInUser2 = LoggedInUser("2","2","2", "a@2.c")

        wordDao.insert(NewLoggedInUser2)
        val allWords = wordDao.getAlphabetizedUsers().waitForValue()
        val user = wordDao.getUserToken("a@b.c", "qqqqqq")
        Assert.assertEquals(user.userId , "Alex")
        Assert.assertEquals(allWords[0].userId, NewLoggedInUser2.userId)
        Assert.assertEquals(allWords[1].userId, NewLoggedInUser1.userId)
    }

    @Test
    @Throws(Exception::class)
    fun deleteAll() = runBlocking {
        val NewLoggedInUser1 = LoggedInUser("Alex","Onechi","qqqqqq", "a@b.c")

        wordDao.insert(NewLoggedInUser1)
        val NewLoggedInUser2 = LoggedInUser("2","2","2", "a@2.c")

        wordDao.insert(NewLoggedInUser2)
        wordDao.deleteAll()
        val allWords = wordDao.getAlphabetizedUsers().waitForValue()
        Assert.assertTrue(allWords.isEmpty())
    }
}