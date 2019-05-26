package sam.st.demoredlist

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.boot.web.client.RestTemplateBuilder

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RedListConsumerTest{

    var sut:RedListConsumerDefinition = RedListConsumer(RestTemplateBuilder())
    val testRegion = Region("Europe","europe")

    @Test
    fun `test endpoint available without token` (){
        val v = sut.reachAPI()
        assertThat(v).isNotNull
        assertThat(v!!.version).isEqualTo("2019-1")
    }

    @Test
    fun `test country endpoint` (){
        val countries = sut.listCountries()
        assertThat(countries).doesNotContainNull()
        assertThat(countries).isNotEmpty()
        assertThat(countries).contains(Country("AT","Austria"))
    }

    @Test
    fun `test region endpoint` (){
        val region = sut.listRegions()
        assertThat(region).doesNotContainNull()
        assertThat(region).isNotEmpty()
        assertThat(region).contains(testRegion)
    }

    @Test
    fun `test species endpoint` (){
        val species = sut.listSpeciesByRegion(testRegion, 0)
        assertThat(species).doesNotContainNull()
        assertThat(species).isNotEmpty()
    }


    @Test
    fun `test conservation measure endpoint` (){
        val list = sut.listConservationMeasure( 12392)
        assertThat(list).doesNotContainNull()
        assertThat(list).isNotEmpty()
    }

    @Test
    @Disabled(value = "on demand for debugging")
    fun `print keys` (){
        println(printDefinition("/species/region/${testRegion.identifier}/page/0"))
    }

    //helper method to debug object fields.
    fun printDefinition(path: String): String {
        val resp = (sut as RedListConsumer).restTemplate
                .getForEntity("${path}?token=${(sut as RedListConsumer).token}", Map::class.java)
        return resp.body!!.entries.map {
            when {
                //take first value in list and print all map keys (assumes it is a map)
                it.value is List<*> ->
                    "field: ${it.key} -> list of obj:"+
                            ((it.value as List<*>).first() as Map<*,*>).keys.joinToString("\n\t","\n")
                //print all keys for a map field
                it.value is Map<*, *> -> "field: ${it.key} -> ${(it.value as Map<*, *>).keys.joinToString(",")}"
                else -> "field: ${it.key}"
            }
        }.joinToString("\n")
    }
}