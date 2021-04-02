package com.eneskiziltas.zeytin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_giris.*


class GirisFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?{
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_giris, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Giriş ekranı araç animasyon işlemleri
        imageView.visibility =  View.INVISIBLE

        val anim = AnimationUtils.loadAnimation(context,R.anim.fade_in)
        anim.reset()
        far.clearAnimation()
        far.startAnimation(anim)

        anim.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationStart(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {

                val animation = AnimationUtils.loadAnimation(context, R.anim.fade_in)
                animation.reset()
                imageView.clearAnimation()
                imageView.startAnimation(animation)
                imageView.visibility = View.VISIBLE

            }
            override fun onAnimationRepeat(animation: Animation?) {
            }

        })


        //Button ClickListener`ları

       val textView5 = view.findViewById<TextView>(R.id.textView5)
        textView5.setOnClickListener {
       val action = GirisFragmentDirections.actionGirisFragmentToMapsFragment()
       Navigation.findNavController(it).navigate(action)
        }


       button3.setOnClickListener {

       val action = GirisFragmentDirections.actionGirisFragmentToMapsFragment()
               Navigation.findNavController(it).navigate(action)


        }
    }




}