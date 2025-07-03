// From Mads Kjeldgaard to plays with Leds and Text
// Quarks.install("https://github.com/madskjeldgaard/launchpadx-sc")

(
// Setup and connect
MIDIClient.init();
~lpXM = LaunchpadX.new();
~lpXM.connect();
)

// Write some scrolling text
~lpXM.textScroll("Hello World!", loop:0, speed: rrand(1,20), colourspec: rrand(0,100))

// If looping text scroll, disable it
~lpXM.disableTextScroll();

// Select a layout
~lpXM.selectLayout(9);

// Activate programmer mode (set to 0 to return to live mode)
~lpXM.setLiveModeSwitch(1);

// You can render arbitrary 8x8 images on it
(
var image = Image.new(8, 8);

image.pixels_(
    Int32Array.fill(image.width * image.height, {
        Image.colorToPixel(Color.new255(255.rand,127.rand,255.rand,255))
    })
);

~lpXM.renderPixelGrid(image, type: 2);
)

// Render one static char
~lpXM.renderChar($M, 2, 30);

// Go back to live mode
~lpXM.setLiveModeSwitch(0);

// Select a layout
~lpXM.selectLayout(0);
~lpXM.selectLayout(1);
~lpXM.selectLayout(4);
~lpXM.selectLayout(5);
~lpXM.selectLayout(6);
~lpXM.selectLayout(7);
~lpXM.selectLayout(13);
~lpXM.selectLayout(127);

// Sleep (turn off LEDS)
~lpXM.setSleepMode(0);

// Unsleep
~lpXM.setSleepMode(1);

// Silly example: Display latest code you evaluated on the Launchpad X
// See the Interpreter help file for more info on using the preprocessor
(
this.preProcessor = {|text|
    ~lpXM.textScroll(text, loop:0, speed: rrand(1,20), colourspec: rrand(0,100))
}
)










// Documented and modified from Luca Paris
// https://github.com/lucasParis/SCLaunchpadX/blob/master/LaunchPadX.sc

(
~lpX = LaunchPadX.new();

/*
Launchpad X layout :

 ┌───────────────────────────────┐
 │ Top Row: x = 0 à 8 (y = -1)   │  ← onTopRowDown / onTopRowUp
 ├───────────────────────────────┤
 │        Grille principale      │  ← onPadDown / onPadUp / onPressure
 │      x = 0 à 7, y = 0 à 7     │
 ├───────────────────────────────┤
 │ Colonne droite : y = 0 à 7    │  ← onRightColumnDown / onRightColumnUp
 └───────────────────────────────┘
*/

~lpX.onPadDown = { |x, y| ("Pad pressed at (" ++ x ++ ", " ++ y ++ ")").postln; };
~lpX.onPadUp = { |x, y| ("Pad released at (" ++ x ++ ", " ++ y ++ ")").postln; };
~lpX.onPressure = { |x, y, pressure| ("Pressure at (" ++ x ++ ", " ++ y ++ ") = " ++ pressure).postln; };

~lpX.onTopRowDown = { |x| ("Top row pressed at column: " ++ x).postln; };
~lpX.onTopRowUp = { |x| ("Top row released at column: " ++ x).postln; };

~lpX.onRightColumnDown = { |y| ("Right column pressed at row: " ++ y).postln; };
~lpX.onRightColumnUp = { |y| ("Right column released at row: " ++ y).postln; };

// Couleur / Code approx de 0 à 127
~lpXcolors = (
    black: 0, gray1: 1, gray2: 2, white: 3, red1: 4, red2: 5, red3: 6, red4: 7,
    beige1: 8, orange1: 9, orange2: 10, brown1: 11, yellow1: 12, yellow2: 13, olive1: 14, olive2: 15,
    lime1: 16, lime2: 17, lime3: 18, green1: 19, green2: 20, green3: 21, khaki1: 22, khaki2: 23,
    mint1: 24, mint2: 25, teal1: 26, teal2: 27, turquoise1: 28, turquoise2: 29, green4: 30, green5: 31,
    cyan1: 32, cyan2: 33, cyan3: 34, sea1: 35, aqua1: 36, aqua2: 37, blue1: 38, blue2: 39,
    sky1: 40, sky2: 41, blue3: 42, navy1: 43, navy2: 44, indigo1: 45, indigo2: 46, indigo3: 47,
    purple1: 48, purple2: 49, purple3: 50, violet1: 51, violet2: 52, magenta1: 53, magenta2: 54, magenta3: 55,
    pink1: 56, pink2: 57, pink3: 58, rose1: 59, coral1: 60, coral2: 61, ochre1: 62, ochre2: 63,
    green6: 64, green7: 65, blue4: 66, blue5: 67, blue6: 68, purple4: 69, gray3: 70, gray4: 71,
    red5: 72, yellow3: 73, yellow4: 74, yellow5: 75, peach1: 76, cyan4: 77, cyan5: 78, cyan6: 79,
    purple5: 80, violet3: 81, violet4: 82, brown2: 83, orange3: 84, yellow6: 85, green8: 86, green9: 87,
    mint3: 88, ice1: 89, blue7: 90, gray5: 91, blue8: 92, yellow7: 93, magenta4: 94, pink4: 95,
    sand1: 96, orange4: 97, yellow8: 98, ochre3: 99, ochre4: 100, green10: 101, green11: 102, gray6: 103,
    navy3: 104, brown3: 105, red6: 106, red7: 107, red8: 108, green12: 109, green13: 110, green14: 111,
    gray7: 112, cream1: 113, cream2: 114, gray8: 115, pink5: 116, brown4: 117, gray9: 118, gray10: 119,
    red9: 120, red10: 121, cream3: 122, lime4: 123, yellow9: 124, mustard1: 125, mustard2: 126, brown5: 127
);

~lpX.display; // No colors at the begining
)


// Action sur la couleur Globale
~lpX.setColor(0, 0, 9);
~lpX.setColor(0, 0, ~lpXcolors[\ochre1]);
~lpX.setColor(0, 0, 5); // red2
~lpX.setColor(0, 0, 21); // green3 // comme le 87?
~lpX.setColor(0, 0, 13); // yellow2
~lpX.setColor(0, 0, 37); // aqua2
~lpX.setColor(0, 0, 95); // pink4

~lpX.setColor(8, 0, 9); // On the right


( // Allumer toute la grille en plein de couleurs
8.do { |x|
    8.do { |y|
        ~lpX.setColor(x, y, 127.rand.postln); // vert
    };
};
)


( // Fait défiler couleurs du 1er de 0 à 127
{
(0..127).do { |i|
   ~lpX.setColor(0, 0, i);
    ("Color code: " ++ i).postln;
    0.1.wait;
};
}.fork(AppClock)
)



// Action sur la couleur par Layer

(
// Créer un nouveau layer
~lpX.newDisplayLayer(\layer1);  // Crée un layer nommé "layer1"

// Initialiser des couleurs dans layer1
9.do { |x|
    9.do { |y|
        // Affecter une couleur à une cellule spécifique de layer1
		~lpX.setLayerColor(\layer1, x, y, 5);  // Valeur 5 correspond à une couleur
    }
};

// Créer un autre layer nommé "layer2"
~lpX.newDisplayLayer(\layer2);  // Crée un layer nommé "layer2"

// Initialiser des couleurs dans layer2
9.do { |x|
    9.do { |y|
        // Affecter une couleur différente dans layer2
        ~lpX.setLayerColor(\layer2, x, y, 50);  // Valeur 10 correspond à une autre couleur
    }
};

// Créer un autre layer nommé "layer2"
~lpX.newDisplayLayer(\layer3);  // Crée un layer nommé "layer2"

// Initialiser des couleurs dans layer2
9.do { |x|
    9.do { |y|
        // Affecter une couleur différente dans layer2
        ~lpX.setLayerColor(\layer3, x, y, 100);  // Valeur 10 correspond à une autre couleur
    }
};
)

// Switch between Layers
~lpX.displayLayer(\layer1)
~lpX.displayLayer(\layer2)
~lpX.displayLayer(\layer3)

// Shows the layer behind
~lpX.clearLayer(\layer1);
~lpX.clearLayer(\layer2);
~lpX.clearLayer(\layer3);

~lpX.display; // Effect ? // get all layers // traverse from highest priority, if not zero use this value


// Réinitialiser tous les layers avant de mettre à jour l'affichage
(
9.do { |x|
    9.do { |y|
        // Réinitialiser les couleurs de layer1 et layer2 à zéro (masquer les couches)
        ~lpX.setLayerColor(\layer1, x, y, 0);  // Valeur 0 pour éteindre
        ~lpX.setLayerColor(\layer2, x, y, 0);  // Valeur 0 pour éteindre
		~lpX.setLayerColor(\layer3, x, y, 0);  // Valeur 0 pour éteindre
    }
};
)










MIDIClient.sources.detect{|a|a.name.contains("LPX")};
MIDIClient.destinations.detect{|a|a.name.contains("LPX")};

MIDIClient.sources{|a|a.name.postcs;};

LaunchpadXon.initMIDI;
LaunchpadXon.resetPads;
LaunchpadXon.resetLED
LaunchpadXon.setLED(1,2,LPLED.amber(3).vel);
LaunchpadXon.pads[0,0].value