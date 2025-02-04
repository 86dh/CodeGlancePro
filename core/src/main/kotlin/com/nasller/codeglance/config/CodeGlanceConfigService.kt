package com.nasller.codeglance.config

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.SimplePersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.nasller.codeglance.util.Util
import kotlinx.coroutines.CoroutineScope

@State(name = Util.PLUGIN_NAME, storages = [Storage("CodeGlancePro.xml")])
class CodeGlanceConfigService(val coroutineScope: CoroutineScope) : SimplePersistentStateComponent<CodeGlanceConfig>(CodeGlanceConfig()) {
	companion object {
		val Config by lazy { ApplicationManager.getApplication().getService(CodeGlanceConfigService::class.java).state }
	}
}