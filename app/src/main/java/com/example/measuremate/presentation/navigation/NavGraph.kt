package com.example.measuremate.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.measuremate.presentation.add_item.AddItemScreen
import com.example.measuremate.presentation.add_item.AddItemViewModel
import com.example.measuremate.presentation.dashboard.DashboardScreen
import com.example.measuremate.presentation.dashboard.DashboardViewModel
import com.example.measuremate.presentation.details.DetailsScreen
import com.example.measuremate.presentation.details.DetailsViewModel
import com.example.measuremate.presentation.signin.SignInScreen
import com.example.measuremate.presentation.signin.SignInViewModel
import com.example.measuremate.presentation.util.UiEvent

@Composable
fun NavGraph(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    windowSize: WindowWidthSizeClass,
    paddingValues: PaddingValues,
    signInViewModel: SignInViewModel
) {

    //using outside the signIn Screen, bcz the sign in func takes time in completing
    LaunchedEffect(key1 = Unit) {
        signInViewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Snackbar -> {
                    snackbarHostState.showSnackbar(event.message)
                }

                UiEvent.HideBottomSheet -> {}
                UiEvent.Navigate -> {}
            }
        }
    }

    NavHost(
        modifier = Modifier.padding(paddingValues),
        navController = navController,
        startDestination = Routes.DashboardScreen
    ) {

        composable<Routes.SignInScreen> {
            val state by signInViewModel.state.collectAsStateWithLifecycle()
            SignInScreen(
                windowSize = windowSize,
                state = state,
                onEvent = signInViewModel::onEvent
            )
        }

        composable<Routes.DashboardScreen> {
            val viewModel: DashboardViewModel = hiltViewModel()
            val state by viewModel.state.collectAsStateWithLifecycle()
            DashboardScreen(
                snackbarHostState = snackbarHostState,
                state = state,
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
            val viewModel: AddItemViewModel = hiltViewModel()
            val state by viewModel.state.collectAsStateWithLifecycle()
            AddItemScreen(
                snackbarHostState = snackbarHostState,
                state = state,
                uiEvent = viewModel.uiEvent,
                onEvent = viewModel::onEvent,
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
        ) {
            val viewModel: DetailsViewModel = hiltViewModel()
            val state by viewModel.state.collectAsStateWithLifecycle()
            DetailsScreen(
                snackbarHostState = snackbarHostState,
                windowSize = windowSize,
                state = state,
                onEvent = viewModel::onEvent,
                uiEvent = viewModel.uiEvent,
                onBackIconClick = { navController.navigateUp() }
            )
        }

    }
}