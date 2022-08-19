package com.example.projectgroup2.ui.main.home

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projectgroup2.data.api.auth.getUser.GetUserResponse
import com.example.projectgroup2.data.api.main.buyer.product.GetProductResponse
import com.example.projectgroup2.data.api.main.buyer.product.ProductResponse
import com.example.projectgroup2.data.api.main.category.CategoryResponse
import com.example.projectgroup2.repository.AuthRepository
import com.example.projectgroup2.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repo: ProductRepository, private val repoAuth: AuthRepository): ViewModel() {
    val showProductBuyer: MutableLiveData<List<GetProductResponse>> = MutableLiveData()
    val showCategory: MutableLiveData<List<CategoryResponse>> = MutableLiveData()
    val showLoading: MutableLiveData<Boolean> = MutableLiveData()
    val showEmpty: MutableLiveData<Boolean> = MutableLiveData()
    val showShimmerProduct: MutableLiveData<Boolean> = MutableLiveData()
    val showShimmerUser: MutableLiveData<Boolean> = MutableLiveData()
    val showShimmerCategory: MutableLiveData<Boolean> = MutableLiveData()
    val showRv: MutableLiveData<Boolean> = MutableLiveData()
    val showError: MutableLiveData<String> = MutableLiveData()
    val showUser: MutableLiveData<GetUserResponse> = MutableLiveData()

    @SuppressLint("NullSafeMutableLiveData")
    fun getProductBuyer(status: String, categoryId: String, search: String){
        CoroutineScope(Dispatchers.IO).launch {
            showEmpty.postValue(false)
            showShimmerProduct.postValue(true)
            showRv.postValue(false)
            val result = repo.getProductBuyer(status, categoryId, search)
            withContext(Dispatchers.Main){
                if (result.isSuccessful){
                    //show data
                    val data = result.body()
                    showShimmerProduct.postValue(false)
                    if(data!!.isEmpty()){
                        showEmpty.postValue(true)
                    }
                    showProductBuyer.postValue(data)
                    showRv.postValue(true)
                }else{
                    //show error
                    val data = result.errorBody()
                    showError.postValue(data.toString())
                    showShimmerProduct.postValue(false)
                    showEmpty.postValue(false)
                    showRv.postValue(false)
                }
            }
        }
    }

    @SuppressLint("NullSafeMutableLiveData")
    fun getCategory(){
        CoroutineScope(Dispatchers.IO).launch {
            showShimmerCategory.postValue(true)
            val result = repo.getCategory()
            withContext(Dispatchers.Main){
                if (result.isSuccessful){
                    //show data
                    val data = result.body()
                    showShimmerCategory.postValue(false)
                    showCategory.postValue(data)
                }else{
                    //show error
                    val data = result.errorBody()
                    showError.postValue(data.toString())
                    showShimmerCategory.postValue(false)
                }
            }
        }
    }

    fun getUserData(){
        CoroutineScope(Dispatchers.IO).launch {
            showShimmerUser.postValue(true)
            val result = repoAuth.getUser(token = repoAuth.getToken()!!)
            withContext(Dispatchers.Main){
                if (result.isSuccessful){
                    //show data
                    val data = result.body()
                    showShimmerUser.postValue(false)
                    showUser.postValue(data!!)
                }else{
                    //show error
                    val data = result.errorBody()
                    showShimmerUser.postValue(false)
                    showError.postValue(data.toString())
                }
            }
        }
    }
}