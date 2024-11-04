package com.notesapp

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.notesapp.ui.composables.MainScreen
import com.notesapp.ui.theme.NotesAppTheme

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth

        enableEdgeToEdge()
        setContent {
            NotesAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding),
                        onLogin = { email, password ->
                            auth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(this) { task ->
                                    var message = "SignIn successful"
                                    if (task.isSuccessful) {
                                        Log.d(TAG, "SignIn successful")
                                        val user = auth.currentUser!!
                                        viewModel.updateCurrentUser(user)
                                    } else {
                                        Log.e(TAG, "SignIn failed", task.exception)
                                        message = "Authentication Failed"
                                    }
                                    Toast.makeText(
                                        baseContext,
                                        message,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        },
                        onSignup = { email, password ->
                            auth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(this) { task ->
                                    var message = "User created successfully"
                                    if (task.isSuccessful) {
                                        Log.d(TAG, "SignUp successful")
                                        val user = auth.currentUser!!
                                        viewModel.updateCurrentUser(user)
                                    } else {
                                        message = "User signup failed"
                                        Log.e(TAG, "Couldn't create a user", task.exception)
                                    }
                                    Toast.makeText(
                                        baseContext,
                                        message,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        },
                        onLogOut = {
                            auth.signOut()
                            viewModel.updateCurrentUser(null)
                        }
                    )
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.updateCurrentUser(auth.currentUser)
    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }
}
