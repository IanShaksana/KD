package com.segen.kd.tabbeddemo.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.segen.kd.fragment.manager.submission.deposito.core.DepositoManage
import com.segen.kd.fragment.manager.submission.loan.core.LoanManage
import com.segen.kd.fragment.manager.task.list.TaskManager
import com.segen.kd.fragment.marketing.submission.deposito.core.Sub32Deposito
import com.segen.kd.fragment.marketing.submission.loan.core.Sub31Loan


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