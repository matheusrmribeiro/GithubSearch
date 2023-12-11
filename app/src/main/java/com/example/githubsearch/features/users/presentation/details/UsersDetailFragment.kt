package com.example.githubsearch.features.users.presentation.details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.githubsearch.R
import com.example.githubsearch.core.base.BaseFragment
import com.example.githubsearch.core.utils.ViewState
import com.example.githubsearch.databinding.FragmentUsersDetailBinding
import com.example.githubsearch.databinding.RecyclerViewCellUsersBinding
import com.example.githubsearch.features.users.domain.entities.UserBasicEntity
import com.example.githubsearch.features.users.domain.entities.UserCompleteEntity
import com.example.githubsearch.features.users.presentation.listing.cell.UserCell
import dagger.hilt.android.AndroidEntryPoint
import io.github.enicolas.genericadapter.AdapterHolderType
import io.github.enicolas.genericadapter.adapter.GenericRecyclerAdapter
import io.github.enicolas.genericadapter.adapter.GenericRecylerAdapterDelegate
import io.github.enicolas.genericadapter.diffable.Snapshot

@AndroidEntryPoint
class UsersDetailFragment : BaseFragment<FragmentUsersDetailBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentUsersDetailBinding
        get() = FragmentUsersDetailBinding::inflate

    /**
     * Variables
     */
    private val viewModel: UsersDetailViewModel by viewModels()
    private val args: UsersDetailFragmentArgs by navArgs()

    /**
     * Adapters
     */
    private val genericRecyclerAdapter =
        GenericRecyclerAdapter(Snapshot(object : DiffUtil.ItemCallback<UserBasicEntity>() {
            override fun areItemsTheSame(
                oldItem: UserBasicEntity,
                newItem: UserBasicEntity
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: UserBasicEntity,
                newItem: UserBasicEntity
            ): Boolean {
                return oldItem.userName == newItem.userName
            }
        }))

    /**
     * Functions
     */
    override fun setupFragment() {
        viewModel.userName = args.userName
        setupButtons()
        fetchData()
    }


    private fun setupButtons() {

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
            getSnapshotItem<UserBasicEntity>(adapter, position)?.let { item ->
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
            getSnapshotItem<UserBasicEntity>(adapter, index)?.let { item ->

            }
        }
    }

    private fun fetchData() {
        viewModel.fetchUsersByUserName().observe { viewState ->
            when (viewState) {
                is ViewState.Error -> onRequestError(viewState)
                is ViewState.Loading -> onRequestLoading()
                is ViewState.Success -> onRequestSuccess(viewState)
            }
        }
    }

    private fun onRequestError(viewState: ViewState.Error) {
        configureErrorMessage(messageResId = viewState.messageRes ?: 0, showMessage = true)
    }

    private fun onRequestLoading() {
    }

    private fun onRequestSuccess(viewState: ViewState.Success<UserCompleteEntity?>) {
        setupData(viewState.result)
    }

    private fun configureEmptyMessage(showMessage: Boolean) {

    }

    private fun configureErrorMessage(messageResId: Int, showMessage: Boolean) {

    }

    private fun setupData(data: UserCompleteEntity?) {
        data?.let {
            binding.txtName.text = data.name
            binding.txtUserBio.text = data.bio
            binding.imgPicture.load(data.picture) {
                error(R.drawable.ic_user)
            }
            binding.txtUserName.text =
                resources.getString(R.string.users_detail_user_name, data.userName)
            binding.txtFollowers.text =
                resources.getString(R.string.users_detail_follower, data.followers.toString())
            binding.txtFollowing.text =
                resources.getString(R.string.users_detail_following, data.following.toString())
        }
    }
    /**
     * Auxiliary functions
     */
    private fun <T> getSnapshotItem(adapter: GenericRecyclerAdapter, index: Int): T? {
        return (adapter.snapshot?.snapshotList?.get(index) as? T)
    }
}