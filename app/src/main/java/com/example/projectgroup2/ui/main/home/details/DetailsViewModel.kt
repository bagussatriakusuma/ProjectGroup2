package com.example.projectgroup2.ui.main.home.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectgroup2.data.api.main.buyerorder.BuyerOrderRequest
import com.example.projectgroup2.data.api.main.buyerorder.BuyerOrderResponse
import com.example.projectgroup2.data.api.main.buyerorder.GetBuyerOrderResponse
import com.example.projectgroup2.data.api.main.productdetails.GetProductByIdResponse
import com.example.projectgroup2.repository.AuthRepository
import com.example.projectgroup2.repository.ProductRepository
import com.example.projectgroup2.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(private val repo: ProductRepository, private val repoAuth: AuthRepository): ViewModel() {
    val showProductDetails: MutableLiveData<GetProductByIdResponse> = MutableLiveData()
    val showBuyerOrder: MutableLiveData<BuyerOrderResponse> = MutableLiveData()
    val showGetBuyerOrder: MutableLiveData<List<GetBuyerOrderResponse>> = MutableLiveData()
    val showError: MutableLiveData<String> = MutableLiveData()
    val showToken: MutableLiveData<Boolean> = MutableLiveData()
    val showShimerProduct: MutableLiveData<Boolean> = MutableLiveData()

    fun getToken(){
        viewModelScope.launch {
            repoAuth.getToken()
            withContext(Dispatchers.Main){
                showToken.postValue(true)
            }
        }
    }

    fun getProductById(id: Int){
        showShimerProduct.postValue(true)
        CoroutineScope(Dispatchers.IO).launch {
            val result = repo.getProductById(id)
            withContext(Dispatchers.Main){
                if (result.isSuccessful){
                    //show data
                    val data = result.body()
                    showShimerProduct.postValue(false)
                    showProductDetails.postValue(data!!)
                }else{
                    //show error
                    val data = result.errorBody()
                    showShimerProduct.postValue(false)
                    showError.postValue(data.toString())
                }
            }
        }
    }

    fun getBuyerOrder(){
        CoroutineScope(Dispatchers.IO).launch {
            val result = repo.getBuyerOrder(token = repoAuth.getToken()!!)
            withContext(Dispatchers.Main){
                if (result.isSuccessful){
                    //show data
                    val data = result.body()
                    showGetBuyerOrder.postValue(data!!)
                }else{
                    //show error
                    val data = result.errorBody()
                    showError.postValue(data.toString())
                }
            }
        }
    }

    fun buyerOrder(requestBuyerOrderRequest: BuyerOrderRequest){
        CoroutineScope(Dispatchers.IO).launch {
            val result = repo.buyerOrder(token = repoAuth.getToken()!!, requestBuyerOrderRequest )
            withContext(Dispatchers.Main){
                if (result.isSuccessful){
                    //show data
                    val data = result.body()
                    showBuyerOrder.postValue(data!!)
                }else{
                    //show error
                    val data = result.errorBody()
                    showError.postValue(data.toString())
                }
            }
        }
    }
}