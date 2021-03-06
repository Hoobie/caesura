package caesura

import probation._

object Test extends TestApp {
  def tests(): Unit = {
    test("simple parse") {
      Csv.parse("""hello,world""")
    }.assert(_ == Row("hello", "world"))
    
    test("simple parse with quotes") {
      Csv.parse(""""hello","world"""") // "
    }.assert(_ == Row("hello", "world"))

    test("empty unquoted field at start") {
      Csv.parse(",hello,world")
    }.assert(_ == Row("", "hello", "world"))
    
    test("empty unquoted field at end") {
      Csv.parse("hello,world,")
    }.assert(_ == Row("hello", "world", ""))
    
    test("empty unquoted field in middle") {
      Csv.parse("hello,,world")
    }.assert(_ == Row("hello", "", "world"))
    
    test("empty quoted field at start") {
      Csv.parse(""""","hello","world"""") // "
    }.assert(_ == Row("", "hello", "world"))
    
    test("empty quoted field at end") {
      Csv.parse(""""hello","world",""""")
    }.assert(_ == Row("hello", "world", ""))
    
    test("empty quoted field in middle") {
      Csv.parse(""""hello","","world"""") // "
    }.assert(_ == Row("hello", "", "world"))
   
    test("quoted comma") {
      Csv.parse(""""hello,world"""") // "
    }.assert(_ == Row("hello,world"))
    
    test("escaped quotes") {
      Csv.parse(""""hello""world"""") // "
    }.assert(_ == Row("""hello"world"""))

    test("decode case class") {
      Csv.parse("""hello,world""").as[Foo]
    }.assert(_ == Foo("hello", "world"))

    test("decode complex case class") {
      Csv.parse("""0.1,two,three,4,five,six""").as[Bar]
    }.assert(_ == Bar(0.1, Foo("two", "three"), 4, Foo("five", "six")))

    test("encode case class") {
      Csv(Foo("hello", "world"))
    }.assert(_ == Row("hello", "world"))
    
    test("encode complex case class") {
     Csv(Bar(0.1, Foo("two", "three"), 4, Foo("five", "six")))
    }.assert(_ == Row("0.1", "two", "three", "4", "five", "six"))

    test("convert simple row to string") {
      Row("hello", "world").toString
    }.assert(_ == """"hello","world"""") // "
    
    test("convert complex row to string") {
      Row("0.1", "two", "three", "4", "five", "six").toString
    }.assert(_ == """"0.1","two","three","4","five","six"""") // "

    test("convert row with escaped quote") {
      Row("hello\"world").toString
    }.assert(_ == """"hello""world"""")
  }
}

case class Foo(one: String, two: String)
case class Bar(one: Double, foo1: Foo, four: Int, foo2: Foo)
