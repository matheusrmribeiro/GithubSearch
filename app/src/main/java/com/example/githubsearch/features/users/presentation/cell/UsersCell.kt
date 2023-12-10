package com.example.githubsearch.features.users.presentation.cell

import coil.load
import com.example.githubsearch.databinding.RecyclerViewCellUsersBinding
import com.example.githubsearch.features.users.domain.entities.UserEntity
import io.github.enicolas.genericadapter.adapter.BaseCell

class UserCell(var binding: RecyclerViewCellUsersBinding) : BaseCell(binding.root) {

    fun set(user: UserEntity) {
        binding.txtUserName.text = user.name ?: user.userName
        binding.txtUserBio.text = user.bio ?: ""
        binding.imgPicture.load(user.picture)
    }
}