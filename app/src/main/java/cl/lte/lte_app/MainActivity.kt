package cl.lte.lte_app

import android.os.Bundle
import android.view.Menu
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.ImageSwitcher
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
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

    // --- INICIO: Variables para la galería de imágenes ---
    // Restaurando la lista de imágenes original que solicitaste.
    private val images = listOf(
        R.drawable.foto1,     // Corresponde a foto1.jpeg
        R.drawable.foto2,     // Corresponde a foto2.jpeg
        R.drawable.foto3      // Corresponde a foto3.jpeg
    )
    private var currentImageIndex = 0
    // --- FIN: Variables para la galería de imágenes ---

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbar)

        supportActionBar?.setDisplayShowTitleEnabled(false)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        navController = navHostFragment.navController

        val topLevelDestinations = setOf(
            R.id.nav_transform, R.id.nav_reflow, R.id.nav_slideshow, R.id.nav_settings
        )

        appBarConfiguration = AppBarConfiguration(
            topLevelDestinations,
            binding.drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.navView?.setupWithNavController(navController)
        binding.appBarMain.contentMain.bottomNavView?.setupWithNavController(navController)

        // --- INICIO: Lógica de la galería y personalización de la Toolbar ---

        // Detecta qué galería está visible (portrait o landscape) y la inicializa.
        binding.appBarMain.contentMain.imageSwitcher?.let { setupImageGallery(it) }
        // CORRECCIÓN: Se ha corregido el error tipográfico a 'appBarMain'.
        binding.appBarMain.contentMain.imageSwitcherLand?.let { setupImageGallery(it) }

        binding.appBarMain.toolbarHamburgerIcon?.setOnClickListener {
            binding.drawerLayout?.openDrawer(GravityCompat.START)
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            val isTopLevel = appBarConfiguration.topLevelDestinations.contains(destination.id)

            if (isTopLevel) {
                binding.appBarMain.toolbar.navigationIcon = null
                binding.appBarMain.toolbarLogoLayout?.visibility = View.VISIBLE
                binding.appBarMain.toolbarHamburgerIcon?.visibility = View.VISIBLE
            } else {
                binding.appBarMain.toolbarLogoLayout?.visibility = View.GONE
                binding.appBarMain.toolbarHamburgerIcon?.visibility = View.GONE
            }
        }

        // --- FIN: Lógica de la galería y personalización de la Toolbar ---

        binding.appBarMain.fab?.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .setAnchorView(R.id.fab).show()
        }
    }

    private fun setupImageGallery(imageSwitcher: ImageSwitcher) {
        imageSwitcher.setFactory {
            val imageView = ImageView(applicationContext)
            imageView.scaleType = ImageView.ScaleType.FIT_CENTER
            imageView.layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
            imageView
        }

        val fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
        val fadeOut = AnimationUtils.loadAnimation(this, android.R.anim.fade_out)
        imageSwitcher.inAnimation = fadeIn
        imageSwitcher.outAnimation = fadeOut

        // Asegurarse de que el índice es válido y la lista no está vacía antes de usarla
        if (images.isNotEmpty() && currentImageIndex < images.size) {
            imageSwitcher.setImageResource(images[currentImageIndex])
        } else if (images.isNotEmpty()) {
            currentImageIndex = 0 // Si el índice está fuera de rango, se resetea.
            imageSwitcher.setImageResource(images[currentImageIndex])
        }

        // Configura los botones para la galería PORTRAIT (horizontal)
        binding.appBarMain.contentMain.buttonNext?.setOnClickListener {
            currentImageIndex = (currentImageIndex + 1) % images.size
            imageSwitcher.setImageResource(images[currentImageIndex])
        }

        binding.appBarMain.contentMain.buttonPrev?.setOnClickListener {
            currentImageIndex = (currentImageIndex - 1 + images.size) % images.size
            imageSwitcher.setImageResource(images[currentImageIndex])
        }

        // Configura el botón para la galería LANDSCAPE (vertical)
        binding.appBarMain.contentMain.buttonDown?.setOnClickListener {
            currentImageIndex = (currentImageIndex + 1) % images.size
            imageSwitcher.setImageResource(images[currentImageIndex])
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return true // No se infla ningún menú para eliminar los 3 puntos.
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}