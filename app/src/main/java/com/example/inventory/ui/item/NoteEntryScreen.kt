package com.example.inventory.ui.item

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inventory.InventoryTopAppBar
import com.example.inventory.R
import com.example.inventory.ui.AppViewModelProvider
import com.example.inventory.ui.theme.InventoryTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteEntryScreen(
    navigateBack: () -> Unit,
    viewModel: NoteEntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val noteUiState by viewModel.noteUiState.collectAsState()

    Scaffold(
        topBar = {
            InventoryTopAppBar(
                title = stringResource(R.string.note_entry_title),
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
            ) {
                OutlinedTextField(
                    value = noteUiState.noteDetails.title,
                    onValueChange = { newTitle: String -> viewModel.updateTitle(newTitle) },
                    label = { Text("Title") },
                    modifier = Modifier.padding(16.dp)
                )
                OutlinedTextField(
                    value = noteUiState.noteDetails.content,
                    onValueChange = { newContent: String -> viewModel.updateContent(newContent) },
                    label = { Text("Content") },
                    modifier = Modifier.padding(16.dp)
                )
                Button(
                    onClick = {
                        viewModel.saveNote()
                        navigateBack()
                    },
                    enabled = noteUiState.isEntryValid,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text("Save Note")
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun NoteEntryScreenPreview() {
    InventoryTheme {
        NoteEntryScreen(navigateBack = { /*Do nothing*/ })
    }
}
