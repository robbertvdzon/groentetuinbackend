package receptenadmin
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import receptenadmin.model.Recepten

fun main(args: Array<String>) {
    val objectMapper = jacksonObjectMapper()
        .setSerializationInclusion(JsonInclude.Include.NON_NULL)
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)

    val resourceAsStream = object {}.javaClass.getResourceAsStream("/auth.json")
    val jsonLines = resourceAsStream?.bufferedReader()?.readLines()?: emptyList()
    val json = jsonLines?.joinToString(separator = "")
    val rec: Recepten = objectMapper.readValue(json,Recepten::class.java)
    val receptenNamen = rec.recipes.map{it.name?:""}.sorted()
    val list = rec.recipes.flatMap { it.ingredients?.map{it.name}?: emptyList() }.distinct().sorted()
    println("Recepten: ${receptenNamen.count()}")
    receptenNamen.forEach {
//        println(" - ${it}")
    }
    println("Ingredienten: ${list.count()}")
    list.forEach {ingredient->
        val containingRecepies = rec.recipes.filter { it.ingredients.map { it.name }.contains(ingredient) }
        val containingRecepiesNames = containingRecepies.map{it.name}.joinToString ()

//        println(" - ${ingredient} : $containingRecepiesNames ")
        println(" - ${ingredient}")
    }

//
//    val fouteIngredienten = """
//Amsterdamse
//Chinees
//Crème
//Flat
//Hollandse
//Griekse
//Japanse
//Parmezaanse
//Rode
//Thaise
//blikje
//bollen
//bos
//conference
//crème
//eetrijpe
//extra
//fijngeknipt
//fijngesneden
//gebraden
//gedroogde
//gekookte
//gele
//gemalen
//gemengde
//geraspte
//gerokt
//gerookte
//geroosterd
//geroosterde
//gezeefde
//griekse
//groene
//grof
//grove
//halfvolle
//hele
//hete
//italiaanse
//kleine
//koelverse
//lauw
//magere
//middelgrote
//mix
//ongebrande
//ongezouten
//pittige
//pure
//rode
//roze
//stuks
//vers
//verse
//voorgekookte
//wilde
//witte
//zoete
//zwart
//zwarte
//    """.trimIndent()
//    val foutIngredientenList = fouteIngredienten.split("\n")


//    println("Fout ingredienten")
//    list.forEach {ingredient->
//        if ( foutIngredientenList.contains(ingredient)) {
//            val containingRecepies = rec.recipes.filter { it.ingredients.map { it.name }.contains(ingredient) }
//            val containingRecepiesNames = containingRecepies.map { it.name }.joinToString()
//
//            println(" - ${ingredient} : $containingRecepiesNames ")
//        }
//    }

//    val fouteRecepten = list.flatMap {ingredient->
//        if ( foutIngredientenList.contains(ingredient)) {
//            val containingRecepies = rec.recipes.filter { it.ingredients.map { it.name }.contains(ingredient) }
//            containingRecepies
//        }
//        else emptyList()
//    }.map{it.name?:""}.distinct()
//    println("Fout recepten: ${fouteRecepten.size}")
//    fouteRecepten.forEach {
//        println(it)
//    }






}
/*
Amsterdamse
Chinees
Crème
Flat
Hollandse
Griekse
Japanse
Parmezaanse
Rode
Thaise
blikje
bollen
bos
conference
crème
eetrijpe
extra
fijngeknipt
fijngesneden
gebraden
gedroogde
gekookte
gele
gemalen
gemengde
geraspte
gerokt
gerookte
geroosterd
geroosterde
gezeefde
griekse
groene
grof
grove
halfvolle
hele
hete
italiaanse
kleine
koelverse
lauw
magere
middelgrote
mix
ongebrande
ongezouten
pittige
pure
rode
roze
stuks
vers
verse
voorgekookte
wilde
witte
zoete
zwart
zwarte










 */