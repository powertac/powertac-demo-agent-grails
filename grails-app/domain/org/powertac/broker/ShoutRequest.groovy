package org.powertac.broker

class ShoutRequest
{
  boolean active

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

	enum OrderType {
		BUY, SELL
	}

  /** optional comment that can be used for example to further describe why a shout was deleted by system (e.g. during deactivaton of a timeslot)    */
  String comment

  static constraints = {
    comment(nullable: true)
	  executionQuantity(nullable: true)
	  executionPrice(nullable: true)
	  limitPrice(nullable: true)
  }

  static namedQueries = {
    findAllActiveAtTimeslot { timeslot ->
			eq('active', true)
			le('beginTimeslot', timeslot)
			ge('endTimeslot', timeslot)
    }
  }
}