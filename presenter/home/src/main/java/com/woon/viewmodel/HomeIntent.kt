package com.woon.viewmodel

sealed class HomeIntent {
    object ChangeSortName : HomeIntent()
    object ChangeSortPrice : HomeIntent()
    object ChangeSortChange : HomeIntent()
    object ChangeSortVolume : HomeIntent()
}
