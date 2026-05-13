package com.f1.quiket.feature.floating.presentation.navigation

import com.f1.quiket.core.navigation.QuiketDestination
import com.f1.quiket.feature.floating.domain.model.ExamType
import com.f1.quiket.feature.floating.domain.model.StudyField
import com.f1.quiket.feature.floating.domain.model.StudyPurpose
import com.f1.quiket.feature.floating.domain.model.UsagePurpose

data object ScheduleExamDestination : QuiketDestination {
    override val route: String = "schedule_exam"
}

data object CreateQuizDestination : QuiketDestination {
    override val route: String = "create_quiz"
}

data object UploadDestination : QuiketDestination {
    override val route: String = "upload"
}

data object AddSubjectDestination : QuiketDestination {
    override val route: String = "add_subject"
}

data object AddSubjectStep1Destination : QuiketDestination {
    override val route: String = "add_subject/step1"
}

data object AddSubjectStep2Destination : QuiketDestination {
    const val ARG_PURPOSE = "purpose"
    override val route: String = "add_subject/step2/{$ARG_PURPOSE}"

    fun createRoute(purpose: StudyPurpose) = "add_subject/step2/${purpose.name}"
}

data object AddSubjectStep3Destination : QuiketDestination {
    const val ARG_PURPOSE = "purpose"
    const val ARG_EXAM_TYPE = "examType"
    const val ARG_FIELD = "field"
    const val ARG_USAGE = "usage"

    override val route: String =
        "add_subject/step3/{$ARG_PURPOSE}?$ARG_EXAM_TYPE={$ARG_EXAM_TYPE}&$ARG_FIELD={$ARG_FIELD}&$ARG_USAGE={$ARG_USAGE}"

    fun createRoute(
        purpose: StudyPurpose,
        examType: ExamType? = null,
        studyField: StudyField? = null,
        usagePurpose: UsagePurpose? = null,
    ) = buildString {
        append("add_subject/step3/${purpose.name}")
        append("?$ARG_EXAM_TYPE=${examType?.name ?: ""}")
        append("&$ARG_FIELD=${studyField?.name ?: ""}")
        append("&$ARG_USAGE=${usagePurpose?.name ?: ""}")
    }
}