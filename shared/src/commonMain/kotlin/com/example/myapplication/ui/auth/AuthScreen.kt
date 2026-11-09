package com.example.myapplication.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.myapplication.domain.model.AuthError
import com.example.myapplication.domain.usecase.FieldError
import com.example.myapplication.generated.resources.*
import com.example.myapplication.ui.components.PaperSheet
import com.example.myapplication.ui.theme.LocalPaper
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
private fun FieldError?.asText(isPassword: Boolean): String? = when (this) {
    null -> null
    FieldError.EMPTY -> stringResource(Res.string.error_required)
    FieldError.TOO_SHORT -> stringResource(
        if (isPassword) Res.string.error_password_short else Res.string.error_username_short
    )
    FieldError.INVALID_CHARS -> stringResource(Res.string.error_username_chars)
    FieldError.WEAK_PASSWORD -> stringResource(Res.string.error_password_weak)
}

private fun AuthError.messageRes(): StringResource = when (this) {
    AuthError.INVALID_CREDENTIALS -> Res.string.auth_error_invalid_credentials
    AuthError.NETWORK -> Res.string.auth_error_network
    AuthError.SERVER -> Res.string.auth_error_server
    AuthError.UNAUTHORIZED, AuthError.UNKNOWN -> Res.string.auth_error_unknown
}

@Composable
fun AuthScreen(
    onAuthorized: () -> Unit,
    viewModel: AuthViewModel = koinViewModel()
) {
    val paper = LocalPaper.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                AuthEffect.NavigateToHome -> onAuthorized()
                is AuthEffect.ShowError -> {
                    val message = getString(effect.error.messageRes())
                    snackbarHostState.currentSnackbarData?.dismiss()
                    scope.launch { snackbarHostState.showSnackbar(message) }
                }
            }
        }
    }

    val fieldColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = paper.ink,
        unfocusedTextColor = paper.ink,
        focusedBorderColor = paper.ink,
        unfocusedBorderColor = paper.rule,
        focusedLabelColor = paper.ink,
        unfocusedLabelColor = paper.ink.copy(alpha = 0.7f),
        cursorColor = paper.ink
    )
    val usernameMessage = state.usernameError.asText(isPassword = false)
    val passwordMessage = state.passwordError.asText(isPassword = true)

    Box(Modifier.fillMaxSize().background(paper.desk)) {
        Column(
            Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Spacer(Modifier.height(32.dp))
            PaperSheet(
                Modifier.fillMaxWidth().widthIn(max = 440.dp).align(Alignment.CenterHorizontally)
            ) {
                Spacer(Modifier.height(24.dp))
                Text(
                    stringResource(Res.string.auth_title),
                    style = MaterialTheme.typography.headlineMedium,
                    color = paper.ink
                )
                Text(
                    stringResource(Res.string.auth_subtitle),
                    style = MaterialTheme.typography.bodyMedium,
                    color = paper.ink.copy(alpha = 0.75f)
                )
                Spacer(Modifier.height(20.dp))

                OutlinedTextField(
                    value = state.username,
                    onValueChange = { viewModel.onIntent(AuthIntent.UsernameChanged(it)) },
                    label = { Text(stringResource(Res.string.username)) },
                    singleLine = true,
                    enabled = !state.isLoading,
                    isError = usernameMessage != null,
                    supportingText = { usernameMessage?.let { Text(it) } },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    colors = fieldColors,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = state.password,
                    onValueChange = { viewModel.onIntent(AuthIntent.PasswordChanged(it)) },
                    label = { Text(stringResource(Res.string.password)) },
                    singleLine = true,
                    enabled = !state.isLoading,
                    isError = passwordMessage != null,
                    supportingText = { passwordMessage?.let { Text(it) } },
                    visualTransformation = if (state.isPasswordVisible)
                        VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        TextButton(onClick = { viewModel.onIntent(AuthIntent.TogglePasswordVisibility) }) {
                            Text(
                                stringResource(
                                    if (state.isPasswordVisible) Res.string.hide else Res.string.show
                                ),
                                color = paper.ink
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                            viewModel.onIntent(AuthIntent.SignIn)
                        }
                    ),
                    colors = fieldColors,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = {
                        focusManager.clearFocus()
                        viewModel.onIntent(AuthIntent.SignIn)
                    },
                    enabled = !state.isLoading,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = paper.ink,
                        contentColor = paper.paper
                    )
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(22.dp),
                            strokeWidth = 2.dp,
                            color = paper.paper
                        )
                    } else {
                        Text(stringResource(Res.string.sign_in))
                    }
                }
                Spacer(Modifier.height(24.dp))
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter).safeDrawingPadding().padding(16.dp)
        )
    }
}