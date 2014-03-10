var pathImages = 'images/field/';
var imgRoad = new Image();
var imgGrass = new Image();
var imgZas = new Image();
var imgBuild = new Image();
imgBuild.src = pathImages + 'imgBuild.jpg';
imgZas.src = pathImages + 'imgZas.jpg';

var imgRoad2 = new Image();
imgRoad2.src = pathImages + 'imgRoad2.jpg';

var imgCity = new Image();
imgCity.src = pathImages + 'imgCity.jpg';

var imgCross = new Image();
imgCross.src = pathImages + 'imgCross.jpg';

var imgDepo = new Image();
imgDepo.src = pathImages + 'imgDepo.jpg';

imgRoad.src = pathImages + 'imgRoad.jpg';
imgGrass.src = pathImages + 'imgGrass.jpg';
var imgFarm = new Image();
imgFarm.src = pathImages + 'imgFarm.jpg';

function Game() {
    console.log('Game instantiated');
    var initialized = false;
    var self = this;
    this.objToInsert = '';
    this.keys = {u: 0, d: 0, l: 0, r: 0}; // pressed keys: up, down, left, right
    this.boundary = {}; // boundary of user movements without loading new yards
    this.cityMap = {size: 5, padding: 20};
    this.zoom = 1;
    this.firebaseMap = null;
    this.radius = 5;
    this.camera = null;
    this.max = 10;
    this.w = 120;
    sheetengine.scene.tilewidth = this.w;
    this.timer = function() {
        var move = 10;
        x = 0;
        y = 0;
        if (self.keys.u) {
            x = -move;
        }
        if (self.keys.d) {
            x = move;
        }
        if (self.keys.r) {
            y = -move;
        }
        if (self.keys.l) {
            y = +move;
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
            /*sheetengine.calc.calculateChangedSheets();
             sheetengine.drawing.drawScene();*/
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
        this.initialized = true;
        canvasElement = document.getElementById('mainCanvas');
        sheetengine.scene.init(canvasElement, {w: 1450, h: 1225});
        var canvasElement2 = document.getElementById('cityCanvas');
        sheetengine2.scene.init(canvasElement2, {w: 1450, h: 1225});
        // define camera
        this.camera = {x: position.x * this.w, y: position.y * this.w, z: position.z * this.w};
        sheetengine.scene.setCenter({x: this.camera.x, y: this.camera.y, z: this.camera.z});
        sheetengine.scene.translateBackground(
                {x: 0, y: 0},
        {x: this.camera.x, y: this.camera.y}
        );


        var w = 60;
        var h = 40;
        var sheetRect = new sheetengine2.Sheet(
                {x: 0, y: 0, z: 0},
        {alphaD: 0, betaD: 0, gammaD: 45},
        {w: w, h: h}
        );
        sheetRect.context.fillStyle = '#FFFFff';
        sheetRect.context.fillRect(0, 0, w, w);
        sheetRect.context.clearRect(3, 3, w - 6, h - 6);
        var obj = new sheetengine2.SheetObject({x: position.x * this.cityMap.size, y: position.y * this.cityMap.size, z: 10}, {alphaD: 0, betaD: 0, gammaD: 0}, [sheetRect], {w: w, h: h, relu: w / 2, relv: h / 2});

        this.cityMap.rect = obj;


        // get relative yard coordinates and set initial boundary for visible yards
        var yardpos = sheetengine.scene.getYardFromPos(this.camera);
        this.setBoundary(yardpos);
        this.firebaseMap = new FirebaseMap();
        this.firebaseMap.setLevel(8);
        this.firebaseMap.setFieldFactory(new SheetengineFieldFactory(sheetengine));
        this.firebaseMap.init(position);

        this.initControls();
    },
    initCityMap: function(city, isMine) {
        var padding = this.cityMap.padding;
        var size = this.cityMap.size;
        var position = XYFromIndex(city.xy);
        var basesheet = new sheetengine2.BaseSheet({x: position.x * size, y: position.y * size, z: 0}, {alphaD: 270, betaD: 0, gammaD: 0}, {w: padding * size, h: padding * size});
        basesheet.img = imgCity;

        var w = 140;
        var h = 20;
        var sheetText = new sheetengine2.Sheet(
                {x: 0, y: 0, z: 0},
        {alphaD: 0, betaD: 0, gammaD: 45},
        {w: w, h: h}
        );
        sheetText.context.font = "13px sans-serif";
        if (isMine)
            sheetText.context.fillStyle = "#ff0000";
        else
            sheetText.context.fillStyle = "#ffffff";
        sheetText.context.textAlign = "center";
        sheetText.context.fillText(city.name, w / 2, h - 10);

        var obj = new sheetengine2.SheetObject({x: position.x * size, y: position.y * size, z: 10}, {alphaD: 0, betaD: 0, gammaD: 0}, [sheetText], {w: w, h: h, relu: w / 2, relv: h / 2});


        sheetengine2.calc.calculateAllSheets();
        sheetengine2.drawing.drawScene(true);
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
        //console.debug(yardpos2);
        //console.debug(sheetengine.scene.tilewidth);
        //console.debug(demo.camera);
        if (yardpos.relyardx != yardpos2.relyardx || yardpos.relyardy != yardpos2.relyardy || yardpos.relyardz != yardpos2.relyardz)
        {
            //  set new boundary
            self.setBoundary(yardpos2);
            var newpos = {x: yardpos2.relyardx, y: yardpos2.relyardy};
            self.firebaseMap.changePosition(oldcentertile, newpos);
            // translate background
            sheetengine.scene.translateBackground(
                    {x: oldcentertile.x * self.w, y: oldcentertile.y * self.w},
            {x: yardpos2.relyardx * self.w, y: yardpos2.relyardy * self.w}
            );
            this.moveRect({x: (yardpos2.relyardx - oldcentertile.x) * this.cityMap.size, y: (yardpos2.relyardy - oldcentertile.y) * this.cityMap.size, z: 0});
            sheetengine.dirty = 0;
            sheetengine.calc.calculateAllSheets();
            sheetengine.drawing.drawScene(true);
        }
    },
    moveRect: function(position) {
        console.debug(position);
        this.cityMap.rect.move(position);
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
    },
    setBoundary: function(yardpos) {
        // for boundary we use relative yard coordinates
        var radius = this.radius;
        this.boundary = {
            xmin: (yardpos.relyardx - radius) * sheetengine.scene.tilewidth,
            ymin: (yardpos.relyardy - radius) * sheetengine.scene.tilewidth,
            xmax: (yardpos.relyardx + radius) * sheetengine.scene.tilewidth,
            ymax: (yardpos.relyardy + radius) * sheetengine.scene.tilewidth
        };
    }

};




