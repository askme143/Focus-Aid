package com.yeongil.digitalwellbeing.background

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.SystemClock
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.yeongil.digitalwellbeing.data.rule.Rule
import com.yeongil.digitalwellbeing.dataSource.SequenceNumber
import com.yeongil.digitalwellbeing.dataSource.ruleDatabase.RuleDatabase
import com.yeongil.digitalwellbeing.repository.RuleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class TimeTriggerService : LifecycleService() {
    private val ruleRepo by lazy {
        RuleRepository(
            SequenceNumber(this),
            RuleDatabase.getInstance(this).ruleDao()
        )
    }
    private val alarmManager by lazy { getSystemService(ALARM_SERVICE) as AlarmManager }
    private val pendingIntent by lazy {
        Intent(this, TimeTriggerService::class.java).let {
            PendingIntent.getService(this, 0, it, 0)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        lifecycleScope.launch(Dispatchers.Default) {
            val rules = ruleRepo.getActiveRuleList()
            val (triggeredSet, minInterval) = checkTimeRules(rules)

            /* Set alarm manager */
            if (minInterval == null) {
                alarmManager.cancel(pendingIntent)
            } else {
                alarmManager.cancel(pendingIntent)
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + minInterval * 1000,
                    pendingIntent
                )
            }

            /* Pass results to MainService */
            val mainIntent = Intent(this@TimeTriggerService, MainService::class.java)
            val triggeredSetStr = Json.encodeToString(triggeredSet)
            mainIntent.putExtra(MainService.TIME_TRIGGERED_RUES_KEY, triggeredSetStr)
            startService(mainIntent)
        }

        return START_REDELIVER_INTENT
    }

    private fun checkTimeRules(rules: List<Rule>): Pair<Set<Int>, Int?> {
        val timeRules = rules.filter { it.timeTrigger != null }
        if (timeRules.isEmpty()) return Pair(emptySet(), null)

        val curr = System.currentTimeMillis() / 1000 / 60

        val checkResults = timeRules.map {
            with(it.timeTrigger!!) {
                val triggered = curr in startTimeInMinutes until endTimeInMinutes
                val timeRemaining = when {
                    curr < startTimeInMinutes -> (startTimeInMinutes - curr) / 1000
                    curr < endTimeInMinutes -> (endTimeInMinutes - curr) / 1000
                    else -> startTimeInMinutes + 24 * 60 - curr
                }
                Triple(triggered, it.ruleInfo.ruleId, timeRemaining)
            }
        }
        val triggeredSet = checkResults.filter { it.first }.map { it.second }.toSet()
        val minInterval = checkResults.minOf { it.third.toInt() }

        return Pair(triggeredSet, minInterval)
    }
}