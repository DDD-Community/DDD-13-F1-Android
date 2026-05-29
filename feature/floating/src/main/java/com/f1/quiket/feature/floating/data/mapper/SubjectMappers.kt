package com.f1.quiket.feature.floating.data.mapper

import com.f1.quiket.feature.floating.data.remote.CertificateResponse
import com.f1.quiket.feature.floating.data.remote.ChapterResponse
import com.f1.quiket.feature.floating.data.remote.ChapterWithPartsResponse
import com.f1.quiket.feature.floating.data.remote.PartResponse
import com.f1.quiket.feature.floating.data.remote.PartSummaryResponse
import com.f1.quiket.feature.floating.data.remote.SubjectCreateRequest
import com.f1.quiket.feature.floating.data.remote.SubjectDetailResponse
import com.f1.quiket.feature.floating.data.remote.SubjectExamDetailRequest
import com.f1.quiket.feature.floating.data.remote.SubjectExamScheduleResponse
import com.f1.quiket.feature.floating.data.remote.SubjectOtherDetailRequest
import com.f1.quiket.feature.floating.data.remote.SubjectResponse
import com.f1.quiket.feature.floating.data.remote.SubjectReviewDetailRequest
import com.f1.quiket.feature.floating.data.remote.SubjectSummaryResponse
import com.f1.quiket.feature.floating.domain.model.Certificate
import com.f1.quiket.feature.floating.domain.model.ChapterWithParts
import com.f1.quiket.feature.floating.domain.model.Part
import com.f1.quiket.feature.floating.domain.model.PartSummary
import com.f1.quiket.feature.floating.domain.model.Subject
import com.f1.quiket.feature.floating.domain.model.SubjectChapter
import com.f1.quiket.feature.floating.domain.model.SubjectCreate
import com.f1.quiket.feature.floating.domain.model.SubjectDetail
import com.f1.quiket.feature.floating.domain.model.SubjectExamDetail
import com.f1.quiket.feature.floating.domain.model.SubjectExamSchedule
import com.f1.quiket.feature.floating.domain.model.SubjectOtherDetail
import com.f1.quiket.feature.floating.domain.model.SubjectReviewDetail
import com.f1.quiket.feature.floating.domain.model.SubjectSummary

fun SubjectSummaryResponse.toDomain(): SubjectSummary = SubjectSummary(
    id = id,
    name = name,
    purpose = purpose,
    chapterCount = chapterCount,
    partCount = partCount,
)

fun SubjectResponse.toDomain(): Subject = Subject(
    id = id,
    name = name,
    purpose = purpose,
    createdAt = createdAt,
)

fun SubjectDetailResponse.toDomain(): SubjectDetail = SubjectDetail(
    id = id,
    name = name,
    purpose = purpose,
    createdAt = createdAt,
    chapters = chapters.map { chapter -> chapter.toDomain() },
    examSchedule = examSchedule?.toDomain(),
)

fun SubjectCreate.toRequest(): SubjectCreateRequest = SubjectCreateRequest(
    name = name,
    purpose = purpose,
    examDetail = examDetail?.toRequest(),
    reviewDetail = reviewDetail?.toRequest(),
    otherDetail = otherDetail?.toRequest(),
)

fun SubjectExamDetail.toRequest(): SubjectExamDetailRequest = SubjectExamDetailRequest(
    examType = examType,
    univMajorField = univMajorField,
    univMajorName = univMajorName,
    univCourseType = univCourseType,
    mhGrade = mhGrade,
    mhSubjectType = mhSubjectType,
    certificateId = certificateId,
    certificateName = certificateName,
    civilRank = civilRank,
    civilSeries = civilSeries,
    langType = langType,
    langExamName = langExamName,
    otherExamName = otherExamName,
)

fun SubjectReviewDetail.toRequest(): SubjectReviewDetailRequest = SubjectReviewDetailRequest(
    field = field,
    studyLevel = studyLevel,
)

fun SubjectOtherDetail.toRequest(): SubjectOtherDetailRequest = SubjectOtherDetailRequest(
    usagePurpose = usagePurpose,
    description = description,
)

fun CertificateResponse.toDomain(): Certificate = Certificate(
    id = id,
    name = name,
    featured = featured,
    displayOrder = displayOrder,
)

fun ChapterResponse.toDomain(): SubjectChapter = SubjectChapter(
    id = id,
    subjectId = subjectId,
    name = name,
    displayOrder = displayOrder,
)

fun ChapterWithPartsResponse.toDomain(): ChapterWithParts = ChapterWithParts(
    id = id,
    subjectId = subjectId,
    name = name,
    displayOrder = displayOrder,
    parts = parts.map { part -> part.toDomain() },
)

fun PartSummaryResponse.toDomain(): PartSummary = PartSummary(
    id = id,
    chapterId = chapterId,
    name = name,
    partNumber = partNumber,
    contentPreview = contentPreview,
)

fun PartResponse.toDomain(): Part = Part(
    id = id,
    chapterId = chapterId,
    name = name,
    partNumber = partNumber,
    content = content,
)

fun SubjectExamScheduleResponse.toDomain(): SubjectExamSchedule = SubjectExamSchedule(
    id = id,
    subjectId = subjectId,
    examName = examName,
    examDate = examDate,
    dDay = dDay,
)
