package com.example.forestquiz.data

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class QuizRepository {

    private val db = Firebase.firestore

    // Fetches all questions from the "questions" collection in Firestore.
    // This is a suspend function to work well with coroutines.
    suspend fun getAllQuestions(): List<Qna> {
        return try {
            val snapshot = db.collection("questions").get().await()
            // Map Firestore documents to our Qna data class
            snapshot.documents.mapNotNull { document ->
                Qna(
                    q = document.getString("Question") ?: "",
                    a = document.getString("ans") ?: "",
                    o1 = document.getString("op1") ?: "",
                    o2 = document.getString("op2") ?: "",
                    o3 = document.getString("op3") ?: ""
                )
            }
        } catch (e: Exception) {
            // In a real app, you'd handle this error more gracefully
            e.printStackTrace()
            emptyList()
        }
    }
}