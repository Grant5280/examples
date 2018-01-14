/**
  * Created by Grant on 2018/1/4/004.
  */
object Option_Test extends App{
  val myMap: Map[String, String] = Map("key1" -> "value")
  val value1: Option[String] = myMap.get("key1")
  val value2: Option[String] = myMap.get("key2")

  println(show(value1)) // Some("value1")
  println(show(value2)) // None
  def show(option: Option[String]) = option match {
    case Some(s) => s
    case None => "?"
  }

  println(value1.getOrElse("defualt"))
  println(value2.getOrElse("defualt"))

}
