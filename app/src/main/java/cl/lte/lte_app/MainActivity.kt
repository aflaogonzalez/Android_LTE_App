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
import cl.lte.lte_app.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // --- INICIO: Variables para la galería de imágenes SUPERIOR ---
    private val imagesTop = listOf(
        R.drawable.foto1,
        R.drawable.foto2,
        R.drawable.foto3
    )
    private var currentImageIndexTop = 0
    // --- FIN: Variables para la galería de imágenes SUPERIOR ---

    // --- INICIO: Variables para la galería de imágenes CENTRAL ---
    private val imagesMiddle = listOf(
        R.drawable.foto4,
        R.drawable.foto5,
        R.drawable.foto6
    )
    private var currentImageIndexMiddle = 0
    // --- FIN: Variables para la galería de imágenes CENTRAL ---


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbar)

        supportActionBar?.setDisplayShowTitleEnabled(false)

        // --- INICIO: Lógica de las galerías ---

        // Configura la galería SUPERIOR (Patrones nuevos)
        binding.appBarMain.contentMain.imageSwitcher?.let { setupTopImageGallery(it) }
        binding.appBarMain.contentMain.imageSwitcherLand?.let { setupTopImageGallery(it) }

        // Configura la galería CENTRAL (Puntos Nuevos)
        binding.appBarMain.contentMain.imageSwitcherMiddle?.let { setupMiddleImageGallery(it) }

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

    private fun setupTopImageGallery(imageSwitcher: ImageSwitcher) {
        setupGenericImageGallery(
            imageSwitcher = imageSwitcher,
            images = imagesTop,
            currentIndex = currentImageIndexTop,
            nextButton = binding.appBarMain.contentMain.buttonNext,
            prevButton = binding.appBarMain.contentMain.buttonPrev,
            downButton = binding.appBarMain.contentMain.buttonDown,
            onIndexChanged = { newIndex -> currentImageIndexTop = newIndex }
        )
    }

    private fun setupMiddleImageGallery(imageSwitcher: ImageSwitcher) {
        setupGenericImageGallery(
            imageSwitcher = imageSwitcher,
            images = imagesMiddle,
            currentIndex = currentImageIndexMiddle,
            nextButton = binding.appBarMain.contentMain.buttonNextMiddle,
            prevButton = binding.appBarMain.contentMain.buttonPrevMiddle,
            // --- CORRECCIÓN: Se activa el botón de flecha abajo para el modo landscape ---
            downButton = binding.appBarMain.contentMain.buttonDownMiddle,
            onIndexChanged = { newIndex -> currentImageIndexMiddle = newIndex }
        )
    }

    private fun setupGenericImageGallery(
        imageSwitcher: ImageSwitcher,
        images: List<Int>,
        currentIndex: Int,
        nextButton: View?,
        prevButton: View?,
        downButton: View?,
        onIndexChanged: (Int) -> Unit
    ) {
        var localIndex = currentIndex

        imageSwitcher.setFactory {
            val imageView = ImageView(applicationContext)
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            imageView.layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
            val paddingInDp = 8
            val scale = resources.displayMetrics.density
            val paddingInPx = (paddingInDp * scale + 0.5f).toInt()
            imageView.setPadding(paddingInPx, paddingInPx, paddingInPx, paddingInPx)
            imageView
        }

        val fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
        val fadeOut = AnimationUtils.loadAnimation(this, android.R.anim.fade_out)
        imageSwitcher.inAnimation = fadeIn
        imageSwitcher.outAnimation = fadeOut

        if (images.isNotEmpty() && localIndex < images.size) {
            imageSwitcher.setImageResource(images[localIndex])
        } else if (images.isNotEmpty()) {
            localIndex = 0
            onIndexChanged(localIndex)
            imageSwitcher.setImageResource(images[localIndex])
        }

        nextButton?.setOnClickListener {
            if (images.isNotEmpty()) {
                localIndex = (localIndex + 1) % images.size
                onIndexChanged(localIndex)
                imageSwitcher.setImageResource(images[localIndex])
            }
        }

        prevButton?.setOnClickListener {
            if (images.isNotEmpty()) {
                localIndex = (localIndex - 1 + images.size) % images.size
                onIndexChanged(localIndex)
                imageSwitcher.setImageResource(images[localIndex])
            }
        }

        downButton?.setOnClickListener {
            if (images.isNotEmpty()) {
                localIndex = (localIndex + 1) % images.size
                onIndexChanged(localIndex)
                imageSwitcher.setImageResource(images[localIndex])
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return true
    }

    // onSupportNavigateUp ya no es necesario porque no hay NavController
}