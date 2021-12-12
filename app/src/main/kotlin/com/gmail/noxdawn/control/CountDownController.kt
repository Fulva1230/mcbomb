package com.gmail.noxdawn.control

interface CountDownTimer {
    fun countDown()
}

interface CountDownTimerCollector {
    fun getTimers(): Iterable<CountDownTimer>
}

class CountDownController(
    private val countDownTimerCollector: CountDownTimerCollector
) : Controller {
    override val period: Int = 2

    override fun activate() {
        for (timer in countDownTimerCollector.getTimers()) {
            timer.countDown()
        }
    }
}
