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
}

import Contract._

// "Ich bekomme 1€ jetzt."
val c1 = One(Currency.EUR)
val c2 = More(100, One(Currency.EUR))

// val zcb1 = ZeroCouponBond(Date("2025-12-24"), 100, Currency.EUR)