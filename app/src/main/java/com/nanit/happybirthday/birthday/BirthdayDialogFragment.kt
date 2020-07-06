package com.nanit.happybirthday.birthday

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.nanit.happybirthday.R
import com.nanit.happybirthday.databinding.DialogBirthdayBinding
import com.nanit.happybirthday.util.ext.loadImage
import com.nanit.happybirthday.util.ext.myApplication
import javax.inject.Inject

class BirthdayDialogFragment : DialogFragment() {

    companion object {
        fun newInstance(): BirthdayDialogFragment {
            return BirthdayDialogFragment()
        }
    }

    private var _binding: DialogBirthdayBinding? = null
    private val binding
        get() = _binding!!

    @Inject
    protected lateinit var vmFactory: ViewModelProvider.Factory

    private val birthdayVm: BirthdayViewModel by lazy {
        ViewModelProvider(this, vmFactory)[BirthdayViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        context?.myApplication?.appComponent?.inject(this)
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        _binding = DialogBirthdayBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi(birthdayVm.getBirthdayStyle())
        observeVm()
    }

    override fun getTheme(): Int = R.style.FullScreenDialogTheme

    private fun initUi(style: BirthdayStyle) {
        with(binding) {
            ivCloseBtn.setOnClickListener { dismiss() }
            ivBackground.setImageResource(style.backgroundImageResId)
            ivCameraBtn.setImageResource(style.cameraIconResId)
        }
    }

    private fun observeVm() {
        with(birthdayVm) {
            name.observe(this@BirthdayDialogFragment, Observer { name ->
                binding.tvCongratsHeadline1.text = getString(R.string.birthday_headline, name)
            })
            photoUri.observe(this@BirthdayDialogFragment, Observer { uri ->
                uri?.let {
                    binding.ivBabyPhoto.loadImage(uri, true,
                        birthdayVm.getBirthdayStyle().photoPlaceholderResId)
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}