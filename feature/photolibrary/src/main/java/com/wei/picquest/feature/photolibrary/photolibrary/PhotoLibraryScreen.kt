package com.wei.picquest.feature.photolibrary.photolibrary

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.wei.picquest.core.designsystem.component.FunctionalityNotAvailablePopup
import com.wei.picquest.core.designsystem.component.ThemePreviews
import com.wei.picquest.core.network.model.NetworkImageDetail
import com.wei.picquest.feature.photolibrary.R

/**
 *
 * UI 事件決策樹
 * 下圖顯示了一個決策樹，用於查找處理特定事件用例的最佳方法。
 *
 *                                                      ┌───────┐
 *                                                      │ Start │
 *                                                      └───┬───┘
 *                                                          ↓
 *                                       ┌───────────────────────────────────┐
 *                                       │ Where is event originated?        │
 *                                       └──────┬─────────────────────┬──────┘
 *                                              ↓                     ↓
 *                                              UI                  ViewModel
 *                                              │                     │
 *                           ┌─────────────────────────┐      ┌───────────────┐
 *                           │ When the event requires │      │ Update the UI │
 *                           │ ...                     │      │ State         │
 *                           └─┬─────────────────────┬─┘      └───────────────┘
 *                             ↓                     ↓
 *                        Business logic      UI behavior logic
 *                             │                     │
 *     ┌─────────────────────────────────┐   ┌──────────────────────────────────────┐
 *     │ Delegate the business logic to  │   │ Modify the UI element state in the   │
 *     │ the ViewModel                   │   │ UI directly                          │
 *     └─────────────────────────────────┘   └──────────────────────────────────────┘
 *
 *
 */
@Composable
internal fun PhotoLibraryRoute(
    navController: NavController,
    viewModel: PhotoLibraryViewModel = hiltViewModel(),
) {
    val query = "your_search_query" // 這應該是動態查詢字串
    val lazyPagingItems = viewModel.imagesState.collectAsLazyPagingItems()

    PhotoLibraryScreen(imagePagingItems = lazyPagingItems)
}

@Composable
internal fun PhotoLibraryScreen(
    imagePagingItems: LazyPagingItems<NetworkImageDetail>,
    withTopSpacer: Boolean = true,
    withBottomSpacer: Boolean = true,
) {
    val showPopup = remember { mutableStateOf(false) }

    if (showPopup.value) {
        FunctionalityNotAvailablePopup(
            onDismiss = {
                showPopup.value = false
            },
        )
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (withTopSpacer) {
                Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
            }

            LazyColumn {
                items(imagePagingItems.itemCount) { index ->
                    val imageDetail = imagePagingItems[index]!!
                    val imageUrl = imageDetail.webformatURL
                    val imageWidth = imageDetail.webformatWidth
                    val imageHeight = imageDetail.webformatHeight

                    Text(
                        text = imageDetail.user,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = null,
                        placeholder = painterResource(id = R.drawable.placeholder_image),
                        error = painterResource(id = R.drawable.error_image),
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(imageWidth.toFloat() / imageHeight.toFloat()),
                    )
                }
                imagePagingItems.apply {
                    when {
                        loadState.refresh is LoadState.Loading -> {
                            item { PageLoader(modifier = Modifier.fillParentMaxSize()) }
                        }

                        loadState.refresh is LoadState.Error -> {
                            val error = imagePagingItems.loadState.refresh as LoadState.Error
                            item {
                                ErrorMessage(
                                    modifier = Modifier,
                                )
                            }
                        }

                        loadState.append is LoadState.Loading -> {
                            item { LoadingNextPageItem(modifier = Modifier) }
                        }

                        loadState.append is LoadState.Error -> {
                            val error = imagePagingItems.loadState.append as LoadState.Error
                            item {
                                ErrorMessage(
                                    modifier = Modifier,
                                )
                            }
                        }
                    }
                }
            }

            if (withBottomSpacer) {
                Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing))
            }
        }
    }
}

@Composable
fun PageLoader(modifier: Modifier) {
    Text(text = "載入中")
}

@Composable
fun ErrorMessage(modifier: Modifier) {
    Text(text = "ErrorMessage")
}

@Composable
fun LoadingNextPageItem(modifier: Modifier) {
    Text(text = "LoadingNextPageItem")
}

@ThemePreviews
@Composable
fun HomeScreenPreview() {
//    PqTheme {
//        PhotoLibraryScreen(images = lazyPagingItems)
//    }
}
