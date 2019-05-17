import java.util.*
import kotlin.math.abs

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/

data class Location(val x: Int, val y: Int)
data class Cell(val x: Int, val y: Int, val ownership: Int, var piece: Piece? = null) {
    // doesn't account for holes in the map!
    fun distance(other: Cell) = abs(x - other.x) + abs(y - other.y)
    fun distance(xx: Int, yy: Int) = abs(x - xx) + abs(y - yy)
}
data class Piece(val id: Int, val isFriendly: Boolean, val level: Int)
interface IAction
data class MoveAction(val id: Int, val newX: Int, val newY: Int): IAction {
    override fun toString() = "MOVE $id $newX $newY"
}
data class TrainAction(val level: Int, val newX: Int, val newY: Int): IAction {
    override fun toString() = "TRAIN $level $newX $newY"
}

fun main(args : Array<String>) {
    val input = Scanner(System.`in`)
    val mines = List(input.nextInt()) {
        val x = input.nextInt()
        val y = input.nextInt()
        Location(x,y)
    }

    // game loop
    while (true) {
        val gold = input.nextInt()
        val income = input.nextInt()
        val opponentGold = input.nextInt()
        val opponentIncome = input.nextInt()

        val board: List<List<Cell?>> = List(12) { y ->
            input.next().mapIndexed { x: Int, ch ->
                val type = when (ch) {
                    '#' -> null
                    '.' -> 0
                    'O' -> 2; 'o' -> 1
                    'X' -> -2; 'x' -> -1
                    else -> throw Exception("Unexpected")
                }
                type?.let { Cell(x, y, it) }
            }
        }

        // Gives us a flat list of the non-void cells
        val boardCells = board.flatten().filterNotNull()

        lateinit var myHQ: Cell
        lateinit var enemyHQ: Cell

        val buildingCount = input.nextInt()
        for (i in 0 until buildingCount) {
            val owner = input.nextInt()
            val buildingType = input.nextInt()
            val x = input.nextInt()
            val y = input.nextInt()
            if (buildingType == 0) {
                // HQ
                val hq = board[y][x]!!
                when (owner) {
                    0 -> myHQ = hq
                    1 -> enemyHQ = hq
                }
            }
        }

        repeat(input.nextInt()) {
            val owner = input.nextInt()
            val unitId = input.nextInt()
            val level = input.nextInt()
            val x = input.nextInt()
            val y = input.nextInt()
            val cell = board[y][x]!!
            cell.piece = Piece(unitId, owner == 0, level)
        }

        val actions = mutableListOf<IAction>()

        // Move all existing units towards middle
        val myPieces = boardCells.filter { it.piece?.isFriendly == true }
        myPieces.forEach { pieceCell ->
            val target = boardCells
                .filterNot { it == pieceCell }
                .filter { it.piece == null }
                .minBy { it.distance(5,5) }!!
            actions += MoveAction(pieceCell.piece!!.id, target.x, target.y)
            target.piece = pieceCell.piece
            pieceCell.piece = null
        }

        val trainingSpot = boardCells
            .firstOrNull { it.distance(myHQ) == 1 && it.piece == null }

        // If we have enough cash, build a new piece!
        if (gold > 30 && trainingSpot != null) {
            actions += TrainAction(1, trainingSpot.x, trainingSpot.y)
        }

        if (actions.any()) {
            println(actions.joinToString(";"))
        } else {
            println("WAIT")
        }
    }
}