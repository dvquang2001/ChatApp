package jetpack.tutorial.firstattempt.presentation.main.home

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import jetpack.tutorial.firstattempt.base.customview.search_bar.SearchBar
import jetpack.tutorial.firstattempt.presentation.NavigationItem
import jetpack.tutorial.firstattempt.presentation.main.home.components.HomeAppBar
import jetpack.tutorial.firstattempt.presentation.main.home.components.ListConversation
import jetpack.tutorial.firstattempt.presentation.main.home.components.ListUserOnline
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    navigateToAuthScreen: () -> Unit,
    onItemUserOnlineClicked: (messageId: String) -> Unit,
    onItemConversationClicked: (conversationId: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState(initial = HomeViewModel.ViewState())
    val effect by viewModel.effect.collectAsState(initial = null)
    val context = LocalContext.current

    LaunchedEffect(effect) {
        when (effect) {
            is HomeViewModel.ViewEffect.SignOutSuccess -> {
                navigateToAuthScreen()
            }

            is HomeViewModel.ViewEffect.SignOutFailed -> {
                Toast.makeText(
                    context,
                    (effect as HomeViewModel.ViewEffect.SignOutFailed).message,
                    Toast.LENGTH_LONG
                ).show()
            }

            else -> {}
        }
    }

    val items = listOf(
        NavigationItem(
            title = "All",
            selectedIcon = Icons.Filled.Home,
            unSelectedIcon = Icons.Outlined.Home
        ),
        NavigationItem(
            title = "Urgent",
            selectedIcon = Icons.Filled.Info,
            unSelectedIcon = Icons.Outlined.Info,
            badgeCount = 45
        ),
        NavigationItem(
            title = "Settings",
            selectedIcon = Icons.Filled.Settings,
            unSelectedIcon = Icons.Outlined.Settings
        ),
        NavigationItem(
            title = "Logout",
            selectedIcon = Icons.Filled.ArrowBack,
            unSelectedIcon = Icons.Outlined.ArrowBack
        )
    )

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedItemIndex by rememberSaveable {
        mutableIntStateOf(0)
    }

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(vertical = 64.dp)
                ) {
                    Text(text = "Header", fontSize = 60.sp)
                }
                items.forEachIndexed { index, item ->
                    NavigationDrawerItem(
                        label = {
                            Text(text = item.title)
                        },
                        selected = index == selectedItemIndex,
                        onClick = {
                            selectedItemIndex = index
                            scope.launch {
                                drawerState.close()
                            }
                            if (index == items.lastIndex) {
                                viewModel.onEvent(HomeViewModel.ViewEvent.SignOut)
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = if (index == selectedItemIndex) {
                                    item.selectedIcon
                                } else item.unSelectedIcon,
                                contentDescription = item.title
                            )
                        },
                        badge = {
                            item.badgeCount?.let {
                                Text(text = item.badgeCount.toString())
                            }
                        },
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                    )
                }
            }
        },
        drawerState = drawerState
    ) {
        Scaffold(
        ) { paddingValues ->
            Column(
                verticalArrangement = Arrangement.spacedBy(24.dp),
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                HomeAppBar(
                    openDrawerClicked = {
                        scope.launch {
                            drawerState.open()
                        }
                    }
                )
                SearchBar(
                    searchText = state.searchText,
                    onTextChanged = {
                        //todo
                    }
                )
                ListUserOnline(
                    users = state.users,
                    onItemClicked = { user ->
                        onItemUserOnlineClicked(user.id)
                    }
                )
                ListConversation(
                    conversations = state.conversations,
                    onItemConversationClicked = onItemConversationClicked,
                    modifier = Modifier
                        .weight(1f)
                )
            }
        }
    }
}