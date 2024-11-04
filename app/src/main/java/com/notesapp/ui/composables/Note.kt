package com.notesapp.ui.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.notesapp.data.NoteModel
import com.notesapp.ui.theme.NotesAppTheme

@Composable
fun Note(
    title: String,
    content: String,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxSize(),
        shape = CardDefaults.outlinedShape,
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
        ) {
            Text(
                text = title,
                maxLines = 2,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = content,
                maxLines = 6,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun NoteEditDialog(
    note: NoteModel?,
    onDismissDialog: (note: NoteModel?, newTitle: String, newContent: String) -> Unit,
    onDeleteNote: (note: NoteModel) -> Unit,
    modifier: Modifier = Modifier
) {
    var title by rememberSaveable {
        mutableStateOf(note?.title.orEmpty())
    }
    var content by rememberSaveable {
        mutableStateOf(note?.content.orEmpty())
    }

    val textFieldColors = TextFieldDefaults.colors(
        unfocusedContainerColor = Color.Transparent,
        focusedContainerColor = Color.Transparent,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent,
        errorIndicatorColor = Color.Transparent
    )

    fun onDismiss() {
        onDismissDialog(note, title, content)
    }

    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(
            dismissOnBackPress = true,
            usePlatformDefaultWidth = false
        )
    ) {
        Surface(
            modifier = modifier.fillMaxWidth().padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Box(
                modifier = Modifier.padding(16.dp)
            ) {
                IconButton(
                    onClick = {
                        note?.let {
                            onDeleteNote(note)
                        }
                        onDismissDialog(null, "", "")
                    },
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete Note"
                    )
                }

                Column {
                    TextField(
                        modifier = modifier,
                        value = title,
                        onValueChange = { title = it },
                        minLines = 1,
                        label = {
                            if (title.isEmpty()) Text(text = "Title", style = MaterialTheme.typography.labelLarge)
                        },
                        colors = textFieldColors,
                        textStyle = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    TextField(
                        modifier = modifier,
                        value = content,
                        onValueChange = { content = it },
                        minLines = 4,
                        label = {
                            if (content.isEmpty()) Text(text = "Content", style = MaterialTheme.typography.labelMedium)
                        },
                        colors = textFieldColors,
                        textStyle = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            modifier = Modifier.weight(1f),
                            onClick = { onDismissDialog(null, "", "") }
                        ) {
                            Text("Cancel")
                        }

                        Spacer(Modifier.width(16.dp))

                        Button(
                            modifier = Modifier.weight(1f),
                            onClick = { onDismiss() }
                        ) {
                            Text(if (note != null) "Update" else "Add")
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NotesPreview() = NotesAppTheme {
    Note(
        title = "Title",
        content = "content",
    )
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun NoteEditDialogPreview() = NotesAppTheme {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        NoteEditDialog(
            note = null,
            onDismissDialog = { _, _, _ -> },
            onDeleteNote = { }
        )
    }
}
