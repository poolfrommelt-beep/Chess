package com.grandmasteredge.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.grandmasteredge.app.ui.HomeScreen
import com.grandmasteredge.kids.ui.KidsScreen
import com.grandmasteredge.multiplayer.ui.MultiplayerScreen
import com.grandmasteredge.singleplayer.ui.SinglePlayerScreen
import com.grandmasteredge.tactics.ui.TacticsScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var currentScreen by remember { mutableStateOf("home") }

            MaterialTheme {
                Scaffold(
                    bottomBar = {
                        if (currentScreen != "home") {
                            NavigationBar {
                                NavigationBarItem(
                                    icon = { Text("🏠") },
                                    label = { Text("Home") },
                                    selected = currentScreen == "home",
                                    onClick = { currentScreen = "home" }
                                )
                                NavigationBarItem(
                                    icon = { Text("🧩") },
                                    label = { Text("Tactics") },
                                    selected = currentScreen == "tactics",
                                    onClick = { currentScreen = "tactics" }
                                )
                                NavigationBarItem(
                                    icon = { Text("👥") },
                                    label = { Text("Multiplayer") },
                                    selected = currentScreen == "multiplayer",
                                    onClick = { currentScreen = "multiplayer" }
                                )
                                NavigationBarItem(
                                    icon = { Text("👶") },
                                    label = { Text("Kids") },
                                    selected = currentScreen == "kids",
                                    onClick = { currentScreen = "kids" }
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        when (currentScreen) {
                            "home" -> HomeScreen(
                                onNavigateToSinglePlayer = { currentScreen = "singleplayer" },
                                onNavigateToMultiplayer = { currentScreen = "multiplayer" },
                                onNavigateToTactics = { currentScreen = "tactics" },
                                onNavigateToKids = { currentScreen = "kids" }
                            )
                            "singleplayer" -> SinglePlayerScreen()
                            "tactics" -> TacticsScreen()
                            "multiplayer" -> MultiplayerScreen()
                            "kids" -> KidsScreen()
                        }
                    }
                }
            }
        }
    }
}
