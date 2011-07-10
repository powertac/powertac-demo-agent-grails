package org.powertac.broker

class ShoutRequestController
{

  static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

  def index = {
    redirect(action: "list", params: params)
  }

  def list = {
    params.max = Math.min(params.max ? params.int('max') : 10, 100)
    [shoutRequestInstanceList: ShoutRequest.list(params), shoutRequestInstanceTotal: ShoutRequest.count()]
  }

  def create = {
    def shoutRequestInstance = new ShoutRequest()
    shoutRequestInstance.properties = params
    return [shoutRequestInstance: shoutRequestInstance]
  }

  def save = {
    def shoutRequestInstance = new ShoutRequest(params)
    if (shoutRequestInstance.save(flush: true)) {
      flash.message = "${message(code: 'default.created.message', args: [message(code: 'shoutRequest.label', default: 'ShoutRequest'), shoutRequestInstance.id])}"
      redirect(action: "show", id: shoutRequestInstance.id)
    }
    else {
      render(view: "create", model: [shoutRequestInstance: shoutRequestInstance])
    }
  }

  def show = {
    def shoutRequestInstance = ShoutRequest.get(params.id)
    if (!shoutRequestInstance) {
      flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'shoutRequest.label', default: 'ShoutRequest'), params.id])}"
      redirect(action: "list")
    }
    else {
      [shoutRequestInstance: shoutRequestInstance]
    }
  }

  def edit = {
    def shoutRequestInstance = ShoutRequest.get(params.id)
    if (!shoutRequestInstance) {
      flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'shoutRequest.label', default: 'ShoutRequest'), params.id])}"
      redirect(action: "list")
    }
    else {
      return [shoutRequestInstance: shoutRequestInstance]
    }
  }

  def update = {
    def shoutRequestInstance = ShoutRequest.get(params.id)
    if (shoutRequestInstance) {
      if (params.version) {
        def version = params.version.toLong()
        if (shoutRequestInstance.version > version) {

          shoutRequestInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'shoutRequest.label', default: 'ShoutRequest')] as Object[], "Another user has updated this ShoutRequest while you were editing")
          render(view: "edit", model: [shoutRequestInstance: shoutRequestInstance])
          return
        }
      }
      shoutRequestInstance.properties = params
      if (!shoutRequestInstance.hasErrors() && shoutRequestInstance.save(flush: true)) {
        flash.message = "${message(code: 'default.updated.message', args: [message(code: 'shoutRequest.label', default: 'ShoutRequest'), shoutRequestInstance.id])}"
        redirect(action: "show", id: shoutRequestInstance.id)
      }
      else {
        render(view: "edit", model: [shoutRequestInstance: shoutRequestInstance])
      }
    }
    else {
      flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'shoutRequest.label', default: 'ShoutRequest'), params.id])}"
      redirect(action: "list")
    }
  }

  def delete = {
    def shoutRequestInstance = ShoutRequest.get(params.id)
    if (shoutRequestInstance) {
      try {
        shoutRequestInstance.delete(flush: true)
        flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'shoutRequest.label', default: 'ShoutRequest'), params.id])}"
        redirect(action: "list")
      }
      catch (org.springframework.dao.DataIntegrityViolationException e) {
        flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'shoutRequest.label', default: 'ShoutRequest'), params.id])}"
        redirect(action: "show", id: params.id)
      }
    }
    else {
      flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'shoutRequest.label', default: 'ShoutRequest'), params.id])}"
      redirect(action: "list")
    }
  }
}
