package com.example.githubsearch.features.users.presentation.details.cell

import com.example.githubsearch.databinding.RecyclerViewCellRepositoryBinding
import com.example.githubsearch.features.users.domain.entities.UserRepositoryEntity
import io.github.enicolas.genericadapter.adapter.BaseCell

class RepositoryCell(var binding: RecyclerViewCellRepositoryBinding) : BaseCell(binding.root) {

    fun set(repository: UserRepositoryEntity) {
        binding.txtRepositoryName.text = repository.name
        binding.txtRepositoryDescription.text = repository.description
    }

}