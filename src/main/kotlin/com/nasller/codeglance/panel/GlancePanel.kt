package com.nasller.codeglance.panel

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.FoldRegion
import com.intellij.openapi.editor.ex.FoldingListener
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.util.ReadTask
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.nasller.codeglance.render.Minimap
import java.awt.image.BufferedImage
import java.lang.ref.SoftReference

/**
 * This JPanel gets injected into editor windows and renders a image generated by GlanceFileRenderer
 */
class GlancePanel(project: Project, textEditor: TextEditor) : AbstractGlancePanel<Minimap>(project,textEditor) {
    init {
        scrollbar = Scrollbar(textEditor, scrollState,this)
        Disposer.register(textEditor, this)
        val foldListener = object : FoldingListener {
            override fun onFoldProcessingEnd() = updateImage()

            override fun onFoldRegionStateChange(region: FoldRegion) = updateImage()
        }
        editor.foldingModel.addListener(foldListener, this)
        add(scrollbar)
        refresh()
    }

    override val updateTask: ReadTask
        get() = object :ReadTask() {
            override fun onCanceled(indicator: ProgressIndicator) {
                renderLock.release()
                renderLock.clean()
                updateImageSoon()
            }

            override fun computeInReadAction(indicator: ProgressIndicator) {
                val map = getOrCreateMap()
                try {
                    map.update(editor, scrollState, indicator)
                    scrollState.computeDimensions(editor, config)
                    ApplicationManager.getApplication().invokeLater {
                        scrollState.recomputeVisible(editor.scrollingModel.visibleArea)
                        repaint()
                    }
                }finally {
                    renderLock.release()
                    if (renderLock.dirty) {
                        renderLock.clean()
                        updateImageSoon()
                    }
                }
            }
        }

    // the minimap is held by a soft reference so the GC can delete it at any time.
    // if its been deleted and we want it again (active tab) we recreate it.
    private fun getOrCreateMap() : Minimap {
        var map = mapRef.get()
        if (map == null) {
            map = Minimap(config)
            mapRef = SoftReference(map)
        }
        return map
    }

    override fun getImgBuff(): BufferedImage {
        return getOrCreateMap().img!!
    }
}