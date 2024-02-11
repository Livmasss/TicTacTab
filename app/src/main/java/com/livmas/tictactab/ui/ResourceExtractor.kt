package com.livmas.tictactab.ui

import android.content.Context
import android.content.res.Resources

class ResourceExtractor(val resources: Resources, val context: Context) {

    //Returns Int id by its string name
    fun getIdByString(name: String): Int =
        resources.getIdentifier(name, "id", context.packageName)

    fun getStringByName(name: String) = resources.getString(
        resources.getIdentifier(name, "string", context.packageName)
    )
}