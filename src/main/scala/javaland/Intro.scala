package javaland

val s = "Mike"

// Ein Haustier ist eins der folgenden:
// - Hund -ODER-
// - Katze -ODER-
// - Schlange
// Fallunterscheidung / Summe
enum Pet {
    case Dog
    case Cat
    case Snake

    def isCute: Boolean =
        this match {
            case Dog => true
            case Cat => true
            case Snake => false
        }
}

val pet1 = Pet.Dog

import Pet._

// Ist Haustier niedlich?
def isCute(pet: Pet): Boolean =
    pet match { // Verzweigung, ein Zweig pro Fall
        case Cat => true
        case Dog => true
        case Snake => false
    }

// Uhrzeit besteht aus / hat folgende Eigenschaften:
// - Stunde -UND-
// - Minute
// zusammengesetzte Daten / Produkt
type Hour = Int
type Minute = Int
case class Time(hour: Hour, minute: Minute) {
    // Minuten seit Mitternacht
    def minutesSinceMidnight: Int =  // keine Parameter => Klammern weglassen
        this.hour * 60 + this.minute
}

// 9 Uhr 44
val time1 = Time(9, 44)

// Minuten seit Mitternacht
def minutesSinceMidnight(time: Time): Int =
    time.hour * 60 + time.minute

// umgekehrt: Aus Minuten seit Mitternacht einen Time-Wert machen
def msmToTime(msm: Int): Time =
    val hour = msm / 60
    val minute = msm % 60
    Time(hour = msm / 60, minute = msm % 60)

// Tier auf dem texanischen Highway:
// - Gürteltier -ODER-
// - Papagei

// Papagei hat folgende Eigenschaften:
// - Satz
// - Gewicht

// Gürteltier hat folgende Eigenschaften:
// - (lebendig ODER tot)   -UND-
// - Gewicht
// Produkt

// "boolean blindness" / "primitive obsession"
// case class Dillo(alive: Bool, weight: Double)
enum Liveness {
    case Alive
    case Dead
}
type Weight = Double
/*
// Repräsentation des Zustands des Gürteltiers
// zu einem bestimmten Zeitpunkt
case class Dillo(liveness: Liveness, weight: Weight) {
    // Gürteltier überfahren
    def runOver: Dillo =
        Dillo(Liveness.Dead, this.weight)

    // Gürteltier füttern
    def feed(amount: Weight): Dillo = {
      this.liveness match {
          case Liveness.Alive => 
              Dillo(Liveness.Alive, weight + amount)
          case Liveness.Dead => this
      }
    }
}
*/

// algebraischer Datentyp: Summe aus Produkten
enum Animal {
  case Dillo(liveness: Liveness, weight: Weight) 
  case Parrot(sentence: String, weight: Weight)

  def runOver: Animal =
    this match {
      case Dillo(_, w) =>
        Dillo(Liveness.Dead, w)
      case Parrot(_, w) =>
        Parrot("", w)
    }
}

// lebendiges Gürteltier, 10kg
val dillo1 = Animal.Dillo(Liveness.Alive, 10)
// totes Gürteltier, 8kg
val dillo2 = Animal.Dillo(Liveness.Dead, 8)
// Begrüßungspapagei
val parrot1 = Animal.Parrot("Welcome!", 1)

// Im Kosmetikladen gibt es:

// - Seife -ODER-
// - Shampoo -ODER
// - Duschgel, bestehend aus Seife UND Shampoo

type PH = Double
enum Hairtype {
  case Normal
  case Dandruff
  case Oily
}

/* 1. Versuch: unflexibel
case class Soap(pH: PH)
case class Shampoo(hairtype: Hairtype)
case class Showergel(soap: Soap, shampoo: Shampoo)
*/

enum Showerproduct {
  case Soap(pH: PH)
  case Shampoo(hairtype: Hairtype)
  // case Showergel(soap: Soap, shampoo: Shampoo)
  // Kombinator: 2 Duschprodukte rein, 1 Duschprodukt raus
  case Mixture(product1: Showerproduct,  // Selbstbezug
               product2: Showerproduct)

  // Seifenanteil, zwischen 0.0 und 1.0
  def soapProportion: Double =
    this match {
      case Soap(pH) => 1.0
      case Shampoo(hairtype) => 0.0
      case Mixture(product1, product2) =>
        // Selbstbezug => rekursiver Aufruf
        (product1.soapProportion + product2.soapProportion) / 2.0
    }
}

val pr1 = Showerproduct.Shampoo(Hairtype.Normal)
val pr2 = Showerproduct.Soap(7)
val pr3 = Showerproduct.Mixture(pr1, pr2)
val pr4 = Showerproduct.Mixture(pr3, Showerproduct.Soap(5))

// Eine geometrische Figur (Shape) ist eins der folgenden:
// - Kreis -ODER-
// - Quadrat -ODER-
// - eine Überlagerung zweier geometrischer Figuren

// 1. Datenmodellierung
// 2. Funktion, die für einen Punkt feststellt, ob er innerhalb
//    einer geometrischen Figur liegt
case class Point(x: Double, y: Double)

def distance(point1: Point, point2: Point) = {
  val dx = point1.x - point2.x
  val dy = point1.y - point2.y
  Math.sqrt((dx*dx) + (dy*dy))
}
  
enum Shape {
  case Circle(center: Point, radius: Double)
  case Square(llCorner: Point, sideLength: Double)
  case Overlay(shape1: Shape, shape2: Shape)

  def contains(point: Point): Boolean =
    this match {
      case Circle(center, radius) =>
        distance(center, point) <= radius
      case Square(llCorner, sideLength) =>
        point.x >= llCorner.x &&
        point.y >= llCorner.y &&
        point.x <= llCorner.x + sideLength &&
        point.y <= llCorner.y + sideLength
      case Overlay(shape1, shape2) =>
        shape1.contains(point) || shape2.contains(point)
    }
}

// Eine Liste ist eins der folgenden:
// - die leere Liste -ODER-
// - eine Cons-Liste aus erstem Element und Rest-Liste
//                                               ^^^^^ Selbstbezug

// Scala: List[...]
// leere Liste: Nil
// Cons:  ::, Infix

// 1elementige Liste: 5
val list1: List[Integer] = 5 :: Nil

// 2elementige Liste: 8 5
val list2: List[Integer] = (8: Integer) :: (5: Integer) :: Nil

// 3elementige Liste
val list3: List[Integer] = List(4, 8, 5)

// 4elementige Liste: 7 4 8 5
val list4 = (7: Integer) :: list3

def listSum(list: List[Integer]): Integer =
  list match {
    case Nil =>0 // neutrales Element von +
    case first :: rest =>
      first + listSum(list)
  }

// neutrales Element ... Gruppen

// aus einer Liste von Integers die geraden Zahlen extrahieren
def isEven(x: Integer): Boolean = x % 2 == 0

def extractEvens(list: List[Integer]): List[Integer] =
  list match {
    case Nil => Nil
    case first :: rest =>
//      isEven(first) match {
//        case true => first :: extractEvens(rest)
//        case false => extractEvens(rest)
//      }
        if isEven(first)
        then first :: extractEvens(rest)
        else extractEvens(rest)
  }

def isOdd(x: Integer): Boolean = x % 2 != 0

def extractOdds(list: List[Integer]): List[Integer] =
  list match {
    case Nil => Nil
    case first :: rest => 
      if isOdd(first)
      then first :: extractOdds(rest)
      else extractOdds(rest)
  }

def filter[A](p: A => Boolean, list: List[A]): List[A] =
  list match {
    case Nil => Nil
    case first :: rest => 
      if p(first)
      then first :: filter(p, rest)
      else filter(p, rest)
  }

val evens = filter(isEven, list4)

val highway = List(dillo1, dillo2, parrot1)

// val deadAnimals = highway.map({ animal => animal.runOver })
// val deadAnimals = highway.map { animal => animal.runOver }
// val deadAnimals = highway.map { _.runOver }

extension (list: List[Animal])
  def runOver = list.map(_.runOver)

val deadAnimals = highway.runOver


val tuple1 = ("Mike", Cat, dillo1)

def partition[A](p: A => Boolean, list: List[A]): (List[A], List[A]) =
  list match {
    case Nil => (Nil, Nil)
    case first :: rest =>
      val (trues, falses) = partition(p, rest)
      if p(first)
      then (first :: trues, falses)
      else (trues, first :: falses)
  }