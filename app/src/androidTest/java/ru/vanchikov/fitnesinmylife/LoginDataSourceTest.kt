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
import ru.vanchikov.fitnesinmylife.data.WaysDao
import ru.vanchikov.fitnesinmylife.data.model.LoggedInUser
import ru.vanchikov.fitnesinmylife.data.model.UserWays
import ru.vanchikov.fitnesinmylife.data.model.Ways
import java.io.IOException


@RunWith(AndroidJUnit4::class)
class WordDaoTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var wordDao: LoginDataSource
    private lateinit var db: UsersRoomDatabase
    private lateinit var waysDao: WaysDao

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getTargetContext()
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, UsersRoomDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        wordDao = db.getUsersDao()
        waysDao = db.getWaysDao()
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



    @Test
    @Throws(Exception::class)
    fun deleteCascade() = runBlocking {

        //val NewLoggedInUser1 = LoggedInUser("Alex","Onechi","qqqqqq", "a@b.c")
        //val NewLoggedInUser2 = LoggedInUser("Djosh","Onetwo","qqqqqq", "b@b.c")
        //val NewLoggedInUser3 = LoggedInUser("Kerdan","Onethree","qqqqqq", "c@b.c")
        //val NewLoggedInUser4 = LoggedInUser("Okes","Onefour","qqqqqq", "d@b.c")

        val NewLoggedInUser1 = LoggedInUser("Alex","Onechi","qqqqqq", "a@b.c")
        val NewLoggedInUser2 = LoggedInUser("Djosh","Onetwo","qqqqqq", "b@b.c")
        val NewLoggedInUser3 = LoggedInUser("Kerdan","Onethree","qqqqqq", "c@b.c")
        val NewLoggedInUser4 = LoggedInUser("Okes","Onefour","qqqqqq", "d@b.c")

        wordDao.insert(NewLoggedInUser1)
        wordDao.insert(NewLoggedInUser2)
        wordDao.insert(NewLoggedInUser3)
        wordDao.insert(NewLoggedInUser4)

        val way1 = UserWays(1,"Alex",242)
        val way2 = UserWays(2,"Alex",321)
        val way3 = UserWays(3,"Alex",542)
        val way4 = UserWays(4,"Alex",341)
        val users = wordDao.getAlphabetizedUsers().waitForValue()
        waysDao.insertWay(way1)
        waysDao.insertWay(way2)
        waysDao.insertWay(way3)
        waysDao.insertWay(way4)

        val fix1 = Ways(1,12f,2.0,4.0,12312312,12f,"GPS",0)
        val fix2 = Ways(1,4f,3.0,6.0,22312312,142f,"NETWORK",0)
        waysDao.insertFix(fix1)
        waysDao.insertFix(fix2)

        var ways =waysDao.getAllWays().waitForValue()
        Assert.assertTrue(!ways.isEmpty())
        //ways.drop(ways.size)
        //wordDao.deleteUser("Alex")
        //ways = waysDao.getAllWays().waitForValue()
        //Assert.assertTrue(ways.isEmpty())
        var fixes = waysDao.getAllFixes().waitForValue()
        waysDao.deleteFix(fixes[0])
        fixes.drop(fixes.size)
        fixes = waysDao.getAllFixes().waitForValue()
        val a = fixes.isEmpty()
        Assert.assertTrue(fixes.isEmpty())
        //wordDao.deleteAll()
        //al allWords = wordDao.getAlphabetizedUsers().waitForValue()

        //Assert.assertTrue(allWords.isEmpty())
    }

}