package receptenadmin.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties


data class Recepten(
    val recipes: List<Recept>
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Recept(
    val uuid: String?,
    val name: String?,
    val instructions: String?,
    val remark: String?,
    val totalCookingTime: Int?,
    val preparingTime: Int?,
    val dateAdded: Long?,
    val nrPersons: Int?,
    val favorite: Boolean?,
    val ingredients: List<ReceptIngredient>,
    val tags: List<String?>?
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class ReceptIngredient(
    val amount: Amount,
    val name: String
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Amount(val nrUnit: Double, val unit: String)
