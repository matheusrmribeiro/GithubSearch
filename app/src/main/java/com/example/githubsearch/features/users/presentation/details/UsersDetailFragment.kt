package com.example.githubsearch.features.users.presentation.details

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.githubsearch.R
import com.example.githubsearch.core.base.BaseFragment
import com.example.githubsearch.core.utils.ViewState
import com.example.githubsearch.core.utils.parseHtml
import com.example.githubsearch.databinding.FragmentUsersDetailBinding
import com.example.githubsearch.databinding.RecyclerViewCellRepositoryBinding
import com.example.githubsearch.features.users.domain.entities.UserBasicEntity
import com.example.githubsearch.features.users.domain.entities.UserCompleteEntity
import com.example.githubsearch.features.users.domain.entities.UserRepositoryEntity
import com.example.githubsearch.features.users.presentation.details.cell.RepositoryCell
import dagger.hilt.android.AndroidEntryPoint
import io.github.enicolas.genericadapter.AdapterHolderType
import io.github.enicolas.genericadapter.adapter.GenericRecyclerAdapter
import io.github.enicolas.genericadapter.adapter.GenericRecylerAdapterDelegate
import io.github.enicolas.genericadapter.diffable.Snapshot
import koleton.api.hideSkeleton
import koleton.api.loadSkeleton

@AndroidEntryPoint
class UsersDetailFragment : BaseFragment<FragmentUsersDetailBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentUsersDetailBinding
        get() = FragmentUsersDetailBinding::inflate

    /**
     * Variables
     */
    private val viewModel: UsersDetailViewModel by viewModels()
    private val args: UsersDetailFragmentArgs by navArgs()
    private var loadingController: Int = 0

    /**
     * Functions
     */
    override fun setupFragment() {
        viewModel.userName = args.userName
        setupRecycler()
        fetchData()
    }

    private fun setupRecycler() {
        genericRecyclerAdapter.delegate = recyclerViewDelegate
        binding.rcvRepositories.apply {
            adapter = genericRecyclerAdapter
            itemAnimator = null
        }
    }

    private fun fetchData() {
        onRequestLoading()
        viewModel.fetchUserByUserName().observe { viewState ->
            when (viewState) {
                is ViewState.Error -> onRequestError(viewState)
                is ViewState.Loading -> onRequestLoading()
                is ViewState.Success -> onRequestUserSuccess(viewState)
            }
        }
        viewModel.fetchUserRepository().observe { viewState ->
            when (viewState) {
                is ViewState.Error -> onRequestError(viewState)
                is ViewState.Loading -> onRequestLoading()
                is ViewState.Success -> onRequestRepositorySuccess(viewState)
            }
        }
    }

    private fun onRequestError(viewState: ViewState.Error) {
        configureErrorMessage(messageResId = viewState.messageRes ?: 0, showMessage = true)
        endLoading()
    }

    private fun onRequestLoading() {
        startLoading()
    }

    private fun onRequestUserSuccess(viewState: ViewState.Success<UserCompleteEntity?>) {
        endLoading()
        setupUserInfo(viewState.result)
    }

    private fun onRequestRepositorySuccess(viewState: ViewState.Success<List<UserRepositoryEntity>?>) {
        endLoading()
        viewState.result?.let {
            setupRepositoryInfo(it)
            configureEmptyMessage(showMessage = it.isEmpty())
        }
    }

    private fun configureEmptyMessage(showMessage: Boolean) {
        if (showMessage) {
            binding.rcvRepositories.visibility = View.GONE
            binding.incError.root.visibility = View.GONE
            binding.incEmpty.root.visibility = View.VISIBLE
        } else {
            binding.rcvRepositories.visibility = View.VISIBLE
            binding.incEmpty.root.visibility = View.GONE
            binding.incError.root.visibility = View.GONE
        }
    }

    private fun configureErrorMessage(messageResId: Int, showMessage: Boolean) {
        if (showMessage) {
            binding.incError.txtErrorMessage.setText(messageResId)
            binding.incError.root.visibility = View.VISIBLE
        } else {
            binding.incError.root.visibility = View.GONE
            binding.incError.txtErrorMessage.text = ""
        }
    }

    private fun setupUserInfo(data: UserCompleteEntity?) {
        data?.let {
            binding.txtName.text = data.name
            data.bio?.let {
                binding.txtUserBio.text = it
                binding.txtUserBio.visibility = View.VISIBLE
            }
            binding.imgPicture.load(data.picture) {
                error(R.drawable.ic_user)
            }
            binding.txtUserName.text =
                resources.getString(R.string.users_detail_user_name, data.userName).parseHtml()
            binding.txtFollowers.text =
                resources.getString(R.string.users_detail_follower, data.followers.toString()).parseHtml()
            binding.txtFollowing.text =
                resources.getString(R.string.users_detail_following, data.following.toString()).parseHtml()
        }
    }

    private fun setupRepositoryInfo(data: List<UserRepositoryEntity>) {
        genericRecyclerAdapter.snapshot?.snapshotList = data

    }

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

    private val recyclerViewDelegate = object : GenericRecylerAdapterDelegate {
        override fun numberOfRows(adapter: GenericRecyclerAdapter): Int {
            return adapter.snapshot?.snapshotList?.size ?: 0
        }

        override fun cellForPosition(
            adapter: GenericRecyclerAdapter,
            cell: RecyclerView.ViewHolder,
            position: Int
        ) {
            getSnapshotItem<UserRepositoryEntity>(adapter, position)?.let { item ->
                (cell as? RepositoryCell)?.set(repository = item)
            }
        }

        override fun registerCellAtPosition(
            adapter: GenericRecyclerAdapter,
            position: Int
        ): AdapterHolderType {
            return AdapterHolderType(
                RecyclerViewCellRepositoryBinding::class.java,
                RepositoryCell::class.java,
                0
            )
        }

        override fun didSelectItemAtIndex(adapter: GenericRecyclerAdapter, index: Int) {
            getSnapshotItem<UserRepositoryEntity>(adapter, index)?.let { item ->
                try {
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(item.url))
                    startActivity(browserIntent)
                } catch (_: Exception) {
                    Toast.makeText(context, R.string.users_detail_repositories_open_fail, Toast.LENGTH_LONG)
                }
            }
        }
    }

    /**
     * Auxiliary functions
     */
    private fun <T> getSnapshotItem(adapter: GenericRecyclerAdapter, index: Int): T? {
        return (adapter.snapshot?.snapshotList?.get(index) as? T)
    }

    private fun startLoading() {
        if (loadingController == 0) {
            loadingController++
            binding.ctlUserInfo.loadSkeleton {
                shimmer(true)
            }
            binding.imgPicture.loadSkeleton()
            binding.txtName.loadSkeleton(length = 25)
            binding.txtUserName.loadSkeleton()
            binding.txtUserBio.loadSkeleton()
            binding.txtFollowers.loadSkeleton()
            binding.txtFollowing.loadSkeleton()
            binding.rcvRepositories.loadSkeleton(R.layout.recycler_view_cell_repository) {
                itemCount(4)
            }
        }
    }

    private fun endLoading() {
        loadingController--
        if (loadingController == 0) {
            binding.imgPicture.hideSkeleton()
            binding.txtName.hideSkeleton()
            binding.txtUserName.hideSkeleton()
            binding.txtUserBio.hideSkeleton()
            binding.txtFollowers.hideSkeleton()
            binding.txtFollowing.hideSkeleton()
            binding.ctlUserInfo.hideSkeleton()
            binding.rcvRepositories.hideSkeleton()
        }
    }

}