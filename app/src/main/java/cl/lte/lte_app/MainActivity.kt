package cl.lte.lte_app

import android.os.Bundle
import android.view.Menu
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import cl.lte.lte_app.databinding.ActivityMainBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // --- INICIO: Variables para las galerías (restauradas) ---
    private val imagesTop = listOf(R.drawable.foto1, R.drawable.foto2, R.drawable.foto3)
    private var currentImageIndexTop = 0

    private val imagesMiddle = listOf(R.drawable.foto4, R.drawable.foto5, R.drawable.foto6)
    private var currentImageIndexMiddle = 0

    private val imagesBottom = listOf(R.drawable.foto7, R.drawable.foto8, R.drawable.foto9)
    private var currentImageIndexBottom = 0
    // --- FIN: Variables para las galerías ---


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbar)

        supportActionBar?.setDisplayShowTitleEnabled(false)

        // --- INICIO: Lógica de las galerías ---

        // Carga inicial de las imágenes
        updateImage(binding.appBarMain.contentMain.imageSwitcher, imagesTop, currentImageIndexTop)
        updateImage(binding.appBarMain.contentMain.imageSwitcherLand, imagesTop, currentImageIndexTop)
        updateImage(binding.appBarMain.contentMain.imageSwitcherMiddle, imagesMiddle, currentImageIndexMiddle)
        updateImage(binding.appBarMain.contentMain.imageSwitcherBottom, imagesBottom, currentImageIndexBottom)
        updateImage(binding.appBarMain.contentMain.imageSwitcherBottomLand, imagesBottom, currentImageIndexBottom)

        // Configuración de los botones
        setupGalleryControls()

        binding.appBarMain.toolbarHamburgerIcon?.setOnClickListener {
            binding.drawerLayout?.openDrawer(GravityCompat.START)
        }

        // --- FIN: Lógica de las galerías ---

        binding.appBarMain.fab?.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .setAnchorView(R.id.fab).show()
        }
    }

    private fun setupGalleryControls() {
        val content = binding.appBarMain.contentMain

        // Galería Superior
        content.buttonNext?.setOnClickListener {
            currentImageIndexTop = (currentImageIndexTop + 1) % imagesTop.size
            updateImage(content.imageSwitcher, imagesTop, currentImageIndexTop)
            updateImage(content.imageSwitcherLand, imagesTop, currentImageIndexTop)
        }
        content.buttonPrev?.setOnClickListener {
            currentImageIndexTop = (currentImageIndexTop - 1 + imagesTop.size) % imagesTop.size
            updateImage(content.imageSwitcher, imagesTop, currentImageIndexTop)
            updateImage(content.imageSwitcherLand, imagesTop, currentImageIndexTop)
        }
        content.buttonDown?.setOnClickListener {
            currentImageIndexTop = (currentImageIndexTop + 1) % imagesTop.size
            updateImage(content.imageSwitcherLand, imagesTop, currentImageIndexTop)
        }

        // Galería Central
        content.buttonNextMiddle?.setOnClickListener {
            currentImageIndexMiddle = (currentImageIndexMiddle + 1) % imagesMiddle.size
            updateImage(content.imageSwitcherMiddle, imagesMiddle, currentImageIndexMiddle)
        }
        content.buttonPrevMiddle?.setOnClickListener {
            currentImageIndexMiddle = (currentImageIndexMiddle - 1 + imagesMiddle.size) % imagesMiddle.size
            updateImage(content.imageSwitcherMiddle, imagesMiddle, currentImageIndexMiddle)
        }
        content.buttonDownMiddle?.setOnClickListener {
            currentImageIndexMiddle = (currentImageIndexMiddle + 1) % imagesMiddle.size
            updateImage(content.imageSwitcherMiddle, imagesMiddle, currentImageIndexMiddle)
        }

        // Galería Inferior
        content.buttonNextBottom?.setOnClickListener {
            currentImageIndexBottom = (currentImageIndexBottom + 1) % imagesBottom.size
            updateImage(content.imageSwitcherBottom, imagesBottom, currentImageIndexBottom)
            updateImage(content.imageSwitcherBottomLand, imagesBottom, currentImageIndexBottom)
        }
        content.buttonPrevBottom?.setOnClickListener {
            currentImageIndexBottom = (currentImageIndexBottom - 1 + imagesBottom.size) % imagesBottom.size
            updateImage(content.imageSwitcherBottom, imagesBottom, currentImageIndexBottom)
            updateImage(content.imageSwitcherBottomLand, imagesBottom, currentImageIndexBottom)
        }
        content.buttonDownBottom?.setOnClickListener {
            currentImageIndexBottom = (currentImageIndexBottom + 1) % imagesBottom.size
            updateImage(content.imageSwitcherBottomLand, imagesBottom, currentImageIndexBottom)
        }
    }

    private fun updateImage(imageView: ImageView?, images: List<Int>, index: Int) {
        imageView?.let { iv ->
            if (images.isNotEmpty() && index < images.size) {
                Glide.with(this)
                    .load(images[index])
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(iv)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return true
    }
}