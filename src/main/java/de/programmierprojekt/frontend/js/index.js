let map = L.map("map").setView([50.81, 10.21], 6);


let source = {lat:null, lng:null, id:null};
let target = {lat:null, lng:null, id:null};
let chooseSource = false;
let chooseTarget = false;


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
      
      $("#srcNode").text("lat: " + lat + " lng:" + lng + " (" + nodeID + ")");
      
      chooseSource = false;
    } else if(chooseTarget) {
      let result = getLatLong(e.latlng.lat, e.latlng.lng).split("+");
      
      
      const nodeID = result[0];
      const lat = result[1];
      const lng = result[2];
      
      $("#trgNode").text("lat: " + lat + " lng:" + lng + " (" + nodeID + ")");

      chooseTarget = false;
    }
}

function getLatLong(lat, lng) {
  let url = "http://localhost:8000/closestNode?lat=" + lat + "&lng=" + lng;

  var xmlHttp = new XMLHttpRequest();
  xmlHttp.open( "GET", url, false ); // false for synchronous request
  xmlHttp.send( null );
  console.log(xmlHttp.responseText + "------");
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
}

function clear() {
  source.id = null;
  source.lat = null;
  source.lng = null;

  target.id = null;
  target.lat = null;
  target.lng = null;

}