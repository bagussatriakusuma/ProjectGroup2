package com.example.projectgroup2.ui.main.jual

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectgroup2.data.api.auth.getUser.GetUserResponse
import com.example.projectgroup2.data.api.main.category.CategoryResponse
import com.example.projectgroup2.data.api.main.uploadproduct.UploadProductResponse
import com.example.projectgroup2.repository.AuthRepository
import com.example.projectgroup2.repository.ProductRepository
import com.example.projectgroup2.utils.Resource
import com.example.projectgroup2.utils.reduceFileImage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class JualViewModel @Inject constructor(private val repo: ProductRepository, private val repoAuth: AuthRepository): ViewModel() {
    val showCategory: MutableLiveData<List<CategoryResponse>> = MutableLiveData()
    val showError: MutableLiveData<String> = MutableLiveData()
    private var addCategory = MutableLiveData<List<String>>()
    val categoryList : LiveData<List<String>> get() = addCategory
    private var showUploadProduct: MutableLiveData<List<UploadProductResponse>> = MutableLiveData()
    val showSuccess: MutableLiveData<Boolean> = MutableLiveData()
    val showErrorPost: MutableLiveData<String> = MutableLiveData()
    val showBottomSheet: MutableLiveData<Boolean> = MutableLiveData()
    val showUser: MutableLiveData<GetUserResponse> = MutableLiveData()
    val showLoading: MutableLiveData<Boolean> = MutableLiveData()

    fun getToken(){
        viewModelScope.launch {
            repoAuth.getToken()
        }
    }

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

    fun getCategory(){
        CoroutineScope(Dispatchers.IO).launch {
            val result = repo.getCategory()
            withContext(Dispatchers.Main){
                if (result.isSuccessful){
                    //show data
                    val data = result.body()
                    showCategory.postValue(data!!)
                }else{
                    //show error
                    val data = result.errorBody()
                    showError.postValue(data.toString())
                }
            }
        }
    }

    fun addCategory(category: List<String>){
        addCategory.postValue(category)
    }

    fun uploadProduct(
        namaProduk: String,
        deskripsiProduk: String,
        hargaProduk: String,
        kategoriProduk: List<Int>,
        alamatPenjual: String,
        image: File
    ){
        val requestFile = reduceFileImage(image).asRequestBody("image/jpg".toMediaTypeOrNull())
        val gambarProduk = MultipartBody.Part.createFormData("image", image.name, requestFile)
        val namaRequestBody = namaProduk.toRequestBody("text/plain".toMediaType())
        val deskripsiRequestBody = deskripsiProduk.toRequestBody("text/plain".toMediaType())
        val hargaRequestBody = hargaProduk.toRequestBody("text/plain".toMediaType())
        val alamatRequestBody = alamatPenjual.toRequestBody("text/plain".toMediaType())

        CoroutineScope(Dispatchers.IO).launch {
            showLoading.postValue(true)
            val result = repo.uploadProductSeller(
                token = repoAuth.getToken()!!,
                gambarProduk,
                namaRequestBody,
                deskripsiRequestBody,
                hargaRequestBody,
                kategoriProduk,
                alamatRequestBody
            )
            withContext(Dispatchers.Main){
                if (result.isSuccessful){
                    //show data
                    showLoading.postValue(false)
                    showSuccess.postValue(true)
//                    val data = result.body()
//                    showUploadProduct.postValue(listOf(data!!))
                }else{
                    //show error
//                    showErrorPost.postValue(true)
                    showLoading.postValue(false)
                    val data = result.errorBody()
                    showError.postValue(data.toString())
                }
            }
        }
    }
}