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
        val TAG = BirthdayDialogFragment::class.java.simpleName

        fun newInstance(): BirthdayDialogFragment {
            return BirthdayDialogFragment()
        }
    }

    internal var callback: OnChangePhotoListener? = null

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun setOnChangePhotoListener(callback: OnChangePhotoListener) {
        this.callback = callback
    }

    override fun getTheme(): Int = R.style.FullScreenDialogTheme

    private fun initUi(style: BirthdayStyle) {
        with(binding) {
            ivCloseBtn.setOnClickListener { dismiss() }
            ivBackground.setImageResource(style.backgroundImageResId)
            ivCameraBtn.setImageResource(style.cameraIconResId)
            ivCameraBtn.setOnClickListener {
                callback?.onChangePhotoClicked()
            }
        }
    }

    private fun observeVm() {
        with(birthdayVm) {
            name.observe(this@BirthdayDialogFragment, Observer { name ->
                binding.tvCongratsHeadline1.text = getString(R.string.birthday_headline, name)
            })
            photoUri.observe(this@BirthdayDialogFragment, Observer { uri ->
                uri?.let {
                    binding.ivBabyPhoto.loadImage(
                        uri, true,
                        birthdayVm.getBirthdayStyle().photoPlaceholderResId
                    )
                }
            })
            age.observe(this@BirthdayDialogFragment, Observer {
                displayAge(it)
            })
        }
    }

    private fun displayAge(age: Age) {
        binding.tvCongratsHeadline2.text = when (age) {
            is Age.Years -> {
                resources.getQuantityString(
                    R.plurals.age_year,
                    if (age.showHalfYear) 2 else age.years
                )
            }
            is Age.Months -> {
                resources.getQuantityString(
                    R.plurals.age_month,
                    if (age.showHalfMonth) 2 else age.months
                )
            }
        }

        if (age.showHalf) {
            binding.ivAgeDigit1.setImageResource(getOneAndHalfDrawableResId())
            binding.ivAgeDigit2.visibility = View.GONE
            return
        }

        binding.ivAgeDigit1.setImageResource(getDigitDrawableResId(age.num))

        if (age.num/10 >= 1) {
            binding.ivAgeDigit2.visibility = View.VISIBLE
            binding.ivAgeDigit2.setImageResource(getDigitDrawableResId(age.num%10))
        } else {
            binding.ivAgeDigit2.visibility = View.GONE
        }
    }

    interface OnChangePhotoListener {
        fun onChangePhotoClicked()
    }
}