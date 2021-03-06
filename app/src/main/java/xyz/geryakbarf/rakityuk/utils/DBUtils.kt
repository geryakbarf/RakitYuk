package xyz.geryakbarf.rakityuk.utils

import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream


class DBUtils(context: Context) :
    SQLiteOpenHelper(
        context,
        DB_NAME,
        null,
        DB_VERSION
    ) {
    private var mDataBase: SQLiteDatabase? = null
    private val mContext: Context
    private var mNeedUpdate = false
    @Throws(IOException::class)
    fun updateDataBase() {
        if (mNeedUpdate) {
            val dbFile =
                File(DB_PATH + DB_NAME)
            if (dbFile.exists()) dbFile.delete()
            copyDataBase()
            mNeedUpdate = false
        }
    }

    private fun checkDataBase(): Boolean {
        val dbFile =
            File(DB_PATH + DB_NAME)
        return dbFile.exists()
    }

    private fun copyDataBase() {
        if (!checkDataBase()) {
            this.readableDatabase
            close()
            try {
                copyDBFile()
            } catch (mIOException: IOException) {
                throw Error("ErrorCopyingDataBase")
            }
        }
    }

    @Throws(IOException::class)
    private fun copyDBFile() {
        val mInput =
            mContext.assets.open(DB_NAME)
        //InputStream mInput = mContext.getResources().openRawResource(R.raw.info);
        val mOutput: OutputStream =
            FileOutputStream(DB_PATH + DB_NAME)
        val mBuffer = ByteArray(1024)
        var mLength: Int
        while (mInput.read(mBuffer).also { mLength = it } > 0) mOutput.write(mBuffer, 0, mLength)
        mOutput.flush()
        mOutput.close()
        mInput.close()
    }

    @Throws(SQLException::class)
    fun openDataBase(): Boolean {
        mDataBase = SQLiteDatabase.openDatabase(
            DB_PATH + DB_NAME,
            null,
            SQLiteDatabase.CREATE_IF_NECESSARY
        )
        return mDataBase != null
    }

    @Synchronized
    override fun close() {
        if (mDataBase != null) mDataBase!!.close()
        super.close()
    }

    override fun onCreate(db: SQLiteDatabase) {}
    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        if (newVersion > oldVersion) mNeedUpdate = true
    }

    fun readMobo(socket: String): Cursor {
        val db = this.writableDatabase
        return db.rawQuery("SELECT * FROM pc_mobo WHERE Jenis like '%$socket%'", null)
    }

    fun readSocket(merek: String): Cursor {
        val db = this.writableDatabase
        return db.rawQuery("SELECT DISTINCT Jenis FROM pc_mobo WHERE Jenis like'%$merek%'", null)
    }

    fun readCPU(socket: String): Cursor {
        val db = this.writableDatabase
        return db.rawQuery("SELECT * FROM pc_cpu WHERE Socket like '%$socket%'", null)
    }

    fun readMemory(ddr: String): Cursor {
        val db = this.writableDatabase
        return db.rawQuery("SELECT * FROM pc_ram WHERE Tipe like '%$ddr%'", null)
    }

    companion object {
        private const val DB_NAME = "data.db"
        private var DB_PATH = ""
        private const val DB_VERSION = 1
    }

    init {
        DB_PATH =
            if (Build.VERSION.SDK_INT >= 17) context.applicationInfo.dataDir + "/databases/" else "/data/data/" + context.packageName + "/databases/"
        mContext = context
        copyDataBase()
        this.readableDatabase
    }
}