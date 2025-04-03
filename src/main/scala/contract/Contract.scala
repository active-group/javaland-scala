package contract

/*
 * 1. einfaches Beispiel
 *    Zero-Bond / zero-coupon bond
 *    "Ich bekomme am 24.12.2025 100€."
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
  case ZeroCouponBond(date: Date, amount: Amount, currency: Currency)
}

import Contract._

val zcb1 = ZeroCouponBond(Date("2025-12-24"), 100, Currency.EUR)