package contract

/*
 * 1. einfaches Beispiel
 *    Zero-Bond / zero-coupon bond
 *    "Ich bekomme am 24.12.2025 100€."
 * 2. Beispiel zerlegen in "atomare" Bestandteile / Ideen
 *    ... z.B. anhand der Attribute
 *    - Währung
 *    - Betrag
 *    - Später
 *    ... dabei nach Selbstbezügen schauen => Kombinatoren
 * 3. wiederholen
 * 
 * Currency Swap:
 * Am 24.12.2025 bekomme ich 100€ und ich zahle 10000 Yen.
 */

case class Date(iso: String)

type Amount = Double

enum Currency {
  case EUR
  case GBP
  case USD
  case YEN
}

enum Contract {
  // case ZeroCouponBond(date: Date, amount: Amount, currency: Currency)
  case One(currency: Currency)
  case More(amount: Amount, contract: Contract)
  case Timed(date: Date, contract: Contract)
  case And(contract1: Contract, contract2: Contract)
}

import Contract._
import Currency._

// "Ich bekomme 1€ jetzt."
val c1 = One(Currency.EUR)
val c2 = More(100, One(Currency.EUR))

// val zcb1 = Timed(Date("2025-12-24"), More(100, One(EUR)))

def zeroCouponBond(date: Date, amount: Amount, currency: Currency): Contract =
  Timed(date, More(amount, One(currency)))

val zcb1 = zeroCouponBond(Date("2025-12-24"), 100, Currency.EUR)
val zcb2 = zeroCouponBond(Date("2025-12-24"), 10000, Yen)
