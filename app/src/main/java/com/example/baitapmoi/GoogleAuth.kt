package com.example.baitapmoi


import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

suspend fun signInWithGoogle(
    context: Context,
    onSuccess: (FirebaseUser?) -> Unit,
    onError: (String) -> Unit
) {
    try {
        // ① Hiện hộp chọn tài khoản Google
        val option = GetSignInWithGoogleOption
            .Builder(context.getString(R.string.default_web_client_id))
            .build()
        val request = GetCredentialRequest.Builder().addCredentialOption(option).build()
        val result = CredentialManager.create(context).getCredential(context, request)

        // ② Lấy "vé" (idToken) Google trả về
        val cred = result.credential
        if (cred is CustomCredential &&
            cred.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
        ) {
            val idToken = GoogleIdTokenCredential.createFrom(cred.data).idToken

            // ③ Đưa vé cho Firebase để đăng nhập
            FirebaseAuth.getInstance()
                .signInWithCredential(GoogleAuthProvider.getCredential(idToken, null))
                .addOnSuccessListener { onSuccess(it.user) }
                .addOnFailureListener { onError(it.message ?: "Lỗi Firebase") }
        } else {
            onError("Không nhận được tài khoản Google")
        }
    } catch (e: Exception) {
        onError(e.message ?: "Đã huỷ đăng nhập")
    }
}