package com.nedajalili.wordweave

data class Level(
    val id: Int,
    val title: String,
    var isUnlocked: Boolean // وضعیت باز یا قفل بودن مرحله
)
