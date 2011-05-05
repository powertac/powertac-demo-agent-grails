/*
 * Copyright (c) 2011 by the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.powertac.broker.infrastructure.messaging

import org.apache.commons.logging.LogFactory
import org.powertac.common.MessageConverter

class XMLMessageReceiver {
  private static final log = LogFactory.getLog(this)

  MessageListenerRegistrar messageListenerRegistrar
  MessageConverter messageConverter

  def onMessage(String xml) {
    log.debug("XMLMessageReceiver.onMessage(String) - received\n${xml}")

    def obj = messageConverter.fromXML(xml)
    def listeners = messageListenerRegistrar.getRegistrations(obj.class)

    log.debug("XMLMessageReceiver - received ${obj.class.name}")
    log.debug("XMLMessageReceiver - dipatching to ${listeners?.size()} listeners")

    listeners?.each { it.onMessage(obj) }
  }
}
