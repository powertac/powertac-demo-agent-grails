<g:javascript library='jquery' plugin='jquery'/>
<g:javascript>
  $(document).ready(function() {
    function updateListing() {
       <g:remoteFunction action="getGameState" controller="gameState"
                         update="gameStatus"/>
    }

    setInterval(updateListing, 1000);
  });
</g:javascript>

<div id='quickStatus'>
  GameStatus: <div id='gameStatus'></div><br/>
  Cash:
</div>