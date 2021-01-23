package com.yeongil.digitalwellbeing.database.ruleDatabase.dao.rule

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.yeongil.digitalwellbeing.database.ruleDatabase.dao.action.*
import com.yeongil.digitalwellbeing.database.ruleDatabase.dao.trigger.ActivityTriggerDao
import com.yeongil.digitalwellbeing.database.ruleDatabase.dao.trigger.LocationTriggerDao
import com.yeongil.digitalwellbeing.database.ruleDatabase.dao.trigger.TimeTriggerDao
import com.yeongil.digitalwellbeing.database.ruleDatabase.dto.rule.RuleDto

@Dao
interface RuleDao :
    RuleInfoDao,
    LocationTriggerDao, TimeTriggerDao, ActivityTriggerDao,
    AppBlockActionDao, NotificationActionDao, DndActionDao, RingerActionDao {
    @Transaction
    suspend fun insertRule(ruleDto: RuleDto) {
        insertRuleInfo(ruleDto.ruleInfoDto)

        ruleDto.locationTriggerDto?.let { insertLocationTrigger(it) }
        ruleDto.timeTriggerDto?.let { insertTimeTrigger(it) }
        ruleDto.activityTriggerDto?.let { insertActivityTrigger(it) }

        ruleDto.appBlockActionDto?.let { insertAppBlockAction(it) }
        ruleDto.notificationAction?.let { insertNotificationAction(it) }
        ruleDto.dndActionDto?.let { insertDndAction(it) }
        ruleDto.ringerActionDto?.let { insertRingerAction(it) }
    }

    @Transaction
    suspend fun updateRule(ruleDto: RuleDto) {
        updateRuleInfo(ruleDto.ruleInfoDto)

        ruleDto.locationTriggerDto?.let { updateLocationTrigger(it) }
        ruleDto.timeTriggerDto?.let { updateTimeTrigger(it) }
        ruleDto.activityTriggerDto?.let { updateActivityTrigger(it) }

        ruleDto.appBlockActionDto?.let { updateAppBlockAction(it) }
        ruleDto.notificationAction?.let { updateNotificationAction(it) }
        ruleDto.dndActionDto?.let { updateDndAction(it) }
        ruleDto.ringerActionDto?.let { updateRingerAction(it) }
    }

    @Transaction
    suspend fun deleteRuleByRid(rid: Int) {
        deleteRuleInfoByRid(rid)

        deleteLocationTriggerByRid(rid)
        deleteTimeTriggerByRid(rid)
        deleteActivityTriggerByRid(rid)

        deleteAppBlockActionByRid(rid)
        deleteNotificationActionByRid(rid)
        deleteDndActionByRid(rid)
        deleteRingerActionByRid(rid)
    }

    @Transaction
    @Query("SELECT * FROM rule_info")
    suspend fun getRuleList(): List<RuleDto>
}