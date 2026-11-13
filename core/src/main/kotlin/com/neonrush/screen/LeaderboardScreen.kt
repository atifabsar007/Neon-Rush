package com.neonrush.screen

import com.badlogic.gdx.Gdx
import com.neonrush.NeonRushGame

class LeaderboardScreen(game:NeonRushGame):BaseScreen(game) {
    private var rows=emptyList<com.neonrush.service.AuthService.PlayerScore>()
    private var message="LOADING GLOBAL RANKINGS..."
    private var requested=false
    override fun show(){ super.show(); if(!requested){requested=true;game.auth.loadLeaderboard { result -> Gdx.app.postRunnable { result.onSuccess { rows=it; message=if(it.isEmpty()) "NO SCORES YET — BE THE FIRST RUNNER" else "TOP NEON RUNNERS" }.onFailure { message=it.message ?: "COULD NOT LOAD LEADERBOARD" } } } } }
    override fun render(delta:Float){ prepare(delta); painter.beginShapes(); painter.backdrop(); painter.bigPanel(32f,120f,416f,570f); painter.button(78f,132f,324f,58f); painter.endShapes(); painter.beginText(); painter.text("GLOBAL LEADERBOARD",240f,650f,1.8f,painter.cyan(),true); painter.text(message.uppercase().take(48),240f,600f,0.72f,painter.muted(),true)
        rows.take(10).forEachIndexed { i,r -> val y=550f-i*38f; painter.text("${i+1}.",92f,y,1f,painter.pink()); painter.text(r.name.take(18),132f,y,0.95f,painter.white()); painter.text("${r.score}",390f,y,1f,painter.cyan(),true) }
        painter.labelButton("BACK",78f,132f,324f,58f); painter.endText(); tapped()?.let { if(inRect(it,78f,132f,324f,58f)) game.navigate(MenuScreen(game)) }
    }
}
