package xyz.geryakbarf.rakityuk.ui

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import xyz.geryakbarf.rakityuk.R
import xyz.geryakbarf.rakityuk.utils.DBUtils
import java.io.IOException
import java.sql.SQLException

class MenuActivity : AppCompatActivity() {

    private var isPressed = false
    private lateinit var sqlite: SQLiteDatabase
    private lateinit var dbUtils: DBUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        dbUtils = DBUtils(applicationContext)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_notifications, R.id.navigation_profile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        try {
            dbUtils.updateDataBase()
        } catch (a: IOException) {
            Toast.makeText(applicationContext, "Databases error!", Toast.LENGTH_SHORT).show()
        }

        try {
            sqlite = dbUtils.writableDatabase
        } catch (a: SQLException) {
            Toast.makeText(applicationContext, "Databases error no1!", Toast.LENGTH_SHORT).show()
        }
    }

}
