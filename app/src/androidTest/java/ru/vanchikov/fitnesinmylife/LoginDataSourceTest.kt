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
import ru.vanchikov.fitnesinmylife.data.UsersWaysDao
import ru.vanchikov.fitnesinmylife.data.UsersRoomDatabase
import ru.vanchikov.fitnesinmylife.data.model.LoggedInUser
import ru.vanchikov.fitnesinmylife.data.model.UserWays
import ru.vanchikov.fitnesinmylife.data.model.Ways
import java.io.IOException


@RunWith(AndroidJUnit4::class)
class WordDaoTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var wordWaysDao: UsersWaysDao
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
        wordWaysDao = db.getUsersDao()

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

        wordWaysDao.insert(NewLoggedInUser)
        val allWords = wordWaysDao.getAlphabetizedUsers().waitForValue()
        Assert.assertEquals(allWords[0].userId, NewLoggedInUser.userId)
    }

    @Test
    @Throws(Exception::class)
    fun getAllWords() = runBlocking {
        val NewLoggedInUser1 = LoggedInUser("Alex","Onechi","qqqqqq", "a@b.c")

        wordWaysDao.insert(NewLoggedInUser1)
        val NewLoggedInUser2 = LoggedInUser("2","2","2", "a@2.c")

        wordWaysDao.insert(NewLoggedInUser2)
        val allWords = wordWaysDao.getAlphabetizedUsers().waitForValue()
        val user = wordWaysDao.getUserToken("a@b.c", "qqqqqq")
        Assert.assertEquals(user.userId , "Alex")
        Assert.assertEquals(allWords[0].userId, NewLoggedInUser2.userId)
        Assert.assertEquals(allWords[1].userId, NewLoggedInUser1.userId)
    }

    @Test
    @Throws(Exception::class)
    fun deleteAll() = runBlocking {
        val NewLoggedInUser1 = LoggedInUser("Alex","Onechi","qqqqqq", "a@b.c")

        wordWaysDao.insert(NewLoggedInUser1)
        val NewLoggedInUser2 = LoggedInUser("2","2","2", "a@2.c")

        wordWaysDao.insert(NewLoggedInUser2)
        wordWaysDao.deleteAll()
        val allWords = wordWaysDao.getAlphabetizedUsers().waitForValue()
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

        wordWaysDao.insert(NewLoggedInUser1)
        wordWaysDao.insert(NewLoggedInUser2)
        wordWaysDao.insert(NewLoggedInUser3)
        wordWaysDao.insert(NewLoggedInUser4)

        val way1 = UserWays(1,"Alex",242)
        val way2 = UserWays(2,"Alex",321)
        val way3 = UserWays(3,"Alex",542)
        val way4 = UserWays(4,"Alex",341)

        wordWaysDao.insertWay(way1)
        wordWaysDao.insertWay(way2)
        wordWaysDao.insertWay(way3)
        wordWaysDao.insertWay(way4)

        val fix1 = Ways(1,12f,2.0,4.0,12312312,12f,"GPS",0)
        val fix2 = Ways(1,4f,3.0,6.0,22312312,142f,"NETWORK",0)
        wordWaysDao.insertFix(fix1)
        wordWaysDao.insertFix(fix2)




        var ways =wordWaysDao.getAllWays().waitForValue()
        wordWaysDao.deleteByWayId(ways[0].wayId)
        ways.drop(ways.size)
        var fixes = wordWaysDao.getAllFixes().waitForValue()
        ways = wordWaysDao.getAllWays().waitForValue()
        val users = wordWaysDao.getAlphabetizedUsers().waitForValue()

        Assert.assertTrue(!ways.isEmpty())
        //ways.drop(ways.size)
        //wordWaysDao.deleteUser("Alex")
        //ways = waysDao.getAllWays().waitForValue()
        //Assert.assertTrue(ways.isEmpty())

        wordWaysDao.deleteFix(fixes[0])
        fixes.drop(fixes.size)
        fixes = wordWaysDao.getAllFixes().waitForValue()
        val a = fixes.isEmpty()
        Assert.assertTrue(fixes.isEmpty())
        //wordWaysDao.deleteAll()
        //al allWords = wordWaysDao.getAlphabetizedUsers().waitForValue()

        //Assert.assertTrue(allWords.isEmpty())
    }
    /*
    suspend  insertWayFix(fix : Ways)
    suspend  deleteFix(fix: Ways) : Int
    suspend  insertWay(way: UserWays)
    suspend  getUserTokenByLP(userlogin : String, userpassword : String) : LoggedInUser
    suspend  insertUser(user: LoggedInUser)
    suspend  deleteAllUsers()

    getAllUsers(): LiveData<List<LoggedInUser>>

    deleteUserByUserId(userId : String) : Int

    allUserWaysByUserId(userId: String): LiveData<Array<UserWays>>

    allWayFixByWayId(wayId: Int) : LiveData<Array<Ways>>

    deleteFixByFixId(fixId : Double) : Int

    getAllFixes() : LiveData<List<Ways>>

    deleteWayById(wayId : Int) : Int

    getAllWays(): LiveData<List<UserWays>>

     */
}