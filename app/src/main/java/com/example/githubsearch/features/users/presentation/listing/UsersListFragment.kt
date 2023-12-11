package com.example.githubsearch.features.users.presentation.listing

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.githubsearch.MainActivity
import com.example.githubsearch.core.base.BaseFragment
import com.example.githubsearch.core.utils.Timer
import com.example.githubsearch.core.utils.ViewState
import com.example.githubsearch.databinding.FragmentUsersListBinding
import com.example.githubsearch.databinding.RecyclerViewCellUsersBinding
import com.example.githubsearch.features.users.domain.entities.UserEntity
import com.example.githubsearch.features.users.presentation.listing.cell.UserCell
import dagger.hilt.android.AndroidEntryPoint
import io.github.enicolas.genericadapter.AdapterHolderType
import io.github.enicolas.genericadapter.adapter.GenericRecyclerAdapter
import io.github.enicolas.genericadapter.adapter.GenericRecylerAdapterDelegate
import io.github.enicolas.genericadapter.diffable.Snapshot


@AndroidEntryPoint
class UsersListFragment : BaseFragment<FragmentUsersListBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentUsersListBinding
        get() = FragmentUsersListBinding::inflate

    /**
     * Variables
     */
    private val viewModel: UsersListViewModel by viewModels()
    private val searchTimer: Timer = Timer()

    /**
     * Adapters
     */
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

    /**
     * Functions
     */
    override fun setupFragment() {
        setupRecycler()
        setupSearchView()
        setupButtons()
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

    private fun setupButtons() {
        binding.incError.btnSearchAgain.setOnClickListener {
            fetchData()
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
        binding.pgrLoading.hide()
        configureErrorMessage(messageResId = viewState.messageRes ?: 0, showMessage = true)
    }

    private fun onRequestLoading() {
        binding.pgrLoading.show()
    }

    private fun onRequestSuccess(viewState: ViewState.Success<List<UserEntity>>) {
        binding.pgrLoading.hide()
        genericRecyclerAdapter.snapshot?.snapshotList = viewState.result
        configureEmptyMessage(showMessage = viewState.result.isEmpty())
    }

    private fun configureEmptyMessage(showMessage: Boolean) {
        if (showMessage) {
            binding.rcvUsers.visibility = View.GONE
            binding.incError.root.visibility = View.GONE
            binding.incEmpty.root.visibility = View.VISIBLE
        } else {
            binding.rcvUsers.visibility = View.VISIBLE
            binding.incEmpty.root.visibility = View.GONE
        }
    }

    private fun configureErrorMessage(messageResId: Int, showMessage: Boolean) {
        if (showMessage) {
            binding.rcvUsers.visibility = View.GONE
            binding.incEmpty.root.visibility = View.GONE
            binding.incError.txtErrorMessage.setText(messageResId)
            binding.incError.root.visibility = View.VISIBLE
        } else {
            binding.incError.root.visibility = View.GONE
            binding.incError.txtErrorMessage.text = ""
            binding.rcvUsers.visibility = View.VISIBLE
        }
    }

    /**
     * Auxiliary functions
     */
    private fun <T> getSnapshotItem(adapter: GenericRecyclerAdapter, index: Int): T? {
        return (adapter.snapshot?.snapshotList?.get(index) as? T)
    }
}