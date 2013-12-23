function MyGeoFire(firebaseRef) {
    geoFire.call(this, firebaseRef);
}

MyGeoFire.prototype = Object.create(geoFire.prototype);

/*
 * level - how mutch points around center 
 * ***
 * *c*  = 1
 * ***
 * 
 * *****
 * **c** = 2
 * *****
 * 
 * etc.
 * 
 * density - how much points 1/10/100..etc
 * 
 * 
 * 158 - count of km from point 0,0 to 1,1 in degrees 
 * */
MyGeoFire.prototype.distRect = function distRect(level, density) {
    return level * (158 / density);
}
