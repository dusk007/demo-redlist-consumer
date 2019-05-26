package sam.st.demoredlist

data class Version(val version:String)

data class Country(val isocode: String, val country:String)
data class CountryResponse(val count:Int, val page:Int, val results: List<Country> = emptyList())

data class Region(val name: String, val identifier: String)
data class RegionResponse(val count:Int, val page:Int, val results: List<Region> = emptyList())

data class ConservationMeasure(val code:String, val title:String)
data class ConservationMeasureResponse(val id:Int, val result: List<ConservationMeasure> = emptyList())

data class Species(
    val taxonid:Int,
    val kingdom_name:String,
    val phylum_name:String,
    val class_name:String,
    val order_name:String,
    val family_name:String,
    val genus_name:String,
    val scientific_name:String,
    val infra_rank:String?,
    val infra_name:String?,
    val population:String?,
    val category:String,
    val conservationMeasures: Set<String>?
)
data class SpeciesResponse(val count:Int, val page:Int, val result: List<Species> = emptyList())
