package com.nanit.happybirthday

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.nanit.happybirthday.view_model.DetailsViewModel
import javax.inject.Inject

class DetailsActivity : AppCompatActivity() {

    //@Inject lateinit var detailsViewModel: DetailsViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val detailsVm: DetailsViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[DetailsViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as NanitApp).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
    }
}
