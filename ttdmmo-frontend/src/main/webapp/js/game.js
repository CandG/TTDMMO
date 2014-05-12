var pathImages = 'images/field/';

var imgBuild = new Image();
imgBuild.src = pathImages + 'imgBuild.jpg';
var imgZas = new Image();
imgZas.src = pathImages + 'imgZas.jpg';
var imgRoad2 = new Image();
imgRoad2.src = pathImages + 'imgRoad2.jpg';
var imgCity = new Image();
imgCity.src = pathImages + 'imgCity.jpg';
var imgCross = new Image();
imgCross.src = pathImages + 'imgCross.jpg';
var imgDepo = new Image();
imgDepo.src = pathImages + 'imgDepo.jpg';
var imgRoad = new Image();
imgRoad.src = pathImages + 'imgRoad.jpg';
var imgGrass = new Image();
imgGrass.src = pathImages + 'imgGrass.jpg';
var imgFarm = new Image();
imgFarm.src = pathImages + 'imgFarm.jpg';
//var imgWoodField = new Image();
//imgWoodField.src = pathImages + 'imgWoodField.jpg';
var imgFarmField = new Image();
imgFarmField.src = pathImages + 'imgFarmField.jpg';

/*Offset time - server time vs. client time*/
var offsetFirebase;
var offsetRef = new Firebase(FbRef.ref + ".info/serverTimeOffset");
offsetRef.on("value", function(snap) {
    offsetFirebase = snap.val();
});


/*
 * Game 
 * Object for storing game data
 * 
 * @returns {Game}
 */
function Game() {
    console.log('Game instantiated');
    var initialized = false;
    var self = this;
    this.objToInsert = '';
    this.keys = {u: 0, d: 0, l: 0, r: 0}; // pressed keys: up, down, left, right
    this.cityMapOpts = {size: 5, padding: 20};
    this.zoom = 1;
    this.firebaseMap = null;
    this.cityMap = null;
    this.radius = 5;
    this.camera = null;
    this.cityCamera = null;
    this.w = 120;
    sheetengine.scene.tilewidth = this.w;
    sheetengine2.scene.tilewidth = this.cityMapOpts.size * this.cityMapOpts.padding;
    this.timer = function() {
        var move = 10;
        x = 0;
        y = 0;
        if (self.keys.u) {
            x = -move;
            y = -move;
        }
        if (self.keys.d) {
            x = move;
            y = move;
        }
        if (self.keys.r) {
            x = move;
            y = -move;
        }
        if (self.keys.l) {
            x = -move;
            y = move;
        }


        if (x || y || sheetengine.dirty) {
            var targetp = {
                x: self.camera.x + x,
                y: self.camera.y + y,
                z: self.camera.z};

            self.moveViewPoint(targetp, self);

            if (sheetengine.dirty !== 0) {
                console.log("redraw");
                sheetengine.dirty = 0;
                sheetengine.calc.calculateAllSheets();
                sheetengine.drawing.drawScene(true);
            }
        }
        if (sheetengine2.dirty) {
            console.log("redraw2");
            sheetengine2.dirty = 0;
            sheetengine2.calc.calculateAllSheets();
            sheetengine2.drawing.drawScene(true);
        }
    };
    onkeydown = function(event) {
        self.setKeys(event, 1);
    };
    onkeyup = function(event) {
        self.setKeys(event, 0);
    };
}

Game.prototype = {
    constructor: Game,
    init: function(position) {
        var canvasElement = document.getElementById('mainCanvas');
        sheetengine.scene.init(canvasElement, {w: 1250, h: 1000});
        var canvasElement2 = document.getElementById('cityCanvas');
        sheetengine2.scene.init(canvasElement2, {w: 850, h: 625});
        // define camera

        this.camera = {x: position.x * this.w, y: position.y * this.w, z: position.z * this.w};
        sheetengine.scene.setCenter({x: this.camera.x, y: this.camera.y, z: this.camera.z});
        sheetengine.scene.translateBackground({x: 0, y: 0}, {x: this.camera.x, y: this.camera.y});

        this.cityCamera = {x: position.x * this.cityMapOpts.size, y: position.y * this.cityMapOpts.size, z: position.z * this.cityMapOpts.size};
        sheetengine2.scene.setCenter({x: this.cityCamera.x, y: this.cityCamera.y, z: this.cityCamera.z});
        sheetengine2.scene.translateBackground({x: 0, y: 0}, {x: this.cityCamera.x, y: this.cityCamera.y});

        /*cityMap rect*/
        var w = 54;
        var h = 40;
        var sheetRect = new sheetengine2.Sheet(
                {x: 0, y: 0, z: 0},
        {alphaD: 0, betaD: 0, gammaD: 45},
        {w: w, h: h}
        );
        sheetRect.context.fillStyle = '#FFFFff';
        sheetRect.context.fillRect(0, 0, w, w);
        sheetRect.context.clearRect(3, 3, w - 6, h - 6);
        //+camera
        var obj = new sheetengine2.SheetObject({x: this.cityCamera.x, y: this.cityCamera.y, z: 10}, {alphaD: 0, betaD: 0, gammaD: 0}, [sheetRect], {w: w, h: h, relu: w / 2, relv: h / 2});
        this.cityMapOpts.rect = obj;
        /*cityMap center*/
        var padding = this.cityMapOpts.padding;
        var size = this.cityMapOpts.size;
        var basesheet = new sheetengine2.BaseSheet({x: 0, y: 0, z: 0}, {alphaD: 270, betaD: 0, gammaD: 0}, {w: padding * size, h: padding * size});
        basesheet.img = imgGrass;

        var yardpos = sheetengine.scene.getYardFromPos(this.camera);

        if (!this.initialized) {
            this.initControls();
            this.cityMap = new CityMap();
            this.cityMap.setLevel(3);
            this.cityMap.setPadding(this.cityMapOpts.padding);
            this.cityMap.setFieldFactory(new SheetengineCityFactory(sheetengine2, this.cityMapOpts.size));
            this.cityMap.init(position);

            this.firebaseMap = new FirebaseMap();
            this.firebaseMap.setLevel(8);
            this.firebaseMap.setFieldFactory(new SheetengineFieldFactory(sheetengine));
            this.firebaseMap.init(position);
        }
        else {
            this.cityMap.reInit(position);
            this.firebaseMap.reInit(position);
        }
        this.initialized = true;
    },
    clickPosition: function(x, y, engine) {
        var yoom = this.zoom;
        if (!engine)
            engine = sheetengine;
        else
            yoom = 1;
        var puv = {
            u: x - engine.canvas.offsetLeft + pageXOffset,
            v: y - engine.canvas.offsetTop + pageYOffset
        };
        var w = engine.canvas.width / 2;
        var h = engine.canvas.height / 2;
        puv.u = (puv.u - w) / yoom + w;
        puv.v = (puv.v - h) / yoom + h;
        var pxy = engine.transforms.inverseTransformPoint({
            u: puv.u + engine.scene.center.u,
            v: puv.v + engine.scene.center.v
        });
        return pxy;
    },
    moveViewPoint: function(targetp, vSelf) {
        if (!vSelf)
            var self = this;
        else
            var self = vSelf;
        var yardpos = sheetengine.scene.getYardFromPos(self.camera);

        var oldcentertile = {x: yardpos.relyardx, y: yardpos.relyardy, z: yardpos.relyardz};
        // move camera
        self.camera = targetp;
        // move center
        sheetengine.scene.setCenter({x: self.camera.x, y: self.camera.y, z: self.camera.z});
        var yardpos2 = sheetengine.scene.getYardFromPos(self.camera);
        //console.debug(oldcentertile);
        if (yardpos.relyardx != yardpos2.relyardx || yardpos.relyardy != yardpos2.relyardy || yardpos.relyardz != yardpos2.relyardz)
        {
            var newpos = {x: yardpos2.relyardx, y: yardpos2.relyardy};
            self.firebaseMap.changePosition(oldcentertile, newpos);
            // translate background
            sheetengine.scene.translateBackground(
                    {x: oldcentertile.x * self.w, y: oldcentertile.y * self.w},
            {x: yardpos2.relyardx * self.w, y: yardpos2.relyardy * self.w}
            );
            this.moveRect({x: (yardpos2.relyardx - oldcentertile.x) * this.cityMapOpts.size, y: (yardpos2.relyardy - oldcentertile.y) * this.cityMapOpts.size, z: 0});
            sheetengine.dirty = 0;
            console.log("redraw");
            sheetengine.calc.calculateAllSheets();
            sheetengine.drawing.drawScene(true);
        }
    },
    moveViewPointCity: function(targetp, vSelf) {
        if (!vSelf)
            var self = this;
        else
            var self = vSelf;
        var yardpos = sheetengine2.scene.getYardFromPos(self.cityCamera);

        var oldcentertile = {x: yardpos.relyardx * self.cityMapOpts.padding, y: yardpos.relyardy * self.cityMapOpts.padding, z: yardpos.relyardz};
        // move camera
        self.cityCamera = targetp;
        // move center
        sheetengine2.scene.setCenter({x: self.cityCamera.x, y: self.cityCamera.y, z: self.cityCamera.z});
        var yardpos2 = sheetengine2.scene.getYardFromPos(self.cityCamera);
        console.debug(oldcentertile);
        if (yardpos.relyardx != yardpos2.relyardx || yardpos.relyardy != yardpos2.relyardy || yardpos.relyardz != yardpos2.relyardz)
        {
            var newpos = {x: yardpos2.relyardx * self.cityMapOpts.padding, y: yardpos2.relyardy * self.cityMapOpts.padding};
            self.cityMap.changePosition(oldcentertile, newpos);
            sheetengine2.scene.translateBackground(
                    {x: yardpos.relyardx * sheetengine2.scene.tilewidth, y: yardpos.relyardy * sheetengine2.scene.tilewidth},
            {x: yardpos2.relyardx * sheetengine2.scene.tilewidth, y: yardpos2.relyardy * sheetengine2.scene.tilewidth}
            );
            sheetengine2.dirty = 0;
            sheetengine2.calc.calculateAllSheets();
            sheetengine2.drawing.drawScene(true);
        }
    },
    moveRect: function(position) {
        console.debug(position);
        this.cityMapOpts.rect.move(position);
        sheetengine2.calc.calculateAllSheets();
        sheetengine2.drawing.drawScene(true);
    },
    initControls: function() {
        $(window).keydown(this.onkeydown);
        $(window).keyup(this.onkeyup);
        // main loop
        setInterval(this.timer, 30);
    },
    setKeys: function(event, val) {
        var keyProcessed = 0;
        if (event.keyCode == '38') {
            this.keys.u = val;
            keyProcessed = 1;
        }
        if (event.keyCode == '37') {
            this.keys.l = val;
            keyProcessed = 1;
        }
        if (event.keyCode == '39') {
            this.keys.r = val;
            keyProcessed = 1;
        }
        if (event.keyCode == '40') {
            this.keys.d = val;
            keyProcessed = 1;
        }
        if (keyProcessed)
            event.preventDefault();
    }

};


var traman = new Game();

