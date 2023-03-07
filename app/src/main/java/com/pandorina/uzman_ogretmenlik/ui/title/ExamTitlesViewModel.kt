package com.pandorina.uzman_ogretmenlik.ui.title

import androidx.lifecycle.MutableLiveData
import com.pandorina.uzman_ogretmenlik.core.BaseViewModel
import com.pandorina.uzman_ogretmenlik.domain.ExamRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class ExamTitlesViewModel @Inject constructor(
    private val repository: ExamRepository
): BaseViewModel() {

    override val TAG: String = "ExamTitles"

    val titles = MutableLiveData<List<String>?>()

    init {
        launchViewModelScope {
            repository.getStudyExamTitles().collectLatest { result ->
                result.onSuccess {
                    titles.value = it.titles
                    stopLoading()
                }.onFailure { throwable ->
                    sendError(throwable.localizedMessage)
                    stopLoading()
                }
            }
        }
    }
}