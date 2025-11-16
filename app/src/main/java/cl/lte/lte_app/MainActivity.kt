package cl.lte.lte_app

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import cl.lte.lte_app.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbar)

        // Oculta el título predeterminado, ya que usamos nuestro propio diseño
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        navController = navHostFragment.navController

        // Define los destinos de nivel superior (las pantallas principales)
        val topLevelDestinations = setOf(
            R.id.nav_transform, R.id.nav_reflow, R.id.nav_slideshow, R.id.nav_settings
        )

        appBarConfiguration = AppBarConfiguration(
            topLevelDestinations,
            binding.drawerLayout
        )

        // Configura la barra de acción para que funcione con NavController.
        setupActionBarWithNavController(navController, appBarConfiguration)

        // Conecta el Navigation Drawer y el Bottom Navigation View con el NavController
        binding.navView?.setupWithNavController(navController)
        binding.appBarMain.contentMain.bottomNavView?.setupWithNavController(navController)

        // ---- INICIO DE LA PERSONALIZACIÓN DE LA TOOLBAR ----

        // Asigna la acción de abrir el cajón a nuestro botón de hamburguesa personalizado (el de la derecha)
        binding.appBarMain.toolbarHamburgerIcon?.setOnClickListener {
            binding.drawerLayout?.openDrawer(GravityCompat.START)
        }

        // Escucha los cambios de destino para mostrar/ocultar los elementos correctos
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val isTopLevel = appBarConfiguration.topLevelDestinations.contains(destination.id)

            if (isTopLevel) {
                // ESTAMOS EN UNA PANTALLA PRINCIPAL
                binding.appBarMain.toolbar.navigationIcon = null
                binding.appBarMain.toolbarLogoLayout?.visibility = View.VISIBLE
                binding.appBarMain.toolbarHamburgerIcon?.visibility = View.VISIBLE
            } else {
                // ESTAMOS EN UNA PANTALLA SECUNDARIA
                binding.appBarMain.toolbarLogoLayout?.visibility = View.GONE
                binding.appBarMain.toolbarHamburgerIcon?.visibility = View.GONE
            }
        }

        // ---- FIN DE LA PERSONALIZACIÓN ----

        binding.appBarMain.fab?.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .setAnchorView(R.id.fab).show()
        }
    }

    // Se sobreescribe para evitar que se infle el menú de 3 puntos.
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // No se infla ningún menú aquí para eliminar los 3 puntos.
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}