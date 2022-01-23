package com.sig.umaps.ui.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sig.umaps.R
import com.sig.umaps.databinding.ActivityAboutBinding
import com.sig.umaps.ui.adapter.SliderImageAdapter
import com.smarteist.autoimageslider.SliderView

class AboutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageSlider = binding.imgSlider
        val imageList: ArrayList<Int> = ArrayList()

        imageList.add(R.drawable.image_one)
        imageList.add(R.drawable.image_two)
        imageList.add(R.drawable.image_three)
        imageList.add(R.drawable.image_four)

        setImageInSlider(imageList, imageSlider)

        binding.apply {
            menuBrowser.setOnClickListener {
                Intent(Intent.ACTION_VIEW, Uri.parse("https://ump.ac.id/")).apply {
                    startActivity(this)
                }
            }

            menuInstagram.setOnClickListener {
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.instagram.com/ump.ac.id/")
                ).apply {
                    startActivity(this)
                }
            }

            menuFacebook.setOnClickListener {
                Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/ump.ac.id/")).apply {
                    startActivity(this)
                }
            }

            menuYoutube.setOnClickListener {
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.youtube.com/channel/UCot5aqu6rlsx10XLMOIJxeg")
                ).apply {
                    startActivity(this)
                }
            }
        }
    }

    private fun setImageInSlider(images: ArrayList<Int>, imageSlider: SliderView) {
        val adapter = SliderImageAdapter()
        adapter.renewItems(images)
        imageSlider.setSliderAdapter(adapter)
        imageSlider.isAutoCycle = true
        imageSlider.startAutoCycle()
    }
}