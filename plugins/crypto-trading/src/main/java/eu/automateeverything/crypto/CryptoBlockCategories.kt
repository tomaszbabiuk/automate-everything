package eu.automateeverything.crypto

import eu.automateeverything.data.localization.Resource
import eu.automateeverything.domain.automation.blocks.BlockCategory

enum class CryptoBlockCategories(
    override val categoryName: Resource,
    override val color: Int
) : BlockCategory {
    Crypto(R.category_crypto, 15),
}