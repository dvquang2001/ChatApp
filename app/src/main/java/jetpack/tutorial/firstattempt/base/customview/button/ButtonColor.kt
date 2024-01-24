package jetpack.tutorial.firstattempt.base.customview.button

import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import jetpack.tutorial.firstattempt.presentation.ui.theme.AbsWhite
import jetpack.tutorial.firstattempt.presentation.ui.theme.InfoMain
import jetpack.tutorial.firstattempt.presentation.ui.theme.Neutral10
import jetpack.tutorial.firstattempt.presentation.ui.theme.Neutral70
import jetpack.tutorial.firstattempt.presentation.ui.theme.Neutral90


object ButtonColor {
    @Composable
    fun buttonColorBlue(): ButtonColors {
        return ButtonDefaults.buttonColors(
            containerColor = InfoMain,
            contentColor = Neutral10,
            disabledContainerColor = InfoMain,
            disabledContentColor = Neutral10
        )
    }

    @Composable
    fun textButtonColor(): ButtonColors {
        return ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Neutral90,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = Neutral90
        )
    }

    @Composable
    fun outlinedButtonColorBlue(): ButtonColors {
        return ButtonDefaults.outlinedButtonColors(
            containerColor = AbsWhite,
            contentColor = InfoMain,
            disabledContentColor = Neutral70
        )
    }
}