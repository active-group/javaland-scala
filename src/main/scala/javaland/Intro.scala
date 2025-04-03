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