package org.mixdrinks.ui

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import org.mixdrinks.ui.details.DetailView
import org.mixdrinks.ui.filters.main.FilterView
import org.mixdrinks.ui.filters.search.SearchItemView
import org.mixdrinks.ui.list.CocktailListView

@Composable
internal fun RootContent(component: RootComponent) {
  var lastTouch by remember { mutableStateOf(Offset.Infinite) }
  Children(
      modifier = Modifier.pointerInput(Unit) {
        detectDragGestures(
            onDragStart = {
              lastTouch = if (it.x < CLOSE_ANIMATION_DURACIOTN_TRIGGER) {
                it
              } else {
                Offset.Infinite
              }
            },
            onDragEnd = {
              lastTouch = Offset.Infinite
            },
            onDragCancel = {
              lastTouch = Offset.Infinite
            },
            onDrag = { change: PointerInputChange, dragAmount: Offset ->
              if (change.position.x - lastTouch.x > CLOSE_ANIMATION_DURACIOTN_TRIGGER) {
                component.onBack()
              }
            }
        )
      },
      stack = component.stack,
      animation = stackAnimation(
          animator = slide()
      ),
      content = {
        when (val child = it.instance) {
          is RootComponent.Child.List -> CocktailListView(child.component)
          is RootComponent.Child.Details -> DetailView(child.component)
          is RootComponent.Child.Filters -> FilterView(child.component)
          is RootComponent.Child.ItemSearch -> SearchItemView(child.component)
        }
      }
  )
}

private const val CLOSE_ANIMATION_DURACIOTN_TRIGGER = 300
