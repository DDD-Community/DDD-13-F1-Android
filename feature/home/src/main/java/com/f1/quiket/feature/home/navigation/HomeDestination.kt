package com.f1.quiket.feature.home.navigation

import com.f1.quiket.core.navigation.QuiketDestination

data object HomeDestination : QuiketDestination {
    override val route: String = "main/home"
}

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

