package com.f1.quiket.feature.floating.presentation.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.f1.quiket.feature.floating.domain.model.StudyPurpose
import com.f1.quiket.feature.floating.presentation.contract.AddSubjectContract
import com.f1.quiket.feature.floating.presentation.screen.addsubject.AddSubjectScreen
import com.f1.quiket.feature.floating.presentation.viewmodel.AddSubjectViewModel

@Composable
fun AddSubjectRoute(
    onFinish: () -> Unit = {},
    onDismiss: () -> Unit = {},
    viewModel: AddSubjectViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                AddSubjectContract.Effect.NavigateBack -> onDismiss()
                AddSubjectContract.Effect.NavigateToSuccess -> onFinish()
            }
        }
    }

    AddSubjectScreen(
        onFinish = { viewModel.handleIntent(AddSubjectContract.Intent.Finish) },
        onDismiss = { viewModel.handleIntent(AddSubjectContract.Intent.Skip) },
    )
}