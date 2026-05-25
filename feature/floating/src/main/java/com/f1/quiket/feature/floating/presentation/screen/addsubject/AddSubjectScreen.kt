package com.f1.quiket.feature.floating.presentation.screen.addsubject

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.f1.quiket.feature.floating.domain.model.AddSubjectState
import com.f1.quiket.feature.floating.domain.model.ExamType
import com.f1.quiket.feature.floating.domain.model.StudyField
import com.f1.quiket.feature.floating.domain.model.StudyPurpose
import com.f1.quiket.feature.floating.domain.model.UsagePurpose
import com.f1.quiket.feature.floating.presentation.screen.UploadScreen
import com.f1.quiket.feature.floating.presentation.screen.subjectdetail.SubjectDetailScreen

@Composable
fun AddSubjectScreen(
    onFinish: () -> Unit = {},
    onDismiss: () -> Unit = {},
) {
    var state by remember { mutableStateOf(AddSubjectState()) }
    var depth by remember { mutableStateOf(1) }

    when (depth) {
        1 -> AddSubjectStep1Screen(
            onBackClick = onDismiss,
            onSkipClick = onFinish,
            onNextClick = { name, purpose ->
                state = state.copy(subjectName = name, studyPurpose = purpose)
                depth = 2
            },
        )

        2 -> AddSubjectStep2Screen(
            studyPurpose = state.studyPurpose ?: StudyPurpose.EXAM,
            onBackClick = { depth = 1 },
            onSkipClick = onFinish,
            onNextClick = { selection ->
                when (selection) {
                    is ExamType -> state = state.copy(examType = selection)
                    is StudyField -> state = state.copy(studyField = selection)
                    is UsagePurpose -> state = state.copy(usagePurpose = selection)
                }
                depth = 3
            },
        )

        3 -> AddSubjectStep3Screen(
            studyPurpose = state.studyPurpose ?: StudyPurpose.EXAM,
            examType = state.examType,
            studyField = state.studyField,
            usagePurpose = state.usagePurpose,
            onBackClick = { depth = 2 },
            onSkipClick = onFinish,
            onCreateClick = { depth = 4 },
        )

        4 -> SubjectDetailScreen(
            subjectName = state.subjectName.ifBlank { "새 과목" },
            studyPurposeLabel = state.studyPurpose?.title ?: "",
            examTypeLabel = state.examType?.label ?: state.studyField?.label ?: state.usagePurpose?.title ?: "",
            onBackClick = onFinish,
            onChapterAddClick = { depth = 5 },
            onUploadClick = { depth = 5 },
        )

        5 -> UploadScreen(
            lectureTitle = state.subjectName.ifBlank { "새 과목" },
            lecturePurpose = state.studyPurpose?.title,
            chapterCount = 0,
            onBackClick = { depth = 4 },
            onNextClick = { depth = 4 },
        )
    }
}