package com.wahid.wurly.presentation.screen.alerts.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.wahid.wurly.R

/**
 * Full-width orange "Add Weather Alert" button with a ⊕ icon.
 *
 * Stateless — the click callback is hoisted.
 */
@Composable
fun AddAlertButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val cornerRadius = dimensionResource(R.dimen.alerts_add_button_corner_radius)
    val height = dimensionResource(R.dimen.alerts_add_button_height)

    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(height),
        shape = RoundedCornerShape(cornerRadius),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = Color.White,
        ),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Filled.AddCircleOutline,
                contentDescription = stringResource(R.string.alerts_add_button_cd),
                modifier = Modifier.size(dimensionResource(R.dimen.alerts_row_icon_size)),
            )
            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.alerts_row_icon_spacing)))
            Text(
                text = stringResource(R.string.alerts_add_button),
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                ),
            )
        }
    }
}