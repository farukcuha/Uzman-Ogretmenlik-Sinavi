package com.pandorina.uzman_ogretmenlik.util

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.pandorina.uzman_ogretmenlik.databinding.LayoutLottieDialogBinding

class LottieDialogFragment(
    private val lottieRes: Int,
    private val message: String,
    private val cancellable: Boolean = true
): DialogFragment() {

    private var _binding: LayoutLottieDialogBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LayoutLottieDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialog?.setCancelable(cancellable)
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.lottieAnimation.setAnimation(lottieRes)
        binding.tvMessage.text = message
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }
}