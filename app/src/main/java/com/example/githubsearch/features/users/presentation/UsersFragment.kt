package com.example.githubsearch.features.users.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.githubsearch.MainActivity
import com.example.githubsearch.core.base.BaseFragment
import com.example.githubsearch.core.utils.Timer
import com.example.githubsearch.core.utils.ViewState
import com.example.githubsearch.databinding.FragmentUsersBinding
import com.example.githubsearch.databinding.RecyclerViewCellUsersBinding
import com.example.githubsearch.features.users.domain.entities.UserEntity
import com.example.githubsearch.features.users.presentation.cell.UserCell
import dagger.hilt.android.AndroidEntryPoint
import io.github.enicolas.genericadapter.AdapterHolderType
import io.github.enicolas.genericadapter.adapter.GenericRecyclerAdapter
import io.github.enicolas.genericadapter.adapter.GenericRecylerAdapterDelegate
import io.github.enicolas.genericadapter.diffable.Snapshot


@AndroidEntryPoint
class UsersFragment : BaseFragment<FragmentUsersBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentUsersBinding
        get() = FragmentUsersBinding::inflate

    private val genericRecyclerAdapter =
        GenericRecyclerAdapter(Snapshot(object : DiffUtil.ItemCallback<UserEntity>() {
            override fun areItemsTheSame(
                oldItem: UserEntity,
                newItem: UserEntity
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: UserEntity,
                newItem: UserEntity
            ): Boolean {
                return oldItem.userName == newItem.userName
            }
        }))

    private val viewModel: UsersViewModel by viewModels()
    private val searchTimer: Timer = Timer()
    private val querySearch = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            if (query?.isEmpty() == true)
                viewModel.setSearchQuery(null)
            else
                viewModel.setSearchQuery(query)
            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            if (newText?.isEmpty() == true)
                viewModel.setSearchQuery(null)
            else
                viewModel.setSearchQuery(newText)
            searchTimer.schedule(1000, task = {
                fetchData()
            })
            return false
        }
    }

    override fun setupFragment() {
        setupRecycler()
        setupSearchView()
        fetchData()
    }

    private fun setupRecycler() {
        genericRecyclerAdapter.delegate = recyclerViewDelegate
        binding.rcvUsers.apply {
            adapter = genericRecyclerAdapter
            itemAnimator = null
        }
    }

    private fun setupSearchView() {
        val searchView = (activity as MainActivity).searchView
        searchView.setOnQueryTextListener(querySearch)
        searchView.setIconifiedByDefault(false)
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
        genericRecyclerAdapter.snapshot?.snapshotList = viewState.result
    }

    /**
     * Auxiliary functions
     */
    private fun <T> getSnapshotItem(adapter: GenericRecyclerAdapter, index: Int): T? {
        return (adapter.snapshot?.snapshotList?.get(index) as? T)
    }
}