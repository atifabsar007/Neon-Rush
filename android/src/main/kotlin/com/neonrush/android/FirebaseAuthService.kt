package com.neonrush.android

import android.app.Activity
import android.content.Intent
import android.text.InputType
import android.widget.EditText
import android.app.AlertDialog
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.neonrush.service.AuthService

/** Firebase-backed identity, cloud profile/progress and public score board. */
class FirebaseAuthService(private val activity: Activity) : AuthService {
    init { FirebaseAuthServiceHolder.service = this }
    private val auth=FirebaseAuth.getInstance()
    private val db=FirebaseFirestore.getInstance()
    private var googleCallback:((Result<AuthService.User>)->Unit)?=null
    private fun user()=auth.currentUser?.let { AuthService.User(it.uid,it.email,it.displayName,it.isAnonymous) }
    override fun currentUser()=user()
    override fun promptInput(title:String,hint:String,initial:String,secure:Boolean,callback:(String?)->Unit){
        activity.runOnUiThread {
            val field=EditText(activity).apply { this.hint=hint; setText(initial); inputType=if(secure) InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD else InputType.TYPE_CLASS_TEXT or if(hint.contains("email",true)) InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS else InputType.TYPE_TEXT_FLAG_CAP_SENTENCES; setSingleLine(true) }
            AlertDialog.Builder(activity).setTitle(title).setView(field).setPositiveButton("Continue"){_,_->callback(field.text?.toString())}.setNegativeButton("Cancel"){_,_->callback(null)}.setOnCancelListener{callback(null)}.show()
        }
    }
    override fun signUp(name:String,email:String,password:String,callback:(Result<AuthService.User>)->Unit){
        if(name.isBlank()||email.isBlank()||password.length<6){callback(Result.failure(IllegalArgumentException("Enter a name, valid email and password of at least 6 characters.")));return}
        val credential=com.google.firebase.auth.EmailAuthProvider.getCredential(email.trim(),password)
        val existing=auth.currentUser
        val task=if(existing?.isAnonymous==true) existing.linkWithCredential(credential) else auth.createUserWithEmailAndPassword(email.trim(),password)
        task.addOnCompleteListener { t ->
            if(!t.isSuccessful){callback(Result.failure(t.exception?:Exception("Registration failed")));return@addOnCompleteListener}
            val u=auth.currentUser!!
            val normalized=name.trim().lowercase()
            val profile=com.google.firebase.auth.UserProfileChangeRequest.Builder().setDisplayName(name.trim()).build()
            u.updateProfile(profile).addOnCompleteListener {
                val usernameRef=db.collection("usernames").document(normalized)
                usernameRef.set(mapOf("uid" to u.uid)).addOnSuccessListener {
                    db.collection("players").document(u.uid).set(mapOf("uid" to u.uid,"username" to normalized,"name" to name.trim(),"email" to email.trim()),com.google.firebase.firestore.SetOptions.merge())
                    db.collection("leaderboard").document(u.uid).set(mapOf("uid" to u.uid,"name" to name.trim(),"level" to 1),com.google.firebase.firestore.SetOptions.merge())
                    callback(Result.success(user()!!))
                }.addOnFailureListener { callback(Result.failure(IllegalStateException("That username is already taken. Choose another one."))) }
            }
        }
    }
    override fun login(email:String,password:String,callback:(Result<AuthService.User>)->Unit){
        val identifier=email.trim()
        if(identifier.contains("@")){
            auth.signInWithEmailAndPassword(identifier,password).addOnCompleteListener { if(it.isSuccessful) callback(Result.success(user()!!)) else callback(Result.failure(it.exception?:Exception("Login failed"))) }
            return
        }
        com.google.firebase.functions.FirebaseFunctions.getInstance().getHttpsCallable("loginWithUsername")
            .call(mapOf("username" to identifier.lowercase(),"password" to password))
            .addOnSuccessListener { result ->
                val token=(result.data as? Map<*,*>)?.get("customToken") as? String
                if(token.isNullOrBlank()){callback(Result.failure(IllegalStateException("Username login could not be completed.")));return@addOnSuccessListener}
                auth.signInWithCustomToken(token).addOnCompleteListener { task ->
                    if(task.isSuccessful) callback(Result.success(user()!!))
                    else callback(Result.failure(task.exception?:Exception("Username login failed")))
                }
            }.addOnFailureListener { callback(Result.failure(it)) }
    }
    override fun googleSignIn(callback:(Result<AuthService.User>)->Unit){
        // The Google Services plugin generates this resource only when a valid
        // google-services.json contains an OAuth web client. Resolve it by name
        // so the project still compiles before Firebase has been configured.
        val clientId = runCatching {
            val resourceId = activity.resources.getIdentifier(
                "default_web_client_id", "string", activity.packageName
            )
            if (resourceId != 0) activity.getString(resourceId) else ""
        }.getOrDefault("")
        if (clientId.isBlank()) {
            callback(Result.failure(IllegalStateException(
                "Google Sign-In is not configured yet. Add android/google-services.json with a Web OAuth client ID, then sync Gradle. Email login and guest play can still be used."
            )))
            return
        }
        val options=GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(clientId).requestEmail().build()
        googleCallback=callback; activity.startActivityForResult(GoogleSignIn.getClient(activity,options).signInIntent, GOOGLE_REQUEST)
    }
    fun onActivityResult(requestCode:Int,resultCode:Int,data:Intent?){ if(requestCode!=GOOGLE_REQUEST)return; val cb=googleCallback?:return; googleCallback=null; try { val account=GoogleSignIn.getSignedInAccountFromIntent(data).getResult(ApiException::class.java); val credential=GoogleAuthProvider.getCredential(account.idToken,null); val existing=auth.currentUser; val task=if(existing?.isAnonymous==true) existing.linkWithCredential(credential) else auth.signInWithCredential(credential); task.addOnCompleteListener { if(it.isSuccessful){ val u=user()!!; db.collection("players").document(u.uid).set(mapOf("uid" to u.uid,"name" to (u.displayName?:"NEON RUNNER"),"email" to u.email),com.google.firebase.firestore.SetOptions.merge()); db.collection("leaderboard").document(u.uid).set(mapOf("uid" to u.uid,"name" to (u.displayName?:"NEON RUNNER")),com.google.firebase.firestore.SetOptions.merge()); cb(Result.success(u)) } else cb(Result.failure(it.exception?:Exception("Google authentication failed"))) } } catch(e:Exception){cb(Result.failure(e))} }
    override fun resetPassword(email:String,callback:(Result<Unit>)->Unit){ if(email.isBlank()){callback(Result.failure(IllegalArgumentException("Enter your email address.")));return}; auth.sendPasswordResetEmail(email.trim()).addOnCompleteListener { if(it.isSuccessful)callback(Result.success(Unit)) else callback(Result.failure(it.exception?:Exception("Could not send reset email"))) } }
    override fun continueAsGuest(name:String,callback:(Result<AuthService.User>)->Unit){ auth.signInAnonymously().addOnCompleteListener { if(it.isSuccessful){val u=auth.currentUser!!; val profile=com.google.firebase.auth.UserProfileChangeRequest.Builder().setDisplayName(name.trim()).build(); u.updateProfile(profile); db.collection("players").document(u.uid).set(mapOf("uid" to u.uid,"name" to name.trim(),"score" to 0,"level" to 1,"guest" to true),com.google.firebase.firestore.SetOptions.merge()); db.collection("leaderboard").document(u.uid).set(mapOf("uid" to u.uid,"name" to name.trim(),"score" to 0,"level" to 1)); callback(Result.success(AuthService.User(u.uid,null,name,true))) }else callback(Result.failure(it.exception?:Exception("Guest mode failed"))) } }
    override fun logout(callback:(Result<Unit>)->Unit){ auth.signOut(); GoogleSignIn.getClient(activity,GoogleSignInOptions.DEFAULT_SIGN_IN).signOut().addOnCompleteListener { callback(Result.success(Unit)) } }
    override fun updateProfile(name:String,callback:(Result<Unit>)->Unit){ val u=auth.currentUser?:run{callback(Result.failure(IllegalStateException("Not signed in")));return}; val req=com.google.firebase.auth.UserProfileChangeRequest.Builder().setDisplayName(name.trim()).build(); u.updateProfile(req).addOnCompleteListener { if(it.isSuccessful){db.collection("players").document(u.uid).update("name",name.trim());db.collection("leaderboard").document(u.uid).set(mapOf("name" to name.trim()),com.google.firebase.firestore.SetOptions.merge());callback(Result.success(Unit))}else callback(Result.failure(it.exception?:Exception("Profile update failed"))) } }
    override fun syncProgress(name:String,score:Int,level:Int,xp:Int,coins:Int,runs:Int,distance:Int,orbs:Int){ val u=auth.currentUser?:return; val ref=db.collection("players").document(u.uid); ref.get().addOnSuccessListener { old -> val best=maxOf(score,(old.getLong("score")?:0L).toInt()); val data=mapOf("uid" to u.uid,"name" to (u.displayName?:name),"email" to u.email,"score" to best,"level" to level,"xp" to xp,"coins" to coins,"runs" to runs,"distance" to distance,"orbs" to orbs,"updatedAt" to com.google.firebase.firestore.FieldValue.serverTimestamp()); ref.set(data,com.google.firebase.firestore.SetOptions.merge()); db.collection("leaderboard").document(u.uid).set(mapOf("uid" to u.uid,"name" to (u.displayName?:name),"score" to best,"level" to level,"updatedAt" to com.google.firebase.firestore.FieldValue.serverTimestamp()),com.google.firebase.firestore.SetOptions.merge()) } }
    override fun restoreProgress(callback:(Result<AuthService.Progress?>)->Unit){
        val u=auth.currentUser?:run{callback(Result.success(null));return}
        db.collection("players").document(u.uid).get().addOnSuccessListener { d ->
            if(!d.exists()){callback(Result.success(null));return@addOnSuccessListener}
            fun n(key:String)=(d.getLong(key)?:0L).toInt()
            callback(Result.success(AuthService.Progress(n("score"),n("level"),n("xp"),n("coins"),n("runs"),n("distance"),n("orbs"))))
        }.addOnFailureListener { callback(Result.failure(it)) }
    }
    override fun loadLeaderboard(callback:(Result<List<AuthService.PlayerScore>>)->Unit){ db.collection("leaderboard").orderBy("score",com.google.firebase.firestore.Query.Direction.DESCENDING).limit(10).get().addOnSuccessListener { snap -> callback(Result.success(snap.documents.map { AuthService.PlayerScore(it.getString("name")?:"PLAYER",(it.getLong("score")?:0L).toInt(),(it.getLong("level")?:1L).toInt()) })) }.addOnFailureListener { callback(Result.failure(it)) } }
    companion object { const val GOOGLE_REQUEST=7124 }
}

internal object FirebaseAuthServiceHolder { var service: FirebaseAuthService? = null }
