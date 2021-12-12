package com.gmail.noxdawn.control

interface Explosive {
    val isTriggered: Boolean
    fun explode()
}

interface ExplosiveCollector {
    fun getExplosives(): Iterable<Explosive>
}

class ExplosionController(
    private val explosiveCollector: ExplosiveCollector
) : Controller {
    override val period = 2

    override fun activate() {
        for (explosive in explosiveCollector.getExplosives()) {
            if (explosive.isTriggered) {
                explosive.explode()
            }
        }
    }
}
