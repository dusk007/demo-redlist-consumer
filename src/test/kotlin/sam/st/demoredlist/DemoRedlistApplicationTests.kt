package sam.st.demoredlist

import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import io.reactivex.Flowable
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.boot.web.client.RestTemplateBuilder
import kotlin.random.Random


/**
 * Test Class for the filter print result logic
 */

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DemoRedlistApplicationTests {

    var api: RedListConsumer = RedListConsumer(RestTemplateBuilder())
    val seed = 123L
    val takeLimit = 12 //limit to keep execution time low

    //swtiched to specific because random often seems to just not yield and results
    val pickedRandomRegion: Region by lazy {
        val rand = Random(seed)
        val regions = api.listRegions()
        regions[rand.nextInt() % regions.size]
    }
    val europe = Region("", "europe")

    val speciesData = CacheBuilder.newBuilder()
            .maximumSize(10)
            .build(object : CacheLoader<Int, List<Species>>() {
                override fun load(key: Int) = api.listSpeciesByRegion(europe, key)
            })

    val conservationCache = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .build(object : CacheLoader<Int, List<ConservationMeasure>>() {
                override fun load(key: Int) = api.listConservationMeasure(key)
            })
    val updateConservationMeasures: (Species) -> Species = { species ->
        species.copy(conservationMeasures = conservationCache.getUnchecked(species.taxonid)
                .map { it.title }
                .toSet())
    }

    @Test
    fun `task steps 1 through 8`() {
        val criticallyEndangered = speciesData.get(0)
                .filter { "CR" == it.category }
                .take(takeLimit)
                .map { updateConservationMeasures(it) }
        criticallyEndangered.forEach { println(it) }
        assertThat(criticallyEndangered).isNotEmpty
    }

    @Test
    fun `task steps 9 through 10`() {
        val mammals =
                speciesData.get(0)
                        .filter { "MAMMALIA" == it.class_name }
                        .take(takeLimit) //takes too long otherwise
                        .map { updateConservationMeasures(it) }
        mammals.forEach { println(it) }
        assertThat(mammals).isNotEmpty
    }

    @Test
    fun `task steps 1 through 8 async`() {
        val singleCount = Flowable.fromIterable(
                speciesData.get(0)
                        .filter { "CR" == it.category }
                        .take(takeLimit)
        )
                .map { updateConservationMeasures(it) }
                .doOnNext { println(it) }
                .count()
        assertThat(singleCount.blockingGet()).isGreaterThan(0)
    }

    @Test
    fun `task steps 9 through 10 async`() {
        val singleCount = Flowable.fromIterable(
                speciesData.get(0).filter { it.class_name == "MAMMALIA" }
        ).take(takeLimit.toLong())
                .map { updateConservationMeasures(it) }
                .doOnNext { println(it) }
                .count()
        assertThat(singleCount.blockingGet()).isGreaterThan(0)
    }

}
