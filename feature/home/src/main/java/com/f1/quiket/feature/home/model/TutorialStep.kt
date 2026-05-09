package com.f1.quiket.feature.home.model

import androidx.compose.ui.geometry.Rect

data class TutorialStep(
    val step: Int,
    val startText: String,
    val highlightedText: String,
    val endText: String,
    val tooltipAlignment: TooltipAlignment,
    val anchorRect: Rect?
)

enum class TooltipAlignment {
    Step1, Step2, Step3, Step4, Step5, Step6
}

enum class TutorialPage { FIRST, SECOND }