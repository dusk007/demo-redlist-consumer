package sam.st.demoredlist

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class Version(val version:String)
@JsonIgnoreProperties(ignoreUnknown = true)
data class Country(val isocode: String, val country:String)
@JsonIgnoreProperties(ignoreUnknown = true)
data class Region(val name: String, val identifier: String)
@JsonIgnoreProperties(ignoreUnknown = true)
data class Species(
    val taxonid:Int?,
    val kingdom_name:String?,
    val phylum_name:String?,
    val class_name:String?,
    val order_name:String?,
    val family_name:String?,
    val genus_name:String?,
    val scientific_name:String?,
    val infra_rank:String?,
    val infra_name:String?,
    val population:String?,
    val category:String?
)
@JsonIgnoreProperties(ignoreUnknown = true)
data class CountryResponse(val count:Int, val page:Int, val results: List<Country> = emptyList())
@JsonIgnoreProperties(ignoreUnknown = true)
data class RegionResponse(val count:Int, val page:Int, val results: List<Region> = emptyList())
@JsonIgnoreProperties(ignoreUnknown = true)
data class SpeciesResponse(val count:Int?, val page:Int?, val result: List<Species> = emptyList())
