package app.revanced.patches.youtube.interaction.noshortsscroll

import app.revanced.patcher.extensions.addInstructionsWithLabels
import app.revanced.patcher.patch.bytecodePatch

@Suppress("unused")
val disableDoomscrollingPatch = bytecodePatch(
    name = "Disable doomscrolling",
    description = "Disables vertical scrolling in YouTube Shorts, preventing doomscrolling while retaining interactivity.",
) {
    apply {
        reelLinearLayoutManagerCanScrollVerticallyMethodMatch.method.addInstructionsWithLabels(
            0,
            """
                const/4 v0, 0x0
                return v0
            """
        )
    }
}
