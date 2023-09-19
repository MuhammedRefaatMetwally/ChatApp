package com.example.chatapp.ui.add_room

import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.chatapp.R
import com.example.chatapp.databinding.ActivityAddRoomBinding
import com.example.chatapp.ui.showLoadingProgressDialog
import com.example.chatapp.ui.showMessage

class AddRoomActivity : AppCompatActivity() {
    lateinit var viewBinding: ActivityAddRoomBinding
    private val viewModel: AddRoomViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_room)
        initViews()
        subscribeToLiveData()
    }

    var loadingDialog: ProgressDialog? = null
    private fun subscribeToLiveData() {
        viewModel.messageLiveData.observe(this) {
            showMessage(
                message = it.message ?: "Something Went Wrong",
                posActionName = it.posActionName,
                posAction = it.onPosActionClick,
                negActionName = it.negActionName,
                negAction = it.onNegActionClick
            )
        }

        viewModel.loadingLiveData.observe(this) {
            if (it == null) {
                loadingDialog?.dismiss()
                loadingDialog = null
                return@observe
            }
            loadingDialog = showLoadingProgressDialog(
                it.message ?: "Something Went Wrong",
                isCancelable = it.isCancelable
            )
            loadingDialog?.show()
        }

        viewModel.events.observe(this, ::handleEvents)
    }

    private fun handleEvents(addRoomViewEvent: AddRoomViewEvent?) {
        when (addRoomViewEvent) {
            AddRoomViewEvent.NavigateToHomeAndFinish -> {
                finish()
            }

            else -> {

            }
        }
    }


    private lateinit var categoriesAdapter: RoomCategoriesAdapter
    private fun initViews() {
        categoriesAdapter = RoomCategoriesAdapter(viewModel.categories)
        viewBinding.vm = viewModel
        viewBinding.lifecycleOwner = this
        viewBinding.content.categoriesSpinner.adapter = categoriesAdapter
        viewBinding.content.categoriesSpinner.onItemSelectedListener =
            object : OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    viewModel.onCategorySelected(position)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

            }
    }
}