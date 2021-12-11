package com.gmail.noxdawn.control

import java.util.*

data class BombState(
    val id: UUID,
    val count: Int,
    val shouldExplode: Boolean,
    val power: Double,
)

interface BombCollector {
    fun getBombs(): Iterable<BombState>
}

interface BombStateApplier {
    fun apply(currentState: BombState, nextState: BombState)
}

class BombController(
    private val bombCollector: BombCollector, private val bombStateApplier: BombStateApplier
) : Controller {
    override val period: Int = 2

    override fun activate() {
        for (bomb in bombCollector.getBombs()) {
            var nextCount = if (bomb.count > 0) bomb.count - 1 else bomb.count
            val shouldExplode = nextCount == 0
            if (shouldExplode) {
                nextCount = -1
            }
            val nextState = bomb.copy(shouldExplode = shouldExplode, count = nextCount)
            bombStateApplier.apply(bomb, nextState)
        }
    }
}
