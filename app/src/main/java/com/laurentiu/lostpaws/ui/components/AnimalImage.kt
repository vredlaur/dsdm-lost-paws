package com.laurentiu.lostpaws.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.laurentiu.lostpaws.ui.theme.CreamDark

@Composable
fun AnimalImage(
    imageUrl: String,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    if (imageUrl.isBlank()) {
        PlaceholderAnimalImage(modifier)
    } else {
        AsyncImage(
            model = imageUrl,
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
            error = rememberVectorPainter(Icons.Filled.Pets),
            modifier = modifier
                .clip(MaterialTheme.shapes.medium)
                .background(CreamDark)
        )
    }
}

@Composable
fun PlaceholderAnimalImage(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .background(CreamDark),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Filled.Pets,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.size(48.dp)
        )
    }
}
