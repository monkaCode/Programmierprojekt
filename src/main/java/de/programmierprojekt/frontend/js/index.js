let map = L.map("map").setView([50.81, 10.21], 6);

let source = {lat:null, lng:null, id:null};
let target = {lat:null, lng:null, id:null};
let chooseSource = false;
let chooseTarget = false;

let path = null;


L.tileLayer("https://tile.openstreetmap.org/{z}/{x}/{y}.png", {
  maxZoom: 19,
  attribution:
    '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>',
}).addTo(map);

let popup = L.popup();

function onMapClick(e) {
  popup
    .setLatLng(e.latlng)
    .setContent(
      "You clicked the map at " +
        e.latlng.toString() 
    )
    .openOn(map);

    if(chooseSource) {
      let result = getLatLong(e.latlng.lat, e.latlng.lng).split("+");
      
      const nodeID = result[0];
      const lat = result[1];
      const lng = result[2];

      source.id = nodeID;
      source.lat = lat;
      source.lng = lng;
      
      $("#srcNode").text("lat: " + lat + " lng:" + lng + " (" + nodeID + ")");
      
      chooseSource = false;
    } else if(chooseTarget) {
      let result = getLatLong(e.latlng.lat, e.latlng.lng).split("+");
      
      
      const nodeID = result[0];
      const lat = result[1];
      const lng = result[2];
      
      target.id = nodeID;
      target.lat = lat;
      target.lng = lng;

      $("#trgNode").text("lat: " + lat + " lng:" + lng + " (" + nodeID + ")");
      
      chooseTarget = false;
    }
  }
  
  function getLatLong(lat, lng) {
    let url = "http://localhost:8000/closestNode?lat=" + lat + "&lng=" + lng;
    
    var xmlHttp = new XMLHttpRequest();
    xmlHttp.open( "GET", url, false ); // false for synchronous request
    xmlHttp.send( null );
    return xmlHttp.responseText;
  }
  
  map.on("click", onMapClick);
  
  function setSource() {
    chooseSource = true;
  }
  
  function setTarget() {
    chooseTarget = true;
  }
  
  function dijkstra() {
    if(source.id != null && target.id != null) {
      let url = "http://localhost:8000/dijkstra?start=" + source.id + "&end=" + target.id;
      
      var xmlHttp = new XMLHttpRequest();
      xmlHttp.open( "GET", url, false ); // false for synchronous request
      xmlHttp.send( null );
      
      if(path != null) {
        map.removeLayer(path);
      }
      path = L.geoJSON(JSON.parse(xmlHttp.responseText)).addTo(map);

    } else {
      alert("Choose your start and end node");
    }
  }
  
  function clean() {
    console.log(source.id + " ." + source.lat + " ." + 
    source.lng);

    console.log(target.id + " ." + target.lat + " ." + 
    target.lng);

    source.id = null;
    source.lat = null;
    source.lng = null;
    
    target.id = null;
    target.lat = null;
    target.lng = null;
    
    if(path != null) {
      map.removeLayer(path);
    }

    $("#trgNode").text("");
    $("#srcNode").text("");
}