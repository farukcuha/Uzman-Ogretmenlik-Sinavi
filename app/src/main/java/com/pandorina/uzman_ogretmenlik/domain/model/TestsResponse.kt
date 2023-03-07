package com.pandorina.uzman_ogretmenlik.domain.model

data class TestsResponse(
    val size: Int?,
    val tests: List<Test>?
) {
    data class Test(
        val testTitle: String?,
        val testNo: Int?
    )
}
