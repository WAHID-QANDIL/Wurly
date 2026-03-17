package com.wahid.wurly.domain.exception

import android.util.Log
import retrofit2.HttpException
import java.io.IOException
import java.io.InterruptedIOException
import java.net.HttpURLConnection
import java.net.UnknownHostException

sealed class CustomDomainModelException(override val message: String?) : Exception(message) {
    class NoInternetConnectionRemoteException : CustomDomainModelException("No internet connection")
    class TimeOutExceptionRemoteException : CustomDomainModelException("Connection time out ")
    class AccessDeniedRemoteException : CustomDomainModelException("You are not authorized to access this data")
    class ServiceUnavailableRemoteException : CustomDomainModelException("ServiceUnavailableRemoteException")
    class ServiceNotFoundRemoteException : CustomDomainModelException("ServiceNotFoundRemoteException")
    class UnknownHostException: CustomDomainModelException("You have no internet access, please connect to internet and try again.")
    class UnknownRemoteException : CustomDomainModelException("UnknownRemoteException")
}
fun Throwable.toCustomRemoteExceptionDomainModel(): CustomDomainModelException {
    Log.d("Exception", this.toString())
    return when(this){
        is InterruptedIOException -> CustomDomainModelException.TimeOutExceptionRemoteException()
        is UnknownHostException -> CustomDomainModelException.UnknownHostException()
        is IOException -> CustomDomainModelException.NoInternetConnectionRemoteException()
        is HttpException -> {
            when(this.code()){
                HttpURLConnection.HTTP_NOT_FOUND -> CustomDomainModelException.ServiceNotFoundRemoteException()
                HttpURLConnection.HTTP_FORBIDDEN -> CustomDomainModelException.AccessDeniedRemoteException()
                HttpURLConnection.HTTP_UNAVAILABLE -> CustomDomainModelException.ServiceUnavailableRemoteException()
                else -> CustomDomainModelException.UnknownRemoteException()
            }
        }
        else -> CustomDomainModelException.UnknownRemoteException()
    }
}