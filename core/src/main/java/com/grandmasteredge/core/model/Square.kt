package com.grandmasteredge.core.model

data class Square(
    val file: Int, // 0..7 (a..h)
    val rank: Int  // 0..7 (1..8)
) {
    init {
        require(file in 0..7) { "File must be in 0..7" }
        require(rank in 0..7) { "Rank must be in 0..7" }
    }

    override fun toString(): String {
        val fileChar = 'a' + file
        val rankChar = '1' + rank
        return "$fileChar$rankChar"
    }

    companion object {
        fun fromString(s: String): Square {
            require(s.length == 2)
            val file = s[0] - 'a'
            val rank = s[1] - '1'
            return Square(file, rank)
        }
    }
}
