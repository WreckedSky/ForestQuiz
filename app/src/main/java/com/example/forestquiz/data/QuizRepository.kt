package com.example.forestquiz.data

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class QuizRepository {

    private val db = Firebase.firestore


    suspend fun getAllQuestions(): List<Qna> {
        return try {
            val snapshot = db.collection("questions").get().await()
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
            e.printStackTrace()
            emptyList()
        }
    }
}