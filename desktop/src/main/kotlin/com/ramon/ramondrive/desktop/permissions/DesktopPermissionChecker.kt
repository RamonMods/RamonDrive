package com.ramon.ramondrive.desktop.permissions

import com.ramon.ramondrive.core.permissions.AppPermission
import com.ramon.ramondrive.core.permissions.PermissionChecker

class DesktopPermissionChecker : PermissionChecker {

    override fun isGranted(permission: AppPermission): Boolean = true

    override fun hasAllFilesAccess(): Boolean = true

    override fun statuses(): Map<AppPermission, Boolean> =
        AppPermission.entries.associateWith { true }

    override fun missingCritical(): List<AppPermission> = emptyList()

    override fun isRequestable(permission: AppPermission): Boolean = false
}
