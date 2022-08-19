package com.example.projectgroup2.ui.main.notif

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projectgroup2.data.api.main.notification.NotifResponse
import com.example.projectgroup2.repository.AuthRepository
import com.example.projectgroup2.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class NotifViewModel @Inject constructor(private val repo: ProductRepository, private val repoAuth: AuthRepository): ViewModel() {
    val showNotif: MutableLiveData<List<NotifResponse>> = MutableLiveData()
    val showError: MutableLiveData<String> = MutableLiveData()

    fun getNotification(){
        CoroutineScope(Dispatchers.IO).launch {
            val result = repo.getNotification(token = repoAuth.getToken()!!)
            withContext(Dispatchers.Main){
                if (result.isSuccessful){
                    //show data
                    val data = result.body()
                    showNotif.postValue(data!!)
                }else{
                    //show error
                    val data = result.errorBody()
                    showError.postValue(data.toString())
                }
            }
        }
    }
}