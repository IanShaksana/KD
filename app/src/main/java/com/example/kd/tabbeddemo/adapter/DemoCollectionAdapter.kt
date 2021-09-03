package com.example.kd.tabbeddemo.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.kd.fragment.manager.submission.deposito.core.DepositoManage
import com.example.kd.fragment.manager.submission.loan.core.LoanManage
import com.example.kd.fragment.manager.task.list.TaskManager
import com.example.kd.fragment.marketing.submission.deposito.core.Sub32Deposito
import com.example.kd.fragment.marketing.submission.loan.core.Sub31Loan
import com.example.kd.tabbeddemo.frag.DemoObjectFragment


private const val ARG_OBJECT = "object"

class DemoCollectionAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 5
    private lateinit var fragment: Fragment

    override fun createFragment(position: Int): Fragment {
        // Return a NEW fragment instance in createFragment(int)
//
//        fragment.arguments = Bundle().apply {
//            // Our object is just an integer :-P
//            putInt(ARG_OBJECT, position + 1)
//        }

        if (position == 0) {
            fragment = DepositoManage()
        }

        if (position == 1) {
            fragment = LoanManage()
        }

        if (position == 2) {
            fragment = TaskManager()
        }

        if (position == 3) {
            fragment = Sub31Loan()
        }

        if (position == 4) {
            fragment = Sub32Deposito()
        }

        return fragment
    }
}