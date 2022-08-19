package com.example.projectgroup2.ui.main.daftarjual

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projectgroup2.data.api.auth.getUser.GetUserResponse
import com.example.projectgroup2.data.api.main.sellerorder.SellerOrderResponse
import com.example.projectgroup2.data.api.main.sellerproduct.SellerProductResponse
import com.example.projectgroup2.repository.AuthRepository
import com.example.projectgroup2.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DaftarJualViewModel @Inject constructor(private val repoAuth: AuthRepository, private val repo: ProductRepository): ViewModel() {
    val showError: MutableLiveData<String> = MutableLiveData()
    val showUser: MutableLiveData<GetUserResponse> = MutableLiveData()
    val showProductSeller: MutableLiveData<List<SellerProductResponse>> = MutableLiveData()
    val showOrderSeller: MutableLiveData<List<SellerOrderResponse>> = MutableLiveData()
    val showEmpty: MutableLiveData<Boolean> = MutableLiveData()
    val showEmpty2: MutableLiveData<Boolean> = MutableLiveData()

    fun getUserData(){
        CoroutineScope(Dispatchers.IO).launch {
            val result = repoAuth.getUser(token = repoAuth.getToken()!!)
            withContext(Dispatchers.Main){
                if (result.isSuccessful){
                    //show data
                    val data = result.body()
                    showUser.postValue(data!!)
                }else{
                    //show error
                    val data = result.errorBody()
                    showError.postValue(data.toString())
                }
            }
        }
    }

    fun getProductSeller(){
        CoroutineScope(Dispatchers.IO).launch {
            val result = repo.getSellerProduct(token = repoAuth.getToken()!!)
            withContext(Dispatchers.Main){
                if (result.isSuccessful){
                    //show data
                    val data = result.body()
                    showProductSeller.postValue(data!!)
                }else{
                    //show error
                    val data = result.errorBody()
                    showError.postValue(data.toString())
                }
            }
        }
    }

    fun getOrderSeller(status: String){
        CoroutineScope(Dispatchers.IO).launch {
            val result = repo.getSellerOrder(token = repoAuth.getToken()!!, status)
            withContext(Dispatchers.Main){
                if (result.isSuccessful){
                    //show data
                    val data = result.body()
                    showOrderSeller.postValue(data!!)
                }else{
                    //show error
                    val data = result.errorBody()
                    showError.postValue(data.toString())
                }
            }
        }
    }
}