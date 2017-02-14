/**
 * Functions for admin portion
 */
var socket;
var stats;

function connect() {
	if ('WebSocket' in window)
	{
		  console.log('Websocket supported');
		  socket = new WebSocket('ws://localhost:8080/WebSocks/test1?admin');

		  console.log('Connection attempted');

		  socket.onopen = function()
		  {
			  console.log('Connection open!');
		  }

		  socket.onclose = function()
		  {
			  console.log('Disconnected');
		  }

		  socket.onmessage = function (evt) 
		  { 
		      var msg = evt.data;
		      console.log(msg);
		      console.log('message received!');
		      
		      if(msg.split(":")[0] == "gameCount")
		      {
		    	  console.log("GameCount = " + msg.split(":")[1]);
		    	  document.getElementById("games").innerHTML = "Active games: " + msg.split(":")[1];
		      }
		      else if(msg.split(":")[0] == "playerCount")
		      {
		    	  console.log("PlayerCount = " + msg.split(":")[1]);
		    	  document.getElementById("players").innerHTML = "Connected Players: " + msg.split(":")[1];
		      }
		  }

	} 
	else 
	{
		console.log('Websocket not supported');
	}
	stats = setInterval(function () {getStats()}, 10000);
}

function getStats()
{
	socket.send("gameCount");
	socket.send("playerCount");
}