package sam.st.demoredlist

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.client.RestTemplateBuilder
import kotlin.random.Random


/**
 * Test Class for the filter print result logic
 */

class DemoRedlistApplicationTests {

    var api: RedListConsumer = RedListConsumer(RestTemplateBuilder())

    @Test
    fun `task steps 1 through 7` () {

        val rand = Random(123L)
        val regions = api.listRegions()
        val pickedRegion = regions[rand.nextInt() % regions.size]

        api.listSpeciesByRegion(pickedRegion, 0)
                .filter { it.category.equals("CR") }
                .map {
                    it.copy(conservationMeasures = api
                            .listConservationMeasure(it.taxonid)
                            .map { it.title }
                            .toSet())
                }.forEach { println(it) }
    }

}
