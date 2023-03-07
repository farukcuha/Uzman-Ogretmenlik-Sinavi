package com.pandorina.uzman_ogretmenlik.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.google.android.material.transition.MaterialFadeThrough
import com.pandorina.uzman_ogretmenlik.util.NetworkConnection
import com.pandorina.uzman_ogretmenlik.R
import com.pandorina.uzman_ogretmenlik.util.LottieDialogFragment

abstract class BaseFragment<T : ViewBinding>(
    private val inflateMethod: (LayoutInflater, ViewGroup?, Boolean) -> T,
    private val homeButtonEnabled: Boolean,
    private val showActionBar: Boolean
) : Fragment() {
    var _binding: T? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflateMethod.invoke(layoutInflater, container, false)
        (activity as AppCompatActivity?)!!.supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(homeButtonEnabled)
            if (showActionBar) show() else hide()
        }
        enterTransition = MaterialFadeThrough()
        exitTransition = MaterialFadeThrough()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        onViewCreated()
    }

    abstract fun onViewCreated()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun setActionBarTitle(title: String){
        (activity as AppCompatActivity).supportActionBar?.title = title
    }

    protected inline fun <reified T> getArgument(tag: String, completion: (T) -> Unit) {
        arguments?.get(tag)?.let {
            completion(it as T)
        }
    }

    protected fun navigate(fragment: Fragment){
        parentFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }
}