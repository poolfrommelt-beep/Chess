package com.grandmasteredge.core.model

data class Move(
    val from: Square,
    val to: Square,
    val promotion: PieceType? = null
) {
    override fun toString(): String {
        val promo = when (promotion) {
            PieceType.QUEEN -> "q"
            PieceType.ROOK -> "r"
            PieceType.BISHOP -> "b"
            PieceType.KNIGHT -> "n"
            else -> ""
        }
        return "$from$to$promo"
    }
}
