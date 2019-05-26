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

	var api:RedListConsumer = RedListConsumer(RestTemplateBuilder())

	@Test
	fun basicSyncSteps() {

		val rand = Random(123L)
		val regions = api.listRegions()
		val pickedRegion = regions[rand.nextInt() % regions.size]

		val species = api.listSpeciesByRegion(pickedRegion)
		val cr = species.filter { it.category.equals("CR") }

		//TODO make Consumer more generic
	}

}
