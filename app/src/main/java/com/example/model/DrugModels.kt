package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
// Agar 'Drug' red ho, toh Alt+Enter dabayein
// import com.example.model.Drug 
import kotlinx.coroutines.launch

class DrugViewModel : ViewModel() {
    
    // Yeh function app ko crash hone se bachayega
    fun insertAllDrugs(drugs: List<Any>) { // Abhi ke liye List<Any> rakha hai taaki error na aaye
        viewModelScope.launch {
            // Yahan hum baad mein database ka code likhenge
        }
    }
}