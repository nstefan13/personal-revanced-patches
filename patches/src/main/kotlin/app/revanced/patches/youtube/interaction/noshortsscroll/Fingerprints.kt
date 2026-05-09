package app.revanced.patches.youtube.interaction.noshortsscroll

import app.revanced.patcher.composingFirstMethod
import app.revanced.patcher.definingClass
import app.revanced.patcher.opcodes
import app.revanced.patcher.parameterTypes
import app.revanced.patcher.returnType
import app.revanced.patcher.patch.BytecodePatchContext
import com.android.tools.smali.dexlib2.Opcode

internal val BytecodePatchContext.reelLinearLayoutManagerCanScrollVerticallyMethodMatch by composingFirstMethod {
    definingClass("Lcom/google/android/libraries/youtube/reel/internal/pager/ReelLinearLayoutManager;")
    returnType("Z")
    parameterTypes()
    opcodes(
        Opcode.IGET_BOOLEAN,
        Opcode.IF_EQZ,
        Opcode.IGET_OBJECT,
        Opcode.IGET_BOOLEAN,
        Opcode.RETURN,
        Opcode.INVOKE_SUPER,
        Opcode.MOVE_RESULT,
        Opcode.RETURN
    )
}
