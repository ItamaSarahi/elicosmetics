package com.equipo1.proyectotecnologias.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.equipo1.proyectotecnologias.fragments.tienda.ShopOrdersFragment
import com.equipo1.proyectotecnologias.fragments.tienda.ShopOrdersStatusFragment

class ShopTabsPagerAdapter (
    fragmentManager: FragmentManager,
    lifecycle: androidx.lifecycle.Lifecycle,
    var numberOfTabs: Int
): FragmentStateAdapter(fragmentManager, lifecycle)
{


    override fun getItemCount(): Int {
        return numberOfTabs
    }

    override fun createFragment(position: Int): Fragment {

        when(position) {
            0 -> {
                val bundle = Bundle()
                bundle.putString("status", "PAGADO")
                val fragment = ShopOrdersStatusFragment()
                fragment.arguments = bundle
                return fragment
            }
            1 -> {
                val bundle = Bundle()
                bundle.putString("status", "DESPACHADO")
                val fragment = ShopOrdersStatusFragment()
                fragment.arguments = bundle
                return fragment
            }
            2 -> {
                val bundle = Bundle()
                bundle.putString("status", "EN CAMINO")
                val fragment = ShopOrdersStatusFragment()
                fragment.arguments = bundle
                return fragment
            }
            3 -> {
                val bundle = Bundle()
                bundle.putString("status", "ENTREGADO")
                val fragment = ShopOrdersStatusFragment()
                fragment.arguments = bundle
                return fragment
            }
            else -> return ShopOrdersStatusFragment()
        }

    }

}