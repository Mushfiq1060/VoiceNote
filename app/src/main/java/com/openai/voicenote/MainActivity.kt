package com.openai.voicenote

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.openai.voicenote.core.designsystem.theme.VnTheme
import com.openai.voicenote.core.model.LabelResource
import com.openai.voicenote.ui.VnApp
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.RECORD_AUDIO),
            0
        )

        var uiState: MainActivityUiState by mutableStateOf(MainActivityUiState.Loading)

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState
                    .onEach {
                        uiState = it
                    }
                    .collect()
            }
        }

        setContent {

            VnTheme {
                VnApp(
                    labelItems = when (uiState) {
                        is MainActivityUiState.Success -> { (uiState as MainActivityUiState.Success).labelItems }
                        else -> { listOf<LabelResource>() }
                    }
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.addDataLayerListener()
    }

    override fun onPause() {
        super.onPause()
        viewModel.removeDataLayerListener()
    }
}
