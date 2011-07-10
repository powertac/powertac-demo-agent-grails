package org.powertac.broker.interfaces

import org.powertac.common.interfaces.TimeslotPhaseProcessor

public interface TimeslotPhaseProcessorWithAutoRegistration extends TimeslotPhaseProcessor {
  def getPhases()
}