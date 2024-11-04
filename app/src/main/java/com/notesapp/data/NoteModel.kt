package com.notesapp.data

import java.util.UUID


data class NoteModel(
//    val user: String,
    var title: String,
    var content: String,
    val id: UUID = UUID.randomUUID(),
)
