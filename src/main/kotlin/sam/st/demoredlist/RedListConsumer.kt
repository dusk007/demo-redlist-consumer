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
    fun listSpeciesByRegion(region: Region): List<Species>
    fun listSpeciesByRegion(region: Region, page: Int): List<Species>
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
        val resp =
                restTemplate.getForObject("/country/list?token=${token}", CountryResponse::class.java)
        return resp?.results ?: throw IllegalStateException("Api should always return data")
    }

    override fun listRegions(): List<Region> {
        val resp =
                restTemplate.getForObject("/region/list?token=${token}", RegionResponse::class.java)
        return resp?.results ?: throw IllegalStateException("Api should always return data")
    }

    override fun listSpeciesByRegion(region: Region): List<Species> {
        println(baseURI + "/species/region/${region.identifier}/page/0?token=${token}")
        val resp =
                restTemplate.getForObject("/species/region/${region.identifier}/page/0?token=${token}", SpeciesResponse::class.java)
        return resp?.result ?: emptyList()
    }

    override fun listSpeciesByRegion(region: Region, page: Int): List<Species> {
        val resp =
                restTemplate.getForObject("/species/region/${region.identifier}/page/${page}?token=${token}", SpeciesResponse::class.java)
        return resp?.result ?: emptyList()
    }

}