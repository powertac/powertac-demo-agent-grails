import org.powertac.broker.GameState
import org.powertac.broker.api.GameStateType

class BootStrap {

    def init = { servletContext ->
      // initialize game state
      GameState currentState = GameState.get(GameState.SINGLETON_ID)
      if (!currentState) {
        currentState = new GameState()
      }
      currentState.state = GameStateType.STOPPED
      currentState.save()
    }

    def destroy = {
    }
}
