package com.demo.moviehub.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.demo.moviehub.data.model.CastMember
import com.demo.moviehub.util.ImageUrlBuilder

@Composable
fun CastItem(
    cast: CastMember,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.width(80.dp)
    ) {
        Box(
            modifier = Modifier
                .size(70.dp)
                .clip(CircleShape)
        ) {
            cast.profilePath?.let { path ->
                val imageUrl = "${ImageUrlBuilder.BASE_IMAGE_URL}w185$path"
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "${cast.name}'s profile",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = cast.name ?: "",
            style = MaterialTheme.typography.labelSmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        
        if (cast.character.isNotEmpty()) {
            Text(
                text = cast.character,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
