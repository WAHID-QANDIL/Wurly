package com.wahid.wurly.utils

import android.content.Context
import androidx.annotation.StringRes
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Small helper to read Android string resources without passing Context through view models.
 */
interface ResourceAccessor {
    fun getString(@StringRes resId: Int, vararg args: Any): String
}

@Singleton
class AndroidResourceAccessor @Inject constructor(
    @ApplicationContext private val context: Context,
) : ResourceAccessor {
    override fun getString(@StringRes resId: Int, vararg args: Any): String =
        context.getString(resId, *args)
}