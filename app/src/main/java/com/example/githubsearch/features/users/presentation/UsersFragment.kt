package com.example.githubsearch.features.users.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.githubsearch.core.base.BaseFragment
import com.example.githubsearch.core.utils.ViewState
import com.example.githubsearch.databinding.FragmentUsersBinding
import com.example.githubsearch.databinding.RecyclerViewCellUsersBinding
import com.example.githubsearch.features.users.domain.entities.UserEntity
import com.example.githubsearch.features.users.presentation.cell.UserCell
import dagger.hilt.android.AndroidEntryPoint
import io.github.enicolas.genericadapter.AdapterHolderType
import io.github.enicolas.genericadapter.adapter.GenericRecyclerAdapter
import io.github.enicolas.genericadapter.adapter.GenericRecylerAdapterDelegate

@AndroidEntryPoint
class UsersFragment : BaseFragment<FragmentUsersBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentUsersBinding
        get() = FragmentUsersBinding::inflate

    private val genericRecyclerAdapter = GenericRecyclerAdapter()

    private val viewModel: UsersViewModel by viewModels()

    override fun setupFragment() {
        setupRecycler()
        fetchData()
    }

    private fun setupRecycler() {
        genericRecyclerAdapter.delegate = recyclerViewDelegate
        binding.rcvUsers.apply {
            adapter = genericRecyclerAdapter
            itemAnimator = null
        }
    }

    private val recyclerViewDelegate = object : GenericRecylerAdapterDelegate {
        override fun numberOfRows(adapter: GenericRecyclerAdapter): Int {
            return adapter.snapshot?.snapshotList?.size ?: 0
        }

        override fun cellForPosition(
            adapter: GenericRecyclerAdapter,
            cell: RecyclerView.ViewHolder,
            position: Int
        ) {
            getSnapshotItem<UserEntity>(adapter, position)?.let { item ->
                (cell as? UserCell)?.set(user = item)
            }
        }

        override fun registerCellAtPosition(
            adapter: GenericRecyclerAdapter,
            position: Int
        ): AdapterHolderType {
            return AdapterHolderType(
                RecyclerViewCellUsersBinding::class.java,
                UserCell::class.java,
                0
            )
        }

        override fun didSelectItemAtIndex(adapter: GenericRecyclerAdapter, index: Int) {
            getSnapshotItem<UserEntity>(adapter, index)?.let { item ->

            }
        }
    }

    private fun fetchData() {
        viewModel.fetchUsers().observe { viewState ->
            when (viewState) {
                is ViewState.Error -> onRequestError(viewState)
                is ViewState.Loading -> onRequestLoading()
                is ViewState.Success -> onRequestSuccess(viewState)
            }
        }
    }

    private fun onRequestError(viewState: ViewState.Error) {
        Toast.makeText(context, viewState.messageRes ?: 0, Toast.LENGTH_LONG).show()

    }

    private fun onRequestLoading() {

    }

    private fun onRequestSuccess(viewState: ViewState.Success<List<UserEntity>>) {

    }

    /**
     * Auxiliary functions
     */
    private fun <T> getSnapshotItem(adapter: GenericRecyclerAdapter, index: Int): T? {
        return (adapter.snapshot?.snapshotList?.get(index) as? T)
    }
}