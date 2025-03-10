package com.ards.ui.playground

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ards.model.Playground
import com.ards.ui.playground.repository.PlaygroundRepository

class PlaygroundViewModel : ViewModel() {
    private val repository = PlaygroundRepository()

    val videoList: LiveData<List<Playground>> = repository.getVideos()
}
