package com.ad_coding.noteappcourse.componentes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ad_coding.noteappcourse.domain.model.Note
import com.ad_coding.noteappcourse.ui.theme.*


@Composable
fun NotaItem(Nota : Note,
             onNoteClick: (Note) -> Unit,
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .clickable(onClick = { onNoteClick(Nota) })

    ){
        Row {
            Text(
                text = Nota.fecha,
                modifier = Modifier
                    .padding(16.dp),
                textAlign = TextAlign.Start,

                )
        }
        Text(
            text = Nota.title,
            modifier = Modifier
                .padding(16.dp),
            textAlign = TextAlign.Start,

        )
        Text(
            text = Nota.content,
            modifier = Modifier
                .padding(16.dp),
            textAlign = TextAlign.Center,

            )
    }
}