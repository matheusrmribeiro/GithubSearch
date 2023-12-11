package com.example.githubsearch.features.users.presentation.listing.cell

import android.view.View
import coil.load
import com.example.githubsearch.R
import com.example.githubsearch.databinding.RecyclerViewCellUsersBinding
import com.example.githubsearch.features.users.domain.entities.UserBasicEntity
import io.github.enicolas.genericadapter.adapter.BaseCell

class UserCell(var binding: RecyclerViewCellUsersBinding) : BaseCell(binding.root) {

    fun set(user: UserBasicEntity) {
        binding.txtUserName.text = user.name ?: user.userName
        user.bio?.let {
            binding.txtUserBio.text = it
            binding.txtUserBio.visibility = View.VISIBLE
        }
        binding.imgPicture.load(user.picture) {
            error(R.drawable.ic_user)
        }
    }
}