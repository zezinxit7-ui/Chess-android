package com.chessandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Ink = Color(0xFF090B0A)
private val Panel = Color(0xFF171B18)
private val Ivory = Color(0xFFE8E5DC)
private val Green = Color(0xFF718574)
private val Gold = Color(0xFFC5A66A)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(colorScheme = darkColorScheme(
                background = Ink, surface = Panel, primary = Gold,
                onBackground = Ivory, onSurface = Ivory
            )) { ChessScreen() }
        }
    }
}

private fun startPosition(): List<Char?> {
    val rows = listOf("rnbqkbnr", "pppppppp", "........", "........",
        "........", "........", "PPPPPPPP", "RNBQKBNR")
    return rows.flatMap { row -> row.map { if (it == '.') null else it } }
}

@Composable
private fun ChessScreen() {
    var board by remember { mutableStateOf(startPosition()) }
    var selected by remember { mutableStateOf<Int?>(null) }
    var flipped by remember { mutableStateOf(false) }
    var moves by remember { mutableIntStateOf(0) }

    Surface(Modifier.fillMaxSize(), color = Ink) {
        Column(
            Modifier.fillMaxSize().padding(horizontal = 20.dp, vertical = 26.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(7.dp)) {
                Text("CHESS", color = Gold, fontSize = 12.sp,
                    fontWeight = FontWeight.Bold, letterSpacing = 4.sp)
                Text("O jogo, sem distrações.", color = Ivory, fontSize = 25.sp,
                    fontWeight = FontWeight.SemiBold)
                Text("PARTIDA LOCAL  /  PROTÓTIPO", color = Color(0xFF858D86),
                    fontSize = 10.sp, letterSpacing = 1.5.sp)
            }

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(if (flipped) "PRETAS EMBAIXO" else "BRANCAS EMBAIXO",
                        color = Color(0xFFB7BDB7), fontSize = 10.sp, letterSpacing = 1.sp)
                    Text("$moves LANCES", color = Color(0xFF858D86),
                        fontSize = 10.sp, letterSpacing = 1.sp)
                }
                BoardView(board, selected, flipped) { index ->
                    val from = selected
                    if (from == null) {
                        if (board[index] != null) selected = index
                    } else if (from == index) {
                        selected = null
                    } else {
                        val moving = board[from]
                        val target = board[index]
                        if (moving != null && target != null &&
                            moving.isUpperCase() == target.isUpperCase()) {
                            selected = index
                        } else {
                            val next = board.toMutableList()
                            next[index] = moving
                            next[from] = null
                            board = next
                            selected = null
                            moves++
                        }
                    }
                }
                Text("Selecione uma peça e toque na casa de destino.",
                    color = Color(0xFF858D86), fontSize = 11.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally))
            }

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Control("REINICIAR", Modifier.weight(1f)) {
                    board = startPosition(); selected = null; moves = 0
                }
                Control("INVERTER", Modifier.weight(1f)) {
                    flipped = !flipped; selected = null
                }
            }
            Text("0.1.0  ·  REGRAS E MOTOR EM IMPLEMENTAÇÃO",
                color = Color(0xFF555D56), fontSize = 9.sp, letterSpacing = 1.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally))
        }
    }
}

@Composable
private fun BoardView(
    board: List<Char?>, selected: Int?, flipped: Boolean,
    onTap: (Int) -> Unit
) {
    Column(Modifier.fillMaxWidth().aspectRatio(1f)) {
        repeat(8) { displayRow ->
            Row(Modifier.weight(1f)) {
                repeat(8) { displayCol ->
                    val row = if (flipped) 7 - displayRow else displayRow
                    val col = if (flipped) 7 - displayCol else displayCol
                    val index = row * 8 + col
                    val piece = board[index]
                    val base = if ((row + col) % 2 == 0) Ivory else Green
                    val square = if (selected == index) Gold else base
                    Box(
                        Modifier.weight(1f).aspectRatio(1f)
                            .background(square)
                            .clickable { onTap(index) },
                        contentAlignment = Alignment.Center
                    ) {
                        if (piece != null) {
                            Text(pieceSymbol(piece),
                                color = if (piece.isUpperCase()) Color(0xFFFAF8F0) else Color(0xFF171A18),
                                fontSize = 29.sp, fontWeight = FontWeight.Normal)
                        }
                    }
                }
            }
        }
    }
}

private fun pieceSymbol(piece: Char): String = when (piece.lowercaseChar()) {
    'k' -> "♚"
    'q' -> "♛"
    'r' -> "♜"
    'b' -> "♝"
    'n' -> "♞"
    'p' -> "♟"
    else -> ""
}

@Composable
private fun Control(label: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Surface(modifier.clickable(onClick = onClick), color = Panel,
        shape = RoundedCornerShape(12.dp)) {
        Box(Modifier.padding(vertical = 16.dp), contentAlignment = Alignment.Center) {
            Text(label, color = Ivory, fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold, letterSpacing = 1.2.sp)
        }
    }
}
