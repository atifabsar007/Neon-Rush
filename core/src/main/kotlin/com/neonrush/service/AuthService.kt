package com.neonrush.service

/** Platform auth bridge. Android uses Firebase; desktop builds remain playable as guests. */
interface AuthService {
    data class User(val uid: String, val email: String?, val displayName: String?, val isGuest: Boolean)
    data class PlayerScore(val name: String, val score: Int, val level: Int)
    data class Progress(val score:Int, val level:Int, val xp:Int, val coins:Int, val runs:Int, val distance:Int, val orbs:Int)
    fun currentUser(): User?
    fun promptInput(title: String, hint: String, initial: String = "", secure: Boolean = false, callback: (String?) -> Unit)
    fun signUp(name: String, email: String, password: String, callback: (Result<User>) -> Unit)
    fun login(email: String, password: String, callback: (Result<User>) -> Unit)
    fun googleSignIn(callback: (Result<User>) -> Unit)
    fun resetPassword(email: String, callback: (Result<Unit>) -> Unit)
    fun continueAsGuest(name: String, callback: (Result<User>) -> Unit)
    fun logout(callback: (Result<Unit>) -> Unit)
    fun updateProfile(name: String, callback: (Result<Unit>) -> Unit)
    fun syncProgress(name: String, score: Int, level: Int, xp: Int, coins: Int, runs: Int, distance: Int, orbs: Int)
    fun restoreProgress(callback: (Result<Progress?>) -> Unit)
    fun loadLeaderboard(callback: (Result<List<PlayerScore>>) -> Unit)
}

class GuestAuthService : AuthService {
    private var user: AuthService.User? = null
    override fun currentUser() = user
    override fun promptInput(title:String,hint:String,initial:String,secure:Boolean,callback:(String?)->Unit) { com.badlogic.gdx.Gdx.input.getTextInput(object:com.badlogic.gdx.Input.TextInputListener { override fun input(text:String)=callback(text); override fun canceled()=callback(null) },title,initial,hint) }
    override fun signUp(name:String,email:String,password:String,callback:(Result<AuthService.User>)->Unit) = callback(Result.failure(IllegalStateException("Online accounts are available in the Android build after Firebase setup.")))
    override fun login(email:String,password:String,callback:(Result<AuthService.User>)->Unit) = callback(Result.failure(IllegalStateException("Online login is available in the Android build after Firebase setup.")))
    override fun googleSignIn(callback:(Result<AuthService.User>)->Unit) = callback(Result.failure(IllegalStateException("Google Sign-In is available in the Android build after Firebase setup.")))
    override fun resetPassword(email:String,callback:(Result<Unit>)->Unit) = callback(Result.failure(IllegalStateException("Password reset requires Firebase.")))
    override fun continueAsGuest(name:String,callback:(Result<AuthService.User>)->Unit) { val u=AuthService.User("guest-local",null,name,true); user=u; callback(Result.success(u)) }
    override fun logout(callback:(Result<Unit>)->Unit) { user=null; callback(Result.success(Unit)) }
    override fun updateProfile(name:String,callback:(Result<Unit>)->Unit) = callback(Result.success(Unit))
    override fun syncProgress(name:String,score:Int,level:Int,xp:Int,coins:Int,runs:Int,distance:Int,orbs:Int) {}
    override fun restoreProgress(callback:(Result<AuthService.Progress?>)->Unit) = callback(Result.success(null))
    override fun loadLeaderboard(callback:(Result<List<AuthService.PlayerScore>>)->Unit) = callback(Result.success(emptyList()))
}
