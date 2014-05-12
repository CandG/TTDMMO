/* 
 * Constants for Firebase and the other settings
 */
FbRef = {
    ref: "https://ttdmmoq.firebaseio.com/",
    refD: "https://ttdmmoq.firebaseio.com/data/",
    refQ: "https://ttdmmoq.firebaseio.com/queue/"
};
MapTypes = {
    nothing: "N",
    road: {
        road: "R",
        direct: "RD",
        cross: "RC",
        left: "RDL",
        right: "RDR"
    },
    building: {
        home: {
            home: "BHH",
            skyScraper: "BHS"
        },
        field: {
            wood: "BFW",
            farm: "BFF"
        },
        cityHall: "BC",
        mainStation: "BM",
        stop: {
            sources: {
                farm: "BSSF",
                wood: "BSSW"
            },
            Warehouse: "BSW",
            source: "BSS",
            stop: "BS"
        }
    }
};
var FieldName = [];
FieldName[MapTypes.nothing] = "Grass";
FieldName[MapTypes.building.stop.sources.farm] = "Farm";
FieldName[MapTypes.building.stop.Warehouse] = "Other city";
FieldName[MapTypes.building.stop.sources.wood] = "Wood";
var VehNum = [];
VehNum[MapTypes.building.stop.sources.farm] = 0;
VehNum[MapTypes.building.stop.sources.wood] = 1;
var VehicleTypes = [
    {name: 'Food vehicle (9 000 €)', id: 0, type: "Farm"},
    {name: 'Wood vehicle (10 000 €)', id: 1, type: "Wood"}
    // ,{name: 'Bus (11 000 €)', id: 2}
];
var Manual = [
    {arrow: false, style: {top: '120px', left: '220px'}, styleArrow: {top: '345px', left: '830px'},
        text: "Hi! My name is Ralf and I will help you with start in this game!"},
    {arrow: false, style: {top: '480px', left: '220px'}, styleArrow: {top: '385px', left: '830px'},
        text: "Your city is above. You have to provide transport service for this city."},
    {arrow: false, style: {top: '380px', left: '0px'}, styleArrow: {top: '400px', left: '510px'},
        text: "Well. Now we will build path from your Warehouse to Farm."},
    {arrow: true, style: {top: '380px', left: '200px'}, styleArrow: {top: '340px', left: '793px'},
        text: "I will teach you how to build roads. Start with click on the 'left road' (red arrow wil help you)."},
    {arrow: true, style: {top: '280px', left: '0px'}, styleArrow: {top: '390px', left: '500px'},
        text: "Click on map, where you want build road."},
    {arrow: true, style: {top: '380px', left: '200px'}, styleArrow: {top: '340px', left: '905px'},
        text: "Super! We will continue with the 'cross road' (red arrow wil help you)."},
    //6 
    {arrow: true, style: {top: '280px', left: '0px'}, styleArrow: {top: '420px', left: '570px'},
        text: "Click on map, where you want build road."},
    {arrow: true, style: {top: '380px', left: '200px'}, styleArrow: {top: '340px', left: '850px'},
        text: "We finish path with the 'right road' (red arrow wil help you)."},
    {arrow: true, style: {top: '280px', left: '0px'}, styleArrow: {top: '469px', left: '510px'},
        text: "Click on map, where you want build road."},
    {arrow: false, style: {top: '280px', left: '0px'}, styleArrow: {top: '400px', left: '510px'},
        text: "GREAT! The road between Farm and city Warehouse is done! Now we buy your first vehicle."},
    {arrow: true, style: {top: '280px', left: '0px'}, styleArrow: {top: '530px', left: '845px'},
        text: "Click on button the 'New vehicle' (red arrow wil help you)."},
    //11
    {arrow: true, style: {top: '280px', left: '0px'}, styleArrow: {top: '510px', left: '880px'},
        text: "We need vehicle for food. So vehicle type is correct. Let´s buy it!"},
    {arrow: true, style: {top: '280px', left: '0px'}, styleArrow: {top: '495px', left: '1040px'},
        text: "Hoho! You have the vehicle! Set path for vehicle! Click on Settings next to vehicle."},
    {arrow: true, style: {top: '280px', left: '0px'}, styleArrow: {top: '526px', left: '990px'},
        text: "Red dots show your path. Path starts in Warehouse. You enter path by arrows on right. Start with click at right arrow."},
    {arrow: true, style: {top: '280px', left: '0px'}, styleArrow: {top: '570px', left: '928px'},
        text: "Nice. You are at cross. Now go down to the farm."},
    {arrow: true, style: {top: '280px', left: '0px'}, styleArrow: {top: '610px', left: '1010px'},
        text: "There is Farm so save new path. Click on Save button."},
    //16
    {arrow: false, style: {top: '180px', left: '220px'}, styleArrow: {top: '400px', left: '510px'},
        text: "I hope you know how to play now! You can continue with route to wood factory. Good luck!"}];
            