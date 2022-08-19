package com.example.projectgroup2.ui.main.daftarjual.infopenawar

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projectgroup2.data.api.main.approveorder.ApproveOrderRequest
import com.example.projectgroup2.data.api.main.approveorder.ApproveOrderResponse
import com.example.projectgroup2.data.api.main.sellerorder.SellerOrderResponse
import com.example.projectgroup2.repository.AuthRepository
import com.example.projectgroup2.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class InfoPenawarViewModel @Inject constructor(private val repo: ProductRepository, private val repoAuth: AuthRepository): ViewModel() {
    val showApproveOrder: MutableLiveData<ApproveOrderResponse> = MutableLiveData()
    val showErrorDeclineOrder: MutableLiveData<String> = MutableLiveData()

    fun declineOrder(id: Int, approveOrderRequest: ApproveOrderRequest){
        CoroutineScope(Dispatchers.IO).launch {
            val result = repo.approveOrder(token = repoAuth.getToken()!!, id, approveOrderRequest)
            withContext(Dispatchers.Main){
                if (result.isSuccessful){
                    //show data
                    val data = result.body()
                    showApproveOrder.postValue(data!!)
                }else{
                    //show error
                    val data = result.errorBody()
                    showErrorDeclineOrder.postValue(data.toString())
                }
            }
        }
    }

}