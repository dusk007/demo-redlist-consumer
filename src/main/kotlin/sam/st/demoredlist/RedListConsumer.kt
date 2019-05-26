package sam.st.demoredlist

import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange
import java.security.cert.CertPath

interface RedListConsumerDefinition {
    val baseURI: String
    fun reachAPI(): Version?
    fun listCountries(): List<Country>
    fun listRegions(): List<Region>
    fun listSpecies(path: String, page: Int): List<Species>
    fun listSpeciesByRegion(region: Region, page: Int): List<Species>
    fun listConservationMeasure(id: Int): List<ConservationMeasure>
}

@Component
class RedListConsumer(templateBuilder: RestTemplateBuilder) : RedListConsumerDefinition {
    final override val baseURI = "https://apiv3.iucnredlist.org/api/v3"
    val restTemplate: RestTemplate = templateBuilder.rootUri(baseURI).build()
    val token = "9bb4facb6d23f48efbf424bb05c0c1ef1cf6f468393bc745d42179ac4aca5fee"

    override fun reachAPI(): Version? {
        return restTemplate.getForObject("/version", Version::class.java)
    }

    override fun listCountries(): List<Country> {
        return restTemplate
                .getForObject("/country/list?token=$token", CountryResponse::class.java)
                ?.results
                ?: throw IllegalStateException("Api should always return data")
    }

    override fun listRegions(): List<Region> {
        return restTemplate
                .getForObject("/region/list?token=$token", RegionResponse::class.java)
                ?.results
                ?: throw IllegalStateException("Api should always return data")
    }

    override fun listSpecies(path: String, page: Int): List<Species> {
        return restTemplate.getForObject("/$path/page/$page?token=$token", SpeciesResponse::class.java)
                ?.result
                ?: emptyList()
    }

    override fun listSpeciesByRegion(region: Region, page: Int): List<Species> {
        return listSpecies("/species/region/${region.identifier}", page = page)
    }

    override fun listConservationMeasure(id: Int): List<ConservationMeasure> {
        println("$baseURI/measures/species/id/$id?token=$token")
        return restTemplate
                .getForObject(
                        "/measures/species/id/$id?token=$token",
                        ConservationMeasureResponse::class.java)
                ?.result
                ?: emptyList()
    }

}