package terapps.factoryplanner.core.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import terapps.factoryplanner.core.FactoryPlannerTestApplication
import kotlin.test.Test


@SpringBootTest(classes = [FactoryPlannerTestApplication::class])
@ActiveProfiles
class FactoryPlannerServiceTest {

    @Autowired
    private lateinit var itemDescriptorService: ItemDescriptorService
    @Autowired
    private lateinit var factoryPlannerService: FactoryPlannerService

    @Test
    fun testPaths(

    ) {
        val item = itemDescriptorService.findByClassName("Desc_OreIron_C")
        val graph = factoryPlannerService.planFactorySiteAllCombinations(item, 60.0)
    }
}