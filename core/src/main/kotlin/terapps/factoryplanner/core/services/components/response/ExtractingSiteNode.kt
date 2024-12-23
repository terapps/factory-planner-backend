package terapps.factoryplanner.core.services.components.response

import terapps.factoryplanner.core.dto.ExtractorDto
import terapps.factoryplanner.core.dto.ItemDescriptorDto

data class ExtractingSiteNode(
        override val factorySiteTarget: ItemDescriptorDto,
        val automaton: ExtractorDto
) : FactoryNode(FactorySiteType.ExtractorSite) {
    override val label: String
        get() = "${factorySiteTarget.displayName}-${automaton.displayName}"

    override val id: String
        get() = "${factorySiteTarget.className}-${automaton.className}"
}