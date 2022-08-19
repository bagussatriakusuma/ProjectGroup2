package com.example.projectgroup2.ui.main.jual.preview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projectgroup2.data.api.auth.getUser.GetUserResponse
import com.example.projectgroup2.repository.AuthRepository
import com.example.projectgroup2.repository.ProductRepository
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
class PreviewViewModel @Inject constructor(private val repo: ProductRepository, private val repoAuth: AuthRepository): ViewModel() {
    val showSuccess: MutableLiveData<Boolean> = MutableLiveData()
    val showError: MutableLiveData<String> = MutableLiveData()
    val showUser: MutableLiveData<GetUserResponse> = MutableLiveData()
    val showLoading: MutableLiveData<Boolean> = MutableLiveData()

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
                    showSuccess.postValue(true)
                    showLoading.postValue(false)
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
}