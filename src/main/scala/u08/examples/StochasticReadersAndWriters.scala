package u08.examples

object StochasticReadersAndWriters extends App:

  enum Place:
    case Idle // P1
    case DecideIfReadOrWrite // P2
    case WaitToRead // P3
    case WaitToWrite // P4
    case Token // P5
    case Reading // P6
    case Writing // P7

  export Place.*
  export u08.modelling.CTMCSimulation.*
  export u08.modelling.SPN
  export u08.modelling.SPN.*
  import scala.u08.utils.MSet

  val spn = SPN[Place](
    Trn(MSet(Idle), _ => 1.0, MSet(DecideIfReadOrWrite), MSet()), // t1
    Trn(MSet(DecideIfReadOrWrite), _ => 200_000, MSet(WaitToRead), MSet()), // t2
    Trn(MSet(DecideIfReadOrWrite), _ => 100_000, MSet(WaitToWrite), MSet()), // t3
    Trn(MSet(WaitToRead, Token), _ => 100_000, MSet(Reading, Token), MSet()), // t4
    Trn(MSet(WaitToWrite, Token), _ => 100_000, MSet(Writing), MSet(Reading)), // t5
    Trn(MSet(Reading), m => 0.1 * m(Reading), MSet(Idle), MSet()), // t6
    Trn(MSet(Writing), m => 0.2, MSet(Idle, Token), MSet()) // t7
  )

  println:
    toCTMC(spn)
      .newSimulationTrace(MSet(Idle, Idle, Idle, Token), new java.util.Random)
      .take(20)
      .toList
      .mkString("\n")
