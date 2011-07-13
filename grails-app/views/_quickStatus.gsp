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

    setInterval(updateBalance, 5000);
    setInterval(updateStatus, 1000);

    var request = 'NONE';
    function updatePauseState() {
      var state = $('#gameStatus').html()

      if (state == 'STOPPED') {
        $('#pauseActionButton').val('Request Pause');
      } else if (state == 'RUNNING') {
        if (request == 'RESUME') {
          request = 'NONE'
          $('#pauseActionStatus').html(' ')
        }
        $('#pauseActionButton').val('Request Pause');
      } else if (state == 'PAUSED') {
        if (request == 'PAUSE') {
          request = 'NONE'
          $('#pauseActionStatus').html(' ')
        }
        $('#pauseActionButton').val('Request Resume');
      }
    }

    $('#pauseActionButton').click(
      function pauseAction(e) {
        var requestTxt = $('#pauseActionButton').val()
        if (requestTxt == 'Request Resume') {
          request = 'RESUME'
        } else {
          request = 'PAUSE'
        }

        $.post("${createLink(controller:'gameStatus',action:'pauseAction')}",
               function(response) {
                $('#pauseActionStatus').html(response);
               },
               'html');
         $('#pauseActionStatus').html('Request submitted')
      })
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
</div>