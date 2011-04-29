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

import org.powertac.broker.interfaces.MessageListener

class MessageListenerRegistrar {
  def registrations = [:]

  public void register(Class clazz, MessageListener listener) {
    def listeners = registrations.get(clazz)
    if (!listeners) {
      listeners = [] as Set;
      registrations.put(clazz, listeners)
    }
    listeners << listener
  }

  public void unregister(Class clazz, MessageListener listener) {
    def listeners = registrations.get(clazz)
    if (listeners) {
      listeners.remove(listener)
    }
  }

  Set<MessageListener> getRegistrations(Class clazz) {
    registrations.get(clazz)?.clone()
  }
}
