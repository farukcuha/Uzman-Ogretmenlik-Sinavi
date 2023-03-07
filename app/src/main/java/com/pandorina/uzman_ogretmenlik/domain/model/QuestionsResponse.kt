package com.pandorina.uzman_ogretmenlik.domain.model

data class QuestionsResponse(
    val size: Int?,
    val questions: List<Question>
){

    data class Question(
        val questionNo: Int?,
        val questionText: String?,
        val answers: List<String>?,
        val correctAnswer: Int?
    )
}
