package com.example.githubsearch.features.users

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.githubsearch.core.base.BaseFragment
import com.example.githubsearch.databinding.FragmentUsersBinding

class UsersFragment : BaseFragment<FragmentUsersBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentUsersBinding
        get() = FragmentUsersBinding::inflate


    override fun setupFragment() {

    }

}