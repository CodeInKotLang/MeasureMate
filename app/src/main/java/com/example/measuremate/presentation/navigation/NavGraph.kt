package com.example.measuremate.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.SnackbarHostState
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
import com.example.measuremate.presentation.dashboard.DashboardState
import com.example.measuremate.presentation.dashboard.DashboardViewModel
import com.example.measuremate.presentation.details.DetailsScreen
import com.example.measuremate.presentation.signin.SignInScreen
import com.example.measuremate.presentation.signin.SignInViewModel
import kotlin.math.sign

@Composable
fun NavGraph(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    windowSize: WindowWidthSizeClass,
    paddingValues: PaddingValues,
    signInViewModel: SignInViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Routes.DashboardScreen
    ) {

        composable<Routes.SignInScreen> {
            val state by signInViewModel.state.collectAsStateWithLifecycle()
            SignInScreen(
                windowSize = windowSize,
                snackbarHostState = snackbarHostState,
                paddingValues = paddingValues,
                state = state,
                uiEvent = signInViewModel.uiEvent,
                onEvent = signInViewModel::onEvent
            )
        }

        composable<Routes.DashboardScreen> {
            val viewModel: DashboardViewModel = hiltViewModel()
            DashboardScreen(
                snackbarHostState = snackbarHostState,
                paddingValues = paddingValues,
                state = DashboardState(),
                onEvent = viewModel::onEvent,
                uiEvent = viewModel.uiEvent,
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
                snackbarHostState = snackbarHostState,
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
                snackbarHostState = snackbarHostState,
                paddingValues = paddingValues,
                bodyPartId = bodyPartId,
                windowSize = windowSize,
                onBackIconClick = { navController.navigateUp() }
            )
        }

    }
}