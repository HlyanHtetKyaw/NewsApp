package com.test.newsapp.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.test.newsapp.R
import com.test.newsapp.data.mappers.toNews
import com.test.newsapp.domain.News
import com.test.newsapp.util.DateUtil
import kotlinx.coroutines.flow.firstOrNull

@Composable
fun NewsDetailScreen(viewModel: NewsViewModel, navController: NavHostController, newId: Int?) {

    var news by remember { mutableStateOf<News?>(null) }

    LaunchedEffect(key1 = newId) {
        val newEntity = viewModel.getNewsById(newId!!).firstOrNull()
        newEntity?.let {
            news = it.toNews()
        }
    }

    news?.let {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.LightGray)
                .padding(bottom = 16.dp)
        ) {
            NewDetailTopSection(
                navController = navController,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.2f)
                    .align(Alignment.TopCenter)
            )
            AsyncImage(
                model = it.urlToImage,
                contentScale = ContentScale.FillBounds,
                contentDescription = it.title,
                error = painterResource(R.drawable.ic_error),
                placeholder = painterResource(R.drawable.ic_error),
                modifier = Modifier
                    .height(120.dp)
                    .align(Alignment.TopCenter)
                    .offset(y = 40.dp)
                    .aspectRatio(1f)
            )
            NewDetailSection(
                data = news!!,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = 200.dp,
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 16.dp
                    )
                    .shadow(10.dp, RoundedCornerShape(10.dp))
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White)
                    .padding(16.dp)
                    .align(Alignment.BottomCenter)
                    .offset(y = (-20).dp)
            )
        }
    }
}

@Composable
fun NewDetailTopSection(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.TopStart,
        modifier = modifier
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color.Black,
                        Color.Transparent
                    )
                )
            )
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .size(36.dp)
                .offset(16.dp, 16.dp)
                .clickable {
                    navController.popBackStack()
                }
        )
    }
}


@Composable
fun NewDetailSection(
    data: News,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .offset(y = 16.dp)
            .verticalScroll(scrollState)
    ) {
        Text(
            text = data.title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Published at ${DateUtil.formatDate(data.publishedAt)}",
            modifier = Modifier.fillMaxWidth(),
            fontSize = 14.sp,
            color = Color.Gray,

            )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Authored by - ${data.author}",
            fontSize = 14.sp,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = data.description,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(8.dp))
        ClickableLinkText(data.url, modifier = Modifier.fillMaxWidth())
    }
}