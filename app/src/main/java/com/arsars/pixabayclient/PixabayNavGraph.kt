package com.arsars.pixabayclient

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.arsars.pixabayclient.Destinations.PHOTO_DETAILS_ROUTE
import com.arsars.pixabayclient.Destinations.PHOTO_SEARCH_ROUTE
import com.arsars.pixabayclient.DestinationsArgs.PHOTO_ID_ARG
import com.arsars.pixabayclient.ui.screens.photo.details.PhotosSearchScreen
import com.arsars.pixabayclient.ui.screens.photo.search.PhotoDetailsScreen

@Composable
fun PixabayNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = PHOTO_SEARCH_ROUTE,
    navActions: NavigationActions = remember(navController) {
        NavigationActions(navController)
    }
) {

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        composable(PHOTO_SEARCH_ROUTE) {
            PhotosSearchScreen(
                openDetails = { navActions.navigateToDetails(it.id) }
            )
        }
        composable(
            PHOTO_DETAILS_ROUTE,
            arguments = listOf(
                navArgument(PHOTO_ID_ARG) { type = NavType.LongType },
            )
        ) {
            PhotoDetailsScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }

}