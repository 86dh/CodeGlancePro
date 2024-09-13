package com.nasller.codeglance.config

import com.intellij.openapi.fileTypes.FileTypes
import com.intellij.openapi.fileTypes.PlainSyntaxHighlighter
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import com.intellij.psi.codeStyle.DisplayPriority
import com.intellij.psi.codeStyle.DisplayPrioritySortable
import com.nasller.codeglance.util.Util
import javax.swing.Icon

class CodeGlanceColorsPage : ColorSettingsPage, DisplayPrioritySortable {
	override fun getAttributeDescriptors() = arrayOf(
		AttributesDescriptor("Class name", Util.MARK_CLASS_ATTRIBUTES),
		AttributesDescriptor("Mark comment", Util.MARK_COMMENT_ATTRIBUTES),
	)

	override fun getColorDescriptors(): Array<ColorDescriptor> = emptyArray()

	override fun getDisplayName() = Util.PLUGIN_NAME

	override fun getIcon(): Icon = FileTypes.PLAIN_TEXT.icon

	override fun getHighlighter() = PlainSyntaxHighlighter()

	override fun getDemoText() = """
		class <class>MyClass</class> {}
		//<mark>This is a comment</mark>
		""".trimIndent()

	override fun getAdditionalHighlightingTagToDescriptorMap() = mapOf(
		Pair("class", Util.MARK_CLASS_ATTRIBUTES),
		Pair("mark", Util.MARK_COMMENT_ATTRIBUTES),
	)

	override fun getPriority() = DisplayPriority.OTHER_SETTINGS
}