package app.revanced.patches.youtube.interaction.noshortsscroll

import app.revanced.patcher.extensions.addInstructionsWithLabels
import app.revanced.patcher.patch.bytecodePatch

@Suppress("unused")
val disableDoomscrollingPatch = bytecodePatch(
    name = "Disable doomscrolling",
    description = "Disables vertical scrolling in YouTube Shorts, preventing doomscrolling while retaining interactivity.",
) {
    compatibleWith(
        "com.google.android.youtube"(
            "20.14.43",
            "20.21.37",
            "20.26.46",
            "20.31.42",
            "20.37.48",
            "20.40.45"
        ),
    )

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
