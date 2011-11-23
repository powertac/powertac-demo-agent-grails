package org.powertac.broker

import org.powertac.common.Competition
import org.powertac.common.enumerations.ProductType
import org.powertac.common.Shout.OrderType

class ShoutRequest
{
  boolean active

  /** the product that should be bought or sold    */
  ProductType product

  int beginTimeslot = 1
  int endTimeslot = Integer.MAX_VALUE

  /** flag that indicates if this shout is a buy or sell order    */
  OrderType buySellIndicator

  /** the product quantity to buy or sell    */
  BigDecimal quantity

  /** the limit price, i.e. the max. acceptable buy or min acceptable sell price    */
  BigDecimal limitPrice

  /** the last executed quantity (if equal to {@code quantity} the shout is fully executed otherwise it is partially executed    */
  BigDecimal executionQuantity

  /** the last execution price    */
  BigDecimal executionPrice

  /** optional comment that can be used for example to further describe why a shout was deleted by system (e.g. during deactivaton of a timeslot)    */
  String comment

  static constraints = {
    comment(nullable: true)
  }

  static namedQueries = {
    findAllActiveAtTimeslot { timeslot ->
      def competition = Competition.list()[0]
      if (competition) {
        eq('active', true)
        le('beginTimeslot', timeslot)
        ge('endTimeslot', timeslot)
      }
    }
  }
}