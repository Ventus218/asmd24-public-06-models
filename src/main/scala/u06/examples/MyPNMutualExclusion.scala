package u06.examples

import u06.utils.MSet

object MyPNMutualExclusion:

  enum Place:
    case Idle // P1
    case DecideIfReadOrWrite // P2
    case WaitToRead // P3
    case WaitToWrite // P4
    case Token // P5
    case Reading // P6
    case Writing // P7

  export Place.*
  export u06.modelling.PetriNet.*
  export u06.modelling.SystemAnalysis.*
  export u06.utils.MSet

  // DSL-like specification of a Petri Net
  def pnME = PetriNet[Place](
    MSet(Idle) ~~> MSet(DecideIfReadOrWrite), // t1
    MSet(DecideIfReadOrWrite) ~~> MSet(WaitToRead), // t2
    MSet(DecideIfReadOrWrite) ~~> MSet(WaitToWrite), // t3
    MSet(WaitToRead, Token) ~~> MSet(Reading, Token), // t4
    MSet(Reading) ~~> MSet(Idle), // t6
    MSet(WaitToWrite, Token) ~~> MSet(Writing) ^^^ MSet(Reading), // t5
    MSet(Writing) ~~> MSet(Idle, Token) // t7
  ).toSystem

@main def mainMyPNMutualExclusion =
  import MyPNMutualExclusion.*
  // example usage
  println(pnME.paths(MSet(Idle, Idle, Token), 7).toList.mkString("\n"))
