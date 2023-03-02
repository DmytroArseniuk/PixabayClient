package com.arsars.pixabayclient

import androidx.navigation.NavHostController
import com.arsars.pixabayclient.Destinations.PHOTO_DETAILS_ROUTE
import com.arsars.pixabayclient.DestinationsArgs.PHOTO_ID_ARG
import com.arsars.pixabayclient.Screens.PHOTO_DETAILS
import com.arsars.pixabayclient.Screens.PHOTO_SEARCH

private object Screens {
    const val PHOTO_SEARCH = "photoSearch"
    const val PHOTO_DETAILS = "photoDetails"
}

object Destinations {
    const val PHOTO_SEARCH_ROUTE = PHOTO_SEARCH
    const val PHOTO_DETAILS_ROUTE = "$PHOTO_DETAILS/{$PHOTO_ID_ARG}"
}

object DestinationsArgs {
    const val PHOTO_ID_ARG = "photoId"
}

class NavigationActions(private val navController: NavHostController) {
    fun navigateToDetails(photoId: Long) {
        navController.navigate("$PHOTO_DETAILS/$photoId")
    }
}