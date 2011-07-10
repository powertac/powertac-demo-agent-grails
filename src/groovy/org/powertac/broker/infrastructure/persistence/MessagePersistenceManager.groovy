package org.powertac.broker.infrastructure.persistence

import org.apache.commons.logging.LogFactory
import org.powertac.common.command.SimStart;
import org.powertac.common.msg.TariffStatus
import org.powertac.common.msg.TimeslotUpdate
import org.powertac.common.*

class MessagePersistenceManager
{
  private static final log = LogFactory.getLog(this)
  private long timeslotMillis

  def save (Object obj) {
    log.warn("I don't know how to save ${obj.class.name} message type yet")
  }

  def save (WeatherReport wr) {
    log.debug("save(WeatherReport) - start")
    wr.merge()
    log.debug("save(WeatherReport) - end")
  }

  def save (Orderbook ob) {
    log.debug("save(Orderbook) - start")

    def asks = ob.asks.clone()
    def bids = ob.bids.clone()

    ob.asks.clear()
    ob.bids.clear()

    def timeslot = ob.timeslot
    if (timeslot) {
      timeslot.addToOrderbooks(ob)
    }

    ob.save()

    def processedAsks = []
    asks.each { ask ->
      def dbAsk = OrderbookAsk.findById(ask.id)
      if (dbAsk) {
        ask.version = dbAsk.version
        ask = ask.merge()
      }
      processedAsks << ask
    }

    def processedBids = []
    bids.each { bid ->
      def dbBid = OrderbookBid.findById(bid.id)
      if (dbBid) {
        bid.version = dbBid.version
        bid = bid.merge()
      }
      processedBids << bid
    }

    ob.bids.addAll(processedBids)
    ob.asks.addAll(processedAsks)

    log.debug("save(Orderbook) - end")
  }

  def save (CustomerInfo ci) {
    log.debug("save(CustomerInfo) - start")
    ci.merge()
    log.debug("save(CustomerInfo) - end")
  }

  def save (BankTransaction bt) {
    log.debug("save(BankTransaction) - begin")

    bt.merge()

    log.debug("save(BankTransaction) - end")
  }

  def save (CashPosition cp) {
    log.debug("save(CashPosition) - begin")

    def dbCp = CashPosition.findByBroker(cp.broker)
    if (dbCp) {
      log.debug("save(CashPosition) - found with version ${dbCp.version}")
      cp.id = dbCp.id
      cp.version = dbCp.version
      cp.merge()
    } else {
      log.debug("save(CashPosition) - not found, saving as new")
      cp.id = null
      cp.save(flush: true)
    }

    log.debug("save(CashPosition) - end")
  }

  def save (ClearedTrade ct) {
    log.debug("save(ClearedTrade) - start")

    def dbCt = ClearedTrade.findById(ct.id)
    if (dbCt) {
      ct.version = dbCt.version
    }

    ct.merge()

    log.debug("save(ClearedTrade) - end")
  }

  def save (TariffSpecification ts) {
    log.debug("save(TariffSpecification) - start")

    def processedRates = []

    ts.rates.each { rate ->
      def dbRate = Rate.findById(rate.id)
      if (dbRate) {
        log.debug("save(TariffSpecification) - found [dbRate.id:${dbRate.id},dbRate.version:${dbRate.version},rate.version:${rate.version}]")
        rate.version = dbRate.version
        rate = rate.merge()
        log.debug("save(TariffSpecification) - after merge rate:${rate}")
      } else {
        log.debug("save(TariffSpecification) - not found rate:${rate.id}")
        rate.save()
      }
      processedRates << rate
    }

    log.debug("save(TariffSpecification) - there are ${ts.rates.size()} item in ts.rates")
    log.debug("save(TariffSpecification) - there are ${processedRates.size()} item in processedRates")

    ts.rates.clear()
    ts.rates.addAll(processedRates)

    if (TariffSpecification.findById(ts.id)) {
      ts.merge()
    } else {
      ts.save()
    }

    log.debug("save(TariffSpecification) - end")
  }

  def save (TariffTransaction ttx) {
    log.debug("save(TariffTransaction) - start")

    ttx.merge()

    log.debug("save(TariffTransaction) - receving ${ttx.txType} ttx for ${ttx.broker.username}")
    log.debug("save(TariffTransaction) - end")

  }

  def save (TariffStatus ts) {
    log.debug("save(TariffStatus) - start")

    ts.merge()

    log.debug("save(TariffStatus) - start")
  }


  def save (Competition competition) {
    log.debug("save(Competition) - start")

    competition.brokers?.each {
      log.debug("save(Competition) - populate broker: ${it}")
      def broker = new Broker(username: it, enabled: true)
      broker.save()
    }

    timeslotMillis = competition.timeslotLength * TimeService.MINUTE

    log.debug("save(Competition) - saving competition ${competition}:${competition.save() ? 'successful' : competition.errors}")
    log.debug("save(Competition) - end")
  }

  def save (SimStart simStart) {
    log.debug("save(SimStart) - start")
    log.debug("Saving simStart - start @ ${simStart.start}")

    simStart.save()

    log.debug("save(SimStart) - this: ${this}")
    log.debug("save(SimStart) - end")
  }

  def save (TimeslotUpdate slotUpdate) {
    log.debug("save(TimeslotUpdate) - start")

    log.debug("save(TimeslotUpdate) - received TimeslotUpdate: ${slotUpdate.id}")

    def newEnableds = []
    slotUpdate.enabled?.each {
      it.id = it.serialNumber
      it.enabled = true
      it.endInstant = it.startInstant + timeslotMillis
      log.debug("save(TimeslotUpdate) -    saving enabled timeslot ${it.id}: ${(newEnableds << it.merge()) ? 'successful' : it.errors}")
    }
    slotUpdate.enabled = newEnableds

    def newDisables = []
    slotUpdate.disabled?.each {
      def dbTimeslot = Timeslot.findBySerialNumber(it.serialNumber)
      it.id = it.serialNumber
      it.enabled = false
      it.endInstant = it.startInstant + timeslotMillis
      it.version = dbTimeslot.version
      log.debug("save(TimeslotUpdate) -    saving disabled timeslot ${it.id}: ${(newDisables << it.merge()) ? 'successful' : it.errors}")
    }
    slotUpdate.disabled = newDisables

    log.debug("save(TimeslotUpdate) - saving TimeslotUpdate ${slotUpdate.id}:${slotUpdate.save() ? 'successful' : slotUpdate.errors}")

    log.debug("save(TimeslotUpdate) - end")
  }

  def save (BalancingTransaction bt) {

  }
}
