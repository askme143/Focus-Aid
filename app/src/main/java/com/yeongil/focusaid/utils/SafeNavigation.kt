package com.yeongil.focusaid.utils

import androidx.navigation.NavController
import androidx.navigation.NavDirections

fun NavController.navigateSafe(direction: NavDirections) {
    currentDestination?.getAction(direction.actionId)?.let { navigate(direction) }
}