package com.ards.ui.playground

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ards.model.Playground
import com.ards.ui.playground.repository.PlaygroundRepository
import com.ards.ui.playground.repository.ProcessesRepository

class ProcessesViewModel : ViewModel() {
    private val repository = ProcessesRepository()

    fun getPlaygroundList(apiUrl: String): LiveData<List<Playground>> {
        return repository.getPlayground(apiUrl)
    }
}
