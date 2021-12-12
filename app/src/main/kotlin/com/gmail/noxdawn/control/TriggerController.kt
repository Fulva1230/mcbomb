package com.gmail.noxdawn.control

interface Trigger {
    val isTriggered: Boolean
    fun trigger()
}

interface TriggerCollector {
    fun getTriggers(): Iterable<Trigger>
}

class TriggerController(
    private val triggerCollectors: List<TriggerCollector>
) : Controller {
    override val period = 2

    override fun activate() {
        for (triggerCollector in triggerCollectors) {
            for (trigger in triggerCollector.getTriggers()) {
                if (trigger.isTriggered) {
                    trigger.trigger()
                }
            }
        }
    }
}
