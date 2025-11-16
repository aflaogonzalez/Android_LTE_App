package cl.lte.lte_app

import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import cl.lte.lte_app.databinding.ActivityMainBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // --- INICIO: Variables para las galerías ---
    private val imagesTop = listOf(R.drawable.foto1, R.drawable.foto2, R.drawable.foto3)
    private var currentImageIndexTop = 0

    private val imagesMiddle = listOf(R.drawable.foto4, R.drawable.foto5, R.drawable.foto6)
    private var currentImageIndexMiddle = 0

    private val imagesBottom = listOf(R.drawable.foto7, R.drawable.foto8, R.drawable.foto9)
    private var currentImageIndexBottom = 0
    // --- FIN: Variables para las galerías ---

    private var panelCount = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbar)

        supportActionBar?.setDisplayShowTitleEnabled(false)

        // Detecta el número de paneles (imágenes por galería) basado en las vistas disponibles.
        panelCount = when {
            findViewById<View>(R.id.image_view_top_3) != null -> 3
            findViewById<View>(R.id.image_view_top_2) != null -> 2
            else -> 1
        }

        // Carga inicial de las imágenes
        updateGalleries()

        // Configuración de los botones
        setupGalleryControls()

        binding.appBarMain.toolbarHamburgerIcon?.setOnClickListener {
            binding.drawerLayout?.openDrawer(GravityCompat.START)
        }

        binding.appBarMain.fab?.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .setAnchorView(R.id.fab).show()
        }
    }

    private fun setupGalleryControls() {
        val content = binding.appBarMain.contentMain
        val step = panelCount

        // Si estamos en modo 3 paneles, la navegación se desactiva.
        if (panelCount == 3) {
            return
        }

        // Galería Superior
        content.buttonNext?.setOnClickListener {
            currentImageIndexTop = (currentImageIndexTop + step) % imagesTop.size
            updateGalleries()
        }
        content.buttonPrev?.setOnClickListener {
            currentImageIndexTop = (currentImageIndexTop - step + imagesTop.size) % imagesTop.size
            updateGalleries()
        }
        content.buttonDown?.setOnClickListener {
            currentImageIndexTop = (currentImageIndexTop + step) % imagesTop.size
            updateGalleries()
        }

        // Galería Central
        content.buttonNextMiddle?.setOnClickListener {
            currentImageIndexMiddle = (currentImageIndexMiddle + step) % imagesMiddle.size
            updateGalleries()
        }
        content.buttonPrevMiddle?.setOnClickListener {
            currentImageIndexMiddle = (currentImageIndexMiddle - step + imagesMiddle.size) % imagesMiddle.size
            updateGalleries()
        }
        content.buttonDownMiddle?.setOnClickListener {
            currentImageIndexMiddle = (currentImageIndexMiddle + step) % imagesMiddle.size
            updateGalleries()
        }

        // Galería Inferior
        content.buttonNextBottom?.setOnClickListener {
            currentImageIndexBottom = (currentImageIndexBottom + step) % imagesBottom.size
            updateGalleries()
        }
        content.buttonPrevBottom?.setOnClickListener {
            currentImageIndexBottom = (currentImageIndexBottom - step + imagesBottom.size) % imagesBottom.size
            updateGalleries()
        }
        content.buttonDownBottom?.setOnClickListener {
            currentImageIndexBottom = (currentImageIndexBottom + step) % imagesBottom.size
            updateGalleries()
        }
    }

    private fun updateGalleries() {
        when (panelCount) {
            3 -> {
                updateImageTriple(findViewById(R.id.image_view_top_1), findViewById(R.id.image_view_top_2), findViewById(R.id.image_view_top_3), imagesTop, currentImageIndexTop)
                updateImageTriple(findViewById(R.id.image_view_middle_1), findViewById(R.id.image_view_middle_2), findViewById(R.id.image_view_middle_3), imagesMiddle, currentImageIndexMiddle)
                updateImageTriple(findViewById(R.id.image_view_bottom_1), findViewById(R.id.image_view_bottom_2), findViewById(R.id.image_view_bottom_3), imagesBottom, currentImageIndexBottom)
            }
            2 -> {
                updateImagePair(findViewById(R.id.image_view_top_1), findViewById(R.id.image_view_top_2), imagesTop, currentImageIndexTop)
                updateImagePair(findViewById(R.id.image_view_middle_1), findViewById(R.id.image_view_middle_2), imagesMiddle, currentImageIndexMiddle)
                updateImagePair(findViewById(R.id.image_view_bottom_1), findViewById(R.id.image_view_bottom_2), imagesBottom, currentImageIndexBottom)
            }
            else -> {
                val content = binding.appBarMain.contentMain
                updateImage(content.imageViewTop, imagesTop, currentImageIndexTop)
                updateImage(content.imageViewMiddle, imagesMiddle, currentImageIndexMiddle)
                updateImage(content.imageViewBottom, imagesBottom, currentImageIndexBottom)
            }
        }
    }

    private fun updateImage(imageView: ImageView?, images: List<Int>, index: Int) {
        imageView?.let { iv ->
            if (images.isNotEmpty() && index < images.size) {
                Glide.with(this).load(images[index]).transition(DrawableTransitionOptions.withCrossFade()).into(iv)
            } else {
                iv.setImageDrawable(null)
            }
        }
    }

    private fun updateImagePair(iv1: ImageView?, iv2: ImageView?, images: List<Int>, index: Int) {
        updateImage(iv1, images, index)
        if (images.isNotEmpty()) {
            val secondImageIndex = (index + 1) % images.size
            updateImage(iv2, images, secondImageIndex)
        }
    }

    private fun updateImageTriple(iv1: ImageView?, iv2: ImageView?, iv3: ImageView?, images: List<Int>, index: Int) {
        updateImage(iv1, images, index)
        if (images.isNotEmpty()) {
            updateImage(iv2, images, (index + 1) % images.size)
            updateImage(iv3, images, (index + 2) % images.size)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return true
    }
}