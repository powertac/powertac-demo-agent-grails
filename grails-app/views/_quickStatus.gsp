<g:javascript library='jquery' plugin='jquery'/>
<g:javascript>
  $(document).ready(function() {

    function updateStatus() {
       <g:remoteFunction action="getGameState" controller="gameStatus"
                         update="gameStatus" onSuccess="updatePauseState();"/>
    }

    function updateBalance() {
      <g:remoteFunction action="getCashBalance" controller="gameStatus"
                        update="cashBalance"/>
    }

    function updatePauseState() {
      <g:remoteFunction action="getPauseActionState" controller="gameStatus"
                        onSuccess="updatePauseActionButton(data)"/>
    }

    setInterval(updateBalance, 5000);
    setInterval(updateStatus, 1000);

    var request = 'NONE';
    function updatePauseActionButton(state) {
      if (state == 'NONE' || state == 'RESUME_REQUESTED') {
        $('#pauseActionButton').val('Request Pause');
      } else {
        $('#pauseActionButton').val('Request Resume');
      }

      if (state == 'NONE' || state == 'PAUSE_ACCEPTED') {
        $('#pauseActionStatus').html(' ')
      }

      $('#debug').html(state)
    }

    $('#pauseActionButton').click(
      function pauseAction(e) {
        $.post("${createLink(controller:'gameStatus',action:'pauseAction')}",
               function(response) {
                $('#pauseActionStatus').html(response);
               },
               'html');
      }
    )


  });
</g:javascript>

<div id='quickStatus'>
  GameStatus: <div id='gameStatus'>
  <img src="${resource(dir: 'images', file: 'spinner.gif')}"
       alt="${message(code: 'spinner.alt', default: 'Loading...')}"/>
  </div><br/>
  Cash: <div id='cashBalance'>
    <img src="${resource(dir: 'images', file: 'spinner.gif')}"
         alt="${message(code: 'spinner.alt', default: 'Loading...')}"/>
  </div><br/>
  <div id='pauseAction'>
    <input type='button' id='pauseActionButton' value='Request Pause'/>
  </div>
  <div id='pauseActionStatus'></div>
  <div style="display: none;" id='debug'></div>
</div>