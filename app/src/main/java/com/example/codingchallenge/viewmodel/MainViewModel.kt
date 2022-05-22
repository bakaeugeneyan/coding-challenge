package com.example.codingchallenge.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.*
import com.example.codingchallenge.data.Repository
import com.example.codingchallenge.data.database.UserEntity
import com.example.codingchallenge.models.UserList
import com.example.codingchallenge.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
): AndroidViewModel(application) {

    /** Room Database **/
    val readUserList: LiveData<List<UserEntity>> = repository.local.readDatabase().asLiveData()

    private fun insertItunes(userEntity: UserEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertUser(userEntity)
        }

    /** Retrofit **/
    var userResponse: MutableLiveData<NetworkResult<UserList>> = MutableLiveData()

    fun getItunesList(queries: Map<String, String>) = viewModelScope.launch {
        getUserSafeCall(queries)
    }

    private suspend fun getUserSafeCall(queries: Map<String, String>) {
        userResponse.value = NetworkResult.Loading()

        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getUserList(queries)
                userResponse.value = handleItunesResponse(response)

                val itunes = userResponse.value!!.data
                if (itunes != null) {
                    offlineCacheItunes(itunes)
                }
            }
            catch (e: Exception) {
                userResponse.value = NetworkResult.Error("Itunes Not Found")
            }
        } else {
            userResponse.value = NetworkResult.Error("No Internet Connection.")
        }
    }

    private fun offlineCacheItunes(itunesList: UserList) {
        val itunesEntity = UserEntity(itunesList)
        insertItunes(itunesEntity)
    }


    private fun handleItunesResponse(response: Response<UserList>): NetworkResult<UserList>? {
        return when {
            response.message().toString().contains("timeout") -> {
                NetworkResult.Error("Timeout")
            }
            response.body()!!.data.isNullOrEmpty() -> {
                NetworkResult.Error("Itunes not found.")
            }
            response.isSuccessful -> {
                val itunes = response.body()
                NetworkResult.Success(itunes!!)
            }
            else -> {
                NetworkResult.Error(response.message())
            }
        }
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}