package org.powertac.broker.infrastructure.persistence

import org.apache.commons.logging.LogFactory
import org.powertac.common.WeatherReport
import org.powertac.common.Orderbook
import org.powertac.common.CustomerInfo
import org.powertac.common.BankTransaction
import org.powertac.common.CashPosition
import org.powertac.common.ClearedTrade
import org.powertac.common.TariffSpecification
import org.powertac.common.TariffTransaction
import org.powertac.common.msg.TariffStatus
import org.powertac.common.Competition
import org.powertac.common.msg.SimStart
import org.powertac.common.msg.TimeslotUpdate
import org.powertac.common.BalancingTransaction
import org.powertac.common.DistributionTransaction
import org.powertac.common.Broker
import org.powertac.common.repo.BrokerRepo
import org.powertac.common.repo.CustomerRepo
import org.powertac.common.repo.OrderbookRepo
import org.powertac.common.repo.TariffRepo
import org.powertac.common.repo.TimeslotRepo
import org.powertac.common.repo.WeatherForecastRepo
import org.powertac.common.repo.WeatherReportRepo

import org.powertac.common.TimeService
import org.powertac.common.repo.DomainRepo
import org.powertac.common.WeatherForecast
import org.powertac.common.PluginConfig
import org.powertac.common.msg.CustomerBootstrapData
import org.powertac.common.msg.MarketBootstrapData

class RepoBasedMessagePersistenceManager {
  private static final log = LogFactory.getLog(this)
  private long timeslotMillis
	private Map<Class,HashMap> repoMap = [:]

	// injection
	BrokerRepo brokerRepo
	CustomerRepo customerRepo
	OrderbookRepo orderbookRepo
	TariffRepo tariffRepo
	TimeslotRepo timeslotRepo
	WeatherForecastRepo weatherForecastRepo
	WeatherReportRepo weatherReportRepo


	private void saveToRepo (Object msg) {
		Class type = msg.getClass()
		def	repo = repoMap.get(msg.getClass())
		if (repo == null) {
			repo = [:]
			repoMap.put(type, repo)
		}
		repo.put(msg.id, msg)
	}

	def get (type, id = null) {
		def msg = null
		def repo = repoMap.get(type)
		if (id) {
			msg = repo?.get(id)
		} else {
			msg = repo?.values()?.iterator().next()
		}
		msg
	}

	def getAll (type) {
		def repo = repoMap.get(type)
		repo?.values()
	}


  def save (Object obj) {
    log.warn("I don't know how to save ${obj.class.name} message type yet")
  }

  def save (WeatherReport wr) {
    log.debug("save(WeatherReport) - start")

	  weatherReportRepo.add(wr)

	  log.debug("save(WeatherReport) - end")
  }

  def save (Orderbook ob) {
    log.debug("save(Orderbook) - start")

//	  orderbookRepo.makeOrderbook(ob.timeslot, ob.clearingPrice)
	  saveToRepo(ob)

    log.debug("save(Orderbook) - end")
  }

  def save (CustomerInfo ci) {
    log.debug("save(CustomerInfo) - start")

	  customerRepo.add(ci)

	  log.debug("save(CustomerInfo) - end")
  }

  def save (BankTransaction bt) {
    log.debug("save(BankTransaction) - begin")

		saveToRepo(bt)

	  log.debug("save(BankTransaction) - end")
  }

  def save (CashPosition cp) {
    log.debug("save(CashPosition) - begin")

	  cp.broker.cash = cp
		saveToRepo(cp)

    log.debug("save(CashPosition) - end")
  }

  def save (ClearedTrade ct) {
    log.debug("save(ClearedTrade) - start")

	  saveToRepo(ct)

    log.debug("save(ClearedTrade) - end")
  }

  def save (TariffSpecification ts) {
    log.debug("save(TariffSpecification) - start")

	  tariffRepo.addSpecification(ts)

    log.debug("save(TariffSpecification) - end")
  }

  def save (TariffTransaction ttx) {
    log.debug("save(TariffTransaction) - start")

	  saveToRepo(ttx)

    log.debug("save(TariffTransaction) - receving ${ttx.txType} ttx for ${ttx.broker.username}")
    log.debug("save(TariffTransaction) - end")

  }

  def save (TariffStatus ts) {
    log.debug("save(TariffStatus) - start")

	  saveToRepo(ts)

    log.debug("save(TariffStatus) - start")
  }

  def save (Competition competition) {
    log.debug("save(Competition) - start")

    competition.brokers?.each { username ->
      log.debug("save(Competition) - populate broker: ${username}")

	    if (!brokerRepo.findByUsername(username)) {
        def broker = new Broker(username)
		    broker.setEnabled(true)
		    brokerRepo.add(broker)
	    }
    }

	  competition.customers?.each { customer ->
	    customerRepo.add(customer)
	  }

    timeslotMillis = competition.timeslotLength * TimeService.MINUTE

	  Competition.newInstance(competition.name).update(competition)
	  saveToRepo(Competition.currentCompetition())

    log.debug("save(Competition) - end")
  }

  def save (SimStart simStart) {
    log.debug("save(SimStart) - start")
    log.debug("Saving simStart - start @ ${simStart.start}")

	  saveToRepo(simStart)

    log.debug("save(SimStart) - this: ${this}")
    log.debug("save(SimStart) - end")
  }

  def save (TimeslotUpdate slotUpdate) {
    log.debug("save(TimeslotUpdate) - start")

    log.debug("save(TimeslotUpdate) - received TimeslotUpdate: ${slotUpdate.id}")

	  saveToRepo(slotUpdate)
	  slotUpdate.enabled.each { timeslot ->
		 	//  timeslotRepo.
	  }

    log.debug("save(TimeslotUpdate) - end")
  }

  def save (BalancingTransaction bt) {
	  log.debug("save(BalancingTransaction) - start")
		saveToRepo(bt)
	  log.debug("save(BalancingTransaction) - end")
  }

  def save (DistributionTransaction dt) {
	  log.debug("save(DistributionTransaction) - start")
		saveToRepo(dt)
	  log.debug("save(DistributionTransaction) - end")
  }

	def save (WeatherForecast wf) {
		log.debug("save(WeatherForecast) - start")
		saveToRepo(wf)
		log.debug("save(WeatherForecast) - end")
	}

	def save(PluginConfig pc) {
		log.debug("save(PluginConfig) - start")
		saveToRepo(pc)
		log.debug("save(PluginConfig) - end")
	}

	def save(CustomerBootstrapData cbd) {
		log.debug("save(CustomerBootstrapData) - start")
		saveToRepo(cbd)
		log.debug("save(CustomerBootstrapData) - end")
	}

	def save(MarketBootstrapData mbd) {
		log.debug("save(MarketBootstrapData) - start")
		saveToRepo(mbd)
		log.debug("save(MarketBootstrapData) - end")
	}

}