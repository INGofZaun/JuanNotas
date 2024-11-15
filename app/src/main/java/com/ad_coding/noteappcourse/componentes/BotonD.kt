package com.ad_coding.noteappcourse.componentes

import android.widget.Switch
import android.widget.ToggleButton
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun BotonD(modifier : Modifier = Modifier){
    var checked by remember { mutableStateOf(true) }
    var Titulo by remember { mutableStateOf("NOTAS") }
    var bandera: Int
    Row(
        modifier = Modifier.fillMaxWidth().padding(5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = Titulo, style = MaterialTheme.typography.titleLarge)
        Column {
            Text(text = Titulo, style = MaterialTheme.typography.titleSmall)
            Switch(
                checked = checked,
                onCheckedChange = {
                    checked = it
                    if (it==true){
                        Titulo = "NOTAS"
                        bandera = 0
                    }else
                    {
                        Titulo = "TAREAS"}
                    bandera = 1
                }
            )
        }
    }

}


