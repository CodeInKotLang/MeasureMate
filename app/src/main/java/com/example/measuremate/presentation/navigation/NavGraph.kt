package com.example.measuremate.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.measuremate.domain.repository.AuthRepository
import com.example.measuremate.presentation.add_item.AddItemScreen
import com.example.measuremate.presentation.dashboard.DashboardScreen
import com.example.measuremate.presentation.details.DetailsScreen
import com.example.measuremate.presentation.signin.SignInScreen
import com.example.measuremate.presentation.signin.SignInViewModel
import kotlin.math.sign

@Composable
fun NavGraph(
    navController: NavHostController,
    windowSize: WindowWidthSizeClass,
    paddingValues: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = Routes.DashboardScreen
    ) {

        composable<Routes.SignInScreen> {
            val signInViewModel: SignInViewModel = hiltViewModel()
            val state by signInViewModel.state.collectAsStateWithLifecycle()
            SignInScreen(
                windowSize = windowSize,
                paddingValues = paddingValues,
                state = state,
                onEvent = signInViewModel::onEvent
            )
        }

        composable<Routes.DashboardScreen> {
            DashboardScreen(
                paddingValues = paddingValues,
                onFabClicked = { navController.navigate(Routes.AddItemScreen) },
                onItemCardClicked = { bodyPartId ->
                    navController.navigate(Routes.DetailsScreen(bodyPartId))
                }
            )
        }

        composable<Routes.AddItemScreen>(
            enterTransition = {
                slideIntoContainer(
                    animationSpec = tween(durationMillis = 500),
                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    animationSpec = tween(durationMillis = 500),
                    towards = AnimatedContentTransitionScope.SlideDirection.End
                )
            }
        ) {
            AddItemScreen(
                paddingValues = paddingValues,
                onBackIconClick = { navController.navigateUp() }
            )
        }

        composable<Routes.DetailsScreen>(
            enterTransition = {
                slideIntoContainer(
                    animationSpec = tween(durationMillis = 500),
                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    animationSpec = tween(durationMillis = 500),
                    towards = AnimatedContentTransitionScope.SlideDirection.End
                )
            }
        ) { navBackStackEntry ->
            val bodyPartId = navBackStackEntry.toRoute<Routes.DetailsScreen>().bodyPartId
            DetailsScreen(
                paddingValues = paddingValues,
                bodyPartId = bodyPartId,
                windowSize = windowSize,
                onBackIconClick = { navController.navigateUp() }
            )
        }

    }
}