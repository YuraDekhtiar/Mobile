package org.mixdrinks.ui.details

import org.mixdrinks.data.FullCocktail
import org.mixdrinks.dto.CocktailId
import org.mixdrinks.dto.SnapshotDto

internal class FullCocktailRepository(
    private val snapshot: suspend () -> SnapshotDto,
) {

  suspend fun getFullCocktail(cocktailId: CocktailId): FullCocktail? {
    val cocktail = snapshot().cocktails.find { it.id == cocktailId } ?: return null

    val tools = snapshot().tools.filter { cocktail.tools.contains(it.id) }
        .map {
          FullCocktail.Tool(
              toolId = it.id,
              name = it.name,
          )
        }
    val tastes = snapshot().tastes.filter { cocktail.tastes.contains(it.id) }
        .map {
          FullCocktail.Taste(
              id = it.id,
              name = it.name,
          )
        }
    val tags = snapshot().tags.filter { cocktail.tags.contains(it.id) }
        .map {
          FullCocktail.Tag(
              id = it.id,
              name = it.name,
          )
        }

    val glassware = snapshot().glassware.find { it.id == cocktail.glassware }
        ?: error("Cannot found glassware for cocktail")

    return FullCocktail(
        id = cocktail.id,
        name = cocktail.name,
        receipt = cocktail.receipt,
        tools = tools,
        tags = tags,
        tastes = tastes,
        glassware = FullCocktail.Glassware(id = glassware.id, name = glassware.name),
    )
  }
}
