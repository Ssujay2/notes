package com.notesapp.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.firebase.auth.FirebaseUser
import com.notesapp.MainViewModel
import com.notesapp.data.NoteModel
import com.notesapp.ui.theme.NotesAppTheme

@Composable
fun MainScreen(
    viewModel: MainViewModel,
    onLogin: (email: String, password: String) -> Unit,
    onSignup: (email: String, password: String) -> Unit,
    onLogOut: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val notes by viewModel.notes.collectAsStateWithLifecycle()

    Box(
        modifier = modifier
            .padding(10.dp)
            .fillMaxSize()
    ) {
        if (currentUser != null) {
            HomeContent(
                onLogOut = onLogOut,
                user = currentUser!!,
                notes = notes,
                onUpdateNote = { note, newTitle, newContent ->
                    viewModel.updateNote(
                        noteModel = note,
                        newTitle = newTitle,
                        newContent = newContent
                    )
                },
                onDeleteNote = {
                    viewModel.onDeleteNote(it)
                }
            )
        } else {
            Login(
                modifier = Modifier
                    .align(Alignment.Center),
                onLogin = onLogin,
                onSignup = onSignup
            )
        }
    }
}

@Composable
fun Login(
    onLogin: (email: String, password: String) -> Unit,
    onSignup: (email: String, password: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var email by rememberSaveable {
        mutableStateOf("")
    }

    var password by rememberSaveable {
        mutableStateOf("")
    }

    Column(
        modifier = modifier
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = email,
            onValueChange = {
                email = it
            },
            label = {
                Text(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    text = "Email",
                    style = MaterialTheme.typography.labelLarge,
                    textAlign = TextAlign.Center
                )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Ascii,
                imeAction = ImeAction.Go
            ),
            textStyle = MaterialTheme.typography.headlineMedium.copy(textAlign = TextAlign.Center)
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = password,
            onValueChange = {
                password = it
            },
            label = {
                Text(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    text = "Password",
                    style = MaterialTheme.typography.labelLarge,
                    textAlign = TextAlign.Center
                )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Go
            ),
            visualTransformation = PasswordVisualTransformation(),
            textStyle = MaterialTheme.typography.headlineMedium.copy(textAlign = TextAlign.Center)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Button(
                onClick = { onLogin(email, password) },
                modifier = Modifier.align(Alignment.Center)
            ) {
                Text("Login")
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Button(
                onClick = { onSignup(email, password) },
                modifier = Modifier.align(Alignment.Center)
            ) {
                Text("SignUp")
            }
        }
    }
}

@Composable
fun HomeContent(
    onLogOut: () -> Unit,
    user: FirebaseUser,
    notes: List<NoteModel>,
    onUpdateNote: (note: NoteModel?, newTitle: String, newContent: String) -> Unit,
    onDeleteNote: (note: NoteModel) -> Unit,
    modifier: Modifier = Modifier
) {
    var showNoteEditDialog by rememberSaveable {
        mutableStateOf(false)
    }
    Box(modifier = modifier.fillMaxSize()) {
        Column {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = onLogOut,
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Text("Log out")
                }
                Text("Hello ${user.email ?: ""}!")
            }

            Spacer(Modifier.height(12.dp))

            NotesContent(
                notes = notes,
                onUpdateNote = onUpdateNote,
                showNoteEditDialog = showNoteEditDialog,
                onUpdateShowNoteEditDialog = {
                    showNoteEditDialog = it
                },
                onDeleteNote = onDeleteNote
            )
        }

        FloatingActionButton(
            onClick = {
                showNoteEditDialog = true
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Create,
                contentDescription = "Add Note"
            )
        }
    }
}

@Composable
fun NotesContent(
    notes: List<NoteModel>,
    onUpdateNote: (note: NoteModel?, newTitle: String, newContent: String) -> Unit,
    showNoteEditDialog: Boolean,
    onUpdateShowNoteEditDialog: (show: Boolean) -> Unit,
    onDeleteNote: (note: NoteModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    var selectedNote by rememberSaveable {
        mutableStateOf<NoteModel?>(null)
    }

    if (notes.isNotEmpty()) {
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            modifier = modifier.fillMaxSize(),
            verticalItemSpacing = 8.dp,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            state = rememberLazyStaggeredGridState()
        ) {
            items(notes) { note ->
                Note(
                    modifier = Modifier.clickable {
                        selectedNote = note
                        onUpdateShowNoteEditDialog(true)
                    },
                    title = note.title,
                    content = note.content
                )
            }
        }
    } else {
        Text("No notes")
    }
    if (showNoteEditDialog) {
        NoteEditDialog(
            onDismissDialog = { note, newTitle, newContent ->
                selectedNote = null
                onUpdateShowNoteEditDialog(false)
                onUpdateNote(note, newTitle, newContent)
            },
            onDeleteNote = onDeleteNote,
            note = selectedNote
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    NotesAppTheme {
        Box(
            modifier = Modifier.padding(20.dp)
        ) {
            Login(onLogin = { _, _ -> }, onSignup = { _, _ -> })
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NotesContentPreview() {
    NotesAppTheme {
        Box(
            modifier = Modifier.padding(20.dp)
        ) {
            NotesContent(
                notes = MainViewModel.getDummyNotes(),
                onUpdateNote = { _, _, _ -> },
                onUpdateShowNoteEditDialog = { },
                showNoteEditDialog = true,
                onDeleteNote = { },
            )
        }
    }
}
