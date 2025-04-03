package contract

/*
 * 1. einfaches Beispiel
 *    Zero-Bond / zero-coupon bond
 *    "Ich bekomme am 24.12.2025 100€."
*/

case class Date(iso: String)

enum Contract {
  case ZeroCouponBond(date: Date, amount: Amount, currency: Currency)
}