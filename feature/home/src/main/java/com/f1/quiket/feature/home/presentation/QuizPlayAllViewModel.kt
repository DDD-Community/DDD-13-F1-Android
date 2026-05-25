package com.f1.quiket.feature.home.presentation

import com.f1.quiket.core.common.mvi.MviViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QuizPlayAllViewModel @Inject constructor() :
    MviViewModel<QuizPlayAllState, QuizPlayAllIntent, QuizPlayAllEffect>(
        initialState = QuizPlayAllState(
            questions = mockQuestions,
        ),
    ) {
    override fun handleIntent(intent: QuizPlayAllIntent) {
        when (intent) {
            is QuizPlayAllIntent.SelectOption -> selectOption(intent.optionId)
            QuizPlayAllIntent.MovePrevious -> movePrevious()
            QuizPlayAllIntent.MoveNext -> moveNext()
            QuizPlayAllIntent.ToggleBookmark -> toggleBookmark()
            QuizPlayAllIntent.OpenQuestionList -> setQuestionListVisible(true)
            QuizPlayAllIntent.CloseQuestionList -> setQuestionListVisible(false)
            is QuizPlayAllIntent.SelectQuestion -> selectQuestion(intent.questionIndex)
            QuizPlayAllIntent.OpenSubmitConfirm -> setSubmitConfirmVisible(true)
            QuizPlayAllIntent.CloseSubmitConfirm -> setSubmitConfirmVisible(false)
            QuizPlayAllIntent.Submit -> setSubmitConfirmVisible(false)
            QuizPlayAllIntent.DismissTutorial -> dismissTutorial()
        }
    }

    private fun selectOption(optionId: String) {
        updateState {
            val questionId = currentQuestion?.id ?: return@updateState this
            copy(
                selectedOptionIds = selectedOptionIds + (questionId to optionId),
            )
        }
    }

    private fun movePrevious() {
        updateState {
            copy(
                currentQuestionIndex = (currentQuestionIndex - 1).coerceAtLeast(0),
            )
        }
    }

    private fun moveNext() {
        updateState {
            copy(
                currentQuestionIndex = (currentQuestionIndex + 1).coerceAtMost(
                    (totalQuestionCount - 1).coerceAtLeast(0),
                ),
            )
        }
    }

    private fun toggleBookmark() {
        updateState {
            val questionId = currentQuestion?.id ?: return@updateState this
            val nextBookmarks = if (bookmarkedQuestionIds.contains(questionId)) {
                bookmarkedQuestionIds - questionId
            } else {
                bookmarkedQuestionIds + questionId
            }
            copy(bookmarkedQuestionIds = nextBookmarks)
        }
    }

    private fun setQuestionListVisible(visible: Boolean) {
        updateState {
            copy(isQuestionListVisible = visible)
        }
    }

    private fun selectQuestion(questionIndex: Int) {
        updateState {
            copy(
                currentQuestionIndex = questionIndex.coerceIn(
                    minimumValue = 0,
                    maximumValue = (totalQuestionCount - 1).coerceAtLeast(0),
                ),
                isQuestionListVisible = false,
            )
        }
    }

    private fun setSubmitConfirmVisible(visible: Boolean) {
        updateState {
            copy(isSubmitConfirmVisible = visible)
        }
    }

    private fun dismissTutorial() {
        updateState {
            copy(showTutorial = false)
        }
    }

    private companion object {
        val mockQuestions = listOf(
            QuizPlayAllQuestion(
                id = "question-1",
                number = 1,
                body = "SQL에서 두 테이블의 공통 컬럼을 기준으로 행을 결합하는 연산은 무엇인가요?",
                timerText = "00:30",
                options = listOf(
                    option("question-1", 1, "SELECT"),
                    option("question-1", 2, "JOIN"),
                    option("question-1", 3, "WHERE"),
                    option("question-1", 4, "GROUP BY"),
                ),
            ),
            QuizPlayAllQuestion(
                id = "question-2",
                number = 2,
                body = "다음 중 '좋은 데이터 모델의 요소'로 가장 거리가 먼 것은?",
                timerText = "00:30",
                options = listOf(
                    option("question-2", 1, "완전성"),
                    option("question-2", 2, "중복성"),
                    option("question-2", 3, "업무 규칙 반영"),
                    option("question-2", 4, "확장성"),
                ),
            ),
            QuizPlayAllQuestion(
                id = "question-3",
                number = 3,
                body = "다음 중 스키마(Schema)에 대한 설명으로 가장 적절한 것은?",
                timerText = "00:29",
                options = listOf(
                    option("question-3", 1, "데이터베이스의 논리적 구조와 제약조건을 정의한 것이다."),
                    option("question-3", 2, "데이터를 일시적으로 저장하는 메모리 공간이다."),
                    option("question-3", 3, "SQL 실행 결과를 화면에 출력하는 별도 프로그램이다."),
                    option("question-3", 4, "테이블 간 물리적 저장 순서를 의미한다."),
                ),
            ),
            QuizPlayAllQuestion(
                id = "question-4",
                number = 4,
                body = "정규화의 주된 목적으로 가장 알맞은 것은?",
                timerText = "00:28",
                options = listOf(
                    option("question-4", 1, "데이터 중복과 이상 현상을 줄인다."),
                    option("question-4", 2, "항상 조회 속도를 최대로 높인다."),
                    option("question-4", 3, "모든 테이블을 하나로 합친다."),
                    option("question-4", 4, "인덱스를 자동 생성한다."),
                ),
            ),
            QuizPlayAllQuestion(
                id = "question-5",
                number = 5,
                body = "기본키에 대한 설명으로 옳은 것은?",
                timerText = "00:27",
                options = listOf(
                    option("question-5", 1, "NULL 값을 여러 개 가질 수 있다."),
                    option("question-5", 2, "각 행을 고유하게 식별한다."),
                    option("question-5", 3, "반드시 외래키와 같은 이름이어야 한다."),
                    option("question-5", 4, "한 테이블에 무제한으로 지정할 수 있다."),
                ),
            ),
            QuizPlayAllQuestion(
                id = "question-6",
                number = 6,
                body = "외래키가 주로 표현하는 관계는 무엇인가요?",
                timerText = "00:27",
                options = listOf(
                    option("question-6", 1, "테이블 간 참조 관계"),
                    option("question-6", 2, "쿼리 실행 순서"),
                    option("question-6", 3, "파일 저장 위치"),
                    option("question-6", 4, "인덱스 정렬 방식"),
                ),
            ),
            QuizPlayAllQuestion(
                id = "question-7",
                number = 7,
                body = "다음 중 SQL 집계 함수가 아닌 것은?",
                timerText = "00:26",
                options = listOf(
                    option("question-7", 1, "COUNT"),
                    option("question-7", 2, "SUM"),
                    option("question-7", 3, "AVG"),
                    option("question-7", 4, "ORDER"),
                ),
            ),
            QuizPlayAllQuestion(
                id = "question-8",
                number = 8,
                body = "WHERE 절의 역할로 가장 적절한 것은?",
                timerText = "00:25",
                options = listOf(
                    option("question-8", 1, "조회할 행의 조건을 지정한다."),
                    option("question-8", 2, "출력 컬럼의 별칭만 지정한다."),
                    option("question-8", 3, "테이블을 생성한다."),
                    option("question-8", 4, "트랜잭션을 종료한다."),
                ),
            ),
            QuizPlayAllQuestion(
                id = "question-9",
                number = 9,
                body = "GROUP BY와 함께 자주 사용되는 절은 무엇인가요?",
                timerText = "00:24",
                options = listOf(
                    option("question-9", 1, "HAVING"),
                    option("question-9", 2, "VALUES"),
                    option("question-9", 3, "COMMIT"),
                    option("question-9", 4, "ROLLBACK"),
                ),
            ),
            QuizPlayAllQuestion(
                id = "question-10",
                number = 10,
                body = "다음 중 관계의 분류 기준으로 가장 적절한 것은?",
                timerText = "00:23",
                options = listOf(
                    option("question-10", 1, "관계명과 관계 차수"),
                    option("question-10", 2, "존재에 의한 관계와 행위에 의한 관계"),
                    option("question-10", 3, "컬럼 길이와 데이터 타입"),
                    option("question-10", 4, "저장 위치와 파일 크기"),
                ),
            ),
        )

        fun option(questionId: String, number: Int, text: String): QuizPlayAllOption =
            QuizPlayAllOption(
                id = "$questionId-option-$number",
                number = number,
                text = text,
            )
    }
}
