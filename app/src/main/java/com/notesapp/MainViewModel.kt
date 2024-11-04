package com.notesapp

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.notesapp.data.NoteModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel: ViewModel() {
    private val _currentUser = MutableStateFlow<FirebaseUser?>(null)
    val currentUser = _currentUser.asStateFlow()

    private val _notes = MutableStateFlow<List<NoteModel>>(getDummyNotes())
    val notes = _notes.asStateFlow()

    fun updateCurrentUser(user: FirebaseUser?) {
        _currentUser.value = user
    }

    fun updateNote(noteModel: NoteModel?, newTitle: String, newContent: String) {
        if (newTitle.isEmpty() && newContent.isEmpty()) return
        val note = noteModel?.apply {
            title = newTitle
            content = newContent

        } ?: NoteModel(title = newTitle, content = newContent)
        val currentList = _notes.value.toMutableList()
        currentList.find { it.id == note.id }?.also {
            currentList.remove(it)
        }
        currentList.add(0, note)
        _notes.value = currentList
    }

    fun onDeleteNote(note: NoteModel) {
        val currentList = _notes.value.toMutableList()
        currentList.find { it.id == note.id }?.also {
            currentList.remove(it)
        }
        _notes.value = currentList
    }

    companion object {
        fun getDummyNotes(): List<NoteModel> {
            return listOf(
                NoteModel("Grocery List", "Milk, Eggs, Bread, Cheese"),
                NoteModel("Meeting Notes", "Discuss project progress, assign tasks"),
                NoteModel("Travel Plans", "Book flights, hotels, and activities"),
                NoteModel("Reminders", "Pay bills, call the doctor, pick up dry cleaning"),
                NoteModel("Ideas", "New app concept, blog post topics, business ideas"),
                NoteModel("Quotes", "Inspirational quotes to keep you motivated"),
                NoteModel("Recipes", "Delicious recipes to try out"),
                NoteModel("Workout Routine", "Exercise plan for the week"),
                NoteModel("Shopping List", "Clothes, shoes, accessories"),
                NoteModel("To-Do List", "Tasks to complete today"),
                NoteModel("Book Recommendations", "Must-read books for different genres"),
                NoteModel("Movie Watchlist", "Movies to watch in your free time"),
                NoteModel("Gift Ideas", "Gift ideas for friends and family"),
                NoteModel("Important Dates", "Birthdays, anniversaries, appointments"),
                NoteModel("Passwords", "Securely store your passwords"),
                NoteModel("Journal Entry", "Reflect on your day and thoughts"),
                NoteModel("Dream Log", "Record your dreams and interpretations"),
                NoteModel("Gratitude List", "Things you are grateful for"),
                NoteModel("Bucket List", "Things you want to do before you die"),
                NoteModel("Learning Resources", "Websites, courses, and books for learning")
            )
        }
    }
}
