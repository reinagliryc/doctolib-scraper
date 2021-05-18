#!/usr/bin/env amm

//import scala.util.matching.Regex

val headers = Map("user-agent"-> "Mozilla/5.0 (X11; Linux x86_64; rv:87.0) Gecko/20100101 Firefox/87.0") 
//val html = requests.get("https://www.doctolib.fr/vaccination-covid-19/clamart?force_max_limit=2&ref_visit_motive_id=6970&ref_visit_motive_ids%5B%5D=6970&ref_visit_motive_ids%5B%5D=7005", headers= headers)
//val ids = """search-result-(\d+)""".r.findAllIn(html.text).matchData.map(_.group(1)).toList

val ids = List(
  5332969, // HIA Percy
  5431747, // Clamart
  5434751, // Meudon
  5432643, // Issy
  5031035, // Porte de Versailles
  5431892, // Porte de Vanves
  5326764, // Chaville
  5430423, // Boulogne Billancourt
  54321267, // Institut Pasteur
  5448891, // Velizy Villacoublay
  5431795
)

def searchSlot(ids : List[Int]) : List[(Int, String)] = for { 
  id <- ids
  request <- Some(requests.get(s"https://www.doctolib.fr/search_results/$id.json?ref_visit_motive_id=6970&ref_visit_motive_ids%5B%5D=6970&ref_visit_motive_ids%5B%5D=7005&speciality_id=5494&search_result_format=json&force_max_limit=2", headers = headers)) 
  result <- Some(ujson.read(request.text)) 
  total <- result.obj.get("total") 
  num <- Some(total.num) if num > 0 
  search_result <- result.obj.get("search_result") 
  link <- search_result.obj.get("link") 
} yield (num.toInt, s"https://www.doctolib.fr${link.value}") 

searchSlot(ids).foreach(x => println(s"${x._1} slot(s) : ${x._2}"))
