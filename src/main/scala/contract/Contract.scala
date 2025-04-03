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

case class Date(iso: String) {
  def isBefore(other: Date): Boolean =
    this.iso < other.iso
}

type Amount = Double

enum Currency {
  case EUR
  case GBP
  case USD
  case YEN
}

enum Direction {
  case Long
  case Short

  def invert: Direction =
    this match
      case Direction.Long => Direction.Short
      case Direction.Short => Long
}

enum Contract {
  // case ZeroCouponBond(date: Date, amount: Amount, currency: Currency)
  case One(currency: Currency)
  case More(amount: Amount, contract: Contract)
  case Timed(date: Date, contract: Contract)
  case And(contract1: Contract, contract2: Contract)
  case Invert(contract: Contract)
  case Zero
  // case Done(contract: Contract)
}


import contract.Contract.*
import contract.Currency.*
import contract.Direction.Long

def more(amount: Amount, contract: Contract): Contract = {
  contract match
    case Zero => Zero
    case _ => More(amount, contract)
}

def and(contract1: Contract, contract2: Contract): Contract = {
  (contract1, contract2) match
    case (Zero, _) => contract2
    case (_, Zero) => contract1
    case _ => And(contract1, contract2)
}

def invert(inputContract: Contract): Contract = {
  inputContract match
    case Zero => Zero
    case Invert(contract) => contract
    case _ => Invert(inputContract)
}

// "Ich bekomme 1€ jetzt."
val c1 = One(Currency.EUR)
// "Ich bekomme 100€ jetzt."
val c2 = More(100, One(Currency.EUR))

// val zcb1 = Timed(Date("2025-12-24"), More(100, One(EUR)))

def zeroCouponBond(date: Date, amount: Amount, currency: Currency): Contract =
  Timed(date, More(amount, One(currency)))

val zcb1 = zeroCouponBond(Date("2025-12-24"), 100, Currency.EUR)
val zcb2 = zeroCouponBond(Date("2025-12-24"), 10000, YEN)

val fxSwap = And(zcb1, Invert(zcb2))

// val c3 = Directed(Direction.Long, c2)
val c4 = Invert(Invert(c2)) // == c2

case class Payment(direction: Direction, date: Date, amount: Amount, currency: Currency) {
  def multiply(multiplier: Amount): Payment =
    Payment(this.direction, date, this.amount * multiplier, currency)

  def invert: Payment =
    Payment(this.direction.invert, this.date, this.amount, this.currency)
}

// Semantik
// alle Zahlungen bis today ... und "Residualvertrag"
def meaning(contract: Contract, today: Date): (List[Payment], Contract) =
  contract match
    case One(currency) => (List(Payment(Long, today, 1, currency)), Zero)

    case More(amount, contract) => {
      val (payments, resultContract) = meaning(contract, today)
      (payments.map(_.multiply(amount)), more(amount, resultContract))
    }

    case Timed(date, contract) =>
      if date.isBefore(today) then
        meaning(contract, today)
      else
        (Nil, Timed(date, contract))

    case And(contract1, contract2) => {
      val (payments1, resultContract1) = meaning(contract1, today)
      val (payments2, resultContract2) = meaning(contract2, today)
      (payments1 ++ payments2, and(resultContract1, resultContract2))
    }

    case Invert(contract) => {
      val (payments, resultContract) = meaning(contract, today)
      (payments.map(_.invert), invert(resultContract))
    }

    case Zero => (Nil, Zero)


val c6 = More(100, And(One(EUR), Timed(Date("2025-12-24"), One(EUR))))