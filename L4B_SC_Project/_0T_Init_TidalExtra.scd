// Extra TidalCycles Personal configuration - with SuperDirt / SuperDirtMixer / TidalLooper / mi-ugens synthDefs
// Example for loading SuperDirt TidalCycles if you also want to use TidalCycles on main Server s
// -> ~mainServer1Window sould be put at 0 in the file "_0B_Init_Config"
// Synchronization via Ableton Link


( // TIDAL Xon - Live 4 Bubbles

~tidalOutputChannels = 2; // Nb of Channels in Tidal
~tidalNbOfLoudspeakers = 8; // Nb of Loudspeakers

// GUI Size and Position parameters
// Size for 4 Tracks
~tidalNbOfOrbits = 4;
~tidalWindowSize = 530;
~tidalWindowMasterSize = 100;
~tidalWindowMixerSize = 300;

/*
// Size for 8 Tracks
~tidalNbOfOrbits = 8;
~tidalWindowSize = 820;
~tidalWindowMasterSize = 100;
~tidalWindowMixerSize = 592;
*/

~tidalWindowPosX = 0 /*1200*/;
~tidalWindowPosY = 250;

// For optimal position with Live 4 Life
~tidalWindowPosX = 1600 /*1200*/;
~tidalWindowPosY = 55;

~tidalMasterMul = 1.ampdb; // Initialisation du conrôle du Master en dB

// Server Options
s.options.numInputBusChannels = 0;
s.options.numOutputBusChannels = ~tidalNbOfLoudspeakers;
s.options.memSize = /*8192 * 256*/ (2.pow(20));
s.options.numBuffers = 1024 * 16;
s.options.maxNodes = /*1024 * 256*/ (8192*500);
// s.latency -> Too many lates ??? with some effects and functions

// Structured Folder of sound files - Base de données sonores XXX
~tidalsoundsFolder = "/Users/xon/Documents/Projets/SC_LFL/Live4LifeProjects/TechnoMachineS/_Sounds2024A/";

// Names of sound folders
~arrayOfFolderNames4Tidal = ("ls -m" + ~tidalsoundsFolder).unixCmdGetStdOut;
~arrayOfFolderNames4Tidal = ~arrayOfFolderNames4Tidal.split($, ).collect { |i| i.stripWhiteSpace };
~arrayOfFolderNames4Tidal = ~arrayOfFolderNames4Tidal.collect { |i| i.replace(" ","_")};

// Distinction des dossiers sonores
~arrayOfFolderNames4TidalSizeS0 = 18; ~arrayOfFolderNames4TidalSizeS1 = 29; ~arrayOfFolderNames4TidalSizeS2 = 67; ~arrayOfFolderNames4TidalSizeS3 = 76; ~arrayOfFolderNames4TidalSizeS4 = 99;

~arrayOfFolderNames4TidalSize = (~arrayOfFolderNames4Tidal.size-1) ! ~tidalNbOfOrbits;
~tidalFolderSizeSwitch = 0 ! ~tidalNbOfOrbits;

~patternSpeedSwitch = 0 ! ~tidalNbOfOrbits;
~patternSpeeds = (0..64) ! ~tidalNbOfOrbits;
~patternSpeedsSize = (~patternSpeeds[0].size-1) ! ~tidalNbOfOrbits;

~tidalEvalPos = 0 ! ~tidalNbOfOrbits;
~tidalSeqLineBegin = 1;
~tidalSeqLineEnd = /*26*/ 92; // Put the number of lines in Pulsar Tidal code XXXXXXXXXXXX
~tidalEvalLine = (1..~tidalSeqLineEnd) ! ~tidalNbOfOrbits;
// ~tidalEvalLine[0] = (1..26); ~tidalEvalLine[1] = (1..26); ~tidalEvalLine[2] = (1..26); ~tidalEvalLine[3] = (1..26);

~tidalnumEffects = 122; // Put the value of fxR in Tidal Cycles (-1?)
~tidalnumFunctions = 255; // Put the value of fuR in Tidal Cycles (-1?)
~tidalnumWhenS = 39; // Put the length of the list sfxx
~tidalnumSwitchX = 20; // Put the length of the list sfux

~tidalTrackSelection = (0..7);
~tidalCurrentOrbit = 0;
~tidalFx10ARandSwitch = 0; ~tidalFx20ARandSwitch = 0; ~tidalFx0ARandSwitch = 0;
~tidalFu10ARandSwitch = 0; ~tidalFu20ARandSwitch = 0; ~tidalFu0ARandSwitch = 0; ~tidalBuf0ARandSwitch = 0;

~tidalRecTemp = 0 ! 3; // To record 3 temporary states / presets - To do
~tidalPresetPos = 0 ! ~tidalNbOfOrbits;
~presetNb = 0 ! ~tidalNbOfOrbits; // To limit the action on GUI only when necessary
~previousPresetNb = 0 ! ~tidalNbOfOrbits;
~orbitUpdateNb = 0;

// To get ~tidalTrackPresetListView.items.size before the initialzation of the GUI
~tidalPresetTrackSize = PathName("/Users/xon/Library/Application Support/SuperCollider/Extensions/SuperDirtMixerX/presetsTr/").files.size;
~tidalPresetLineBegin = 1;
~tidalPresetLineEnd = ~tidalPresetTrackSize;

~superDirtOSC = NetAddr.new("127.0.0.1", 57120);

s.waitForBoot {
	~dirt = SuperDirt(~tidalOutputChannels, s);
	~dirt.loadSoundFiles(~tidalsoundsFolder++"*"); // ~dirt.loadSoundFiles;
	s.sync; // Wait for samples to be read
	~dirt.start(57120, 0 ! ~tidalNbOfOrbits); // Start listening on port 57120, create 8 orbits

	// Taille des Fichiers de Tidal // ~dirt.soundLibrary.buffers.size
	// ~dirt.soundLibrary.bufferEvents.keys.asArray // Get info with bufferEvents
	~tidalFolderSizes = (~dirt.soundLibrary.buffers.size+1).collect { |i| ~dirt.soundLibrary.buffers[~arrayOfFolderNames4Tidal[i].asSymbol].size-1 };

	// Initialize the SuperDirtMixer
	~tidalX = SuperDirtMixerX(~dirt/*, 6010*/);
	// You can adjust parameters before you use the ui
	~tidalX.setOrbitLabels(["1 Fuk", "2 Pee", "3 Pou", "4 Nah", "5 Lead", "6 Bass", "7 Key", "8 Pad"]);
	~tidalX.setMasterBus(0);

	if (~tidalOutputChannels != 32, {

			( // Réverb Custom VST
				~dirtNumChannels = ~dirt.numChannels;

				// This is VST Plugin synthdef example for a global reverb fx
				SynthDef(\valhalla2, {|out|
					var size = 14; // This value needs to be hard coded and means that it works for 14 orbits.
					var dryBusses = NamedControl.kr(\dryBusses, (0 ! size ));
					var wetReverbs = NamedControl.kr(\wetReverbs, (0 ! size)); // Contains the amount of dry/wet for each orbit. Value is between 0 and 1.
					var fxSound = size.collect({arg i; In.ar(dryBusses[i], ~dirtNumChannels) * wetReverbs[i]}).sum;

					fxSound = VSTPlugin.ar(fxSound, numOut: ~dirtNumChannels, id: \valhalla);

					// out should be a global fx bus for reverb.
					// With this you could add more of these fx busses without affecting other audio busses.
					Out.ar(out ,fxSound);
				}).add;

				SynthDef(\masterSynth, { |out, fxBus|
					var size = 8; // This value needs to be hard coded and means that it works for 8 orbits.
					var wetSound = In.ar(fxBus, ~dirtNumChannels);
					var dryBusses = NamedControl.kr(\dryBusses, (0 ! size ));
					var wetSums = NamedControl.kr(\wetSums, (0 ! size));

					// Reduces the dry bus gain logarithmically for each orbit based on the
					// global fx amount.
					var drySound = size.collect({
						arg i;
						ReplaceOut.ar (dryBusses[i], In.ar(dryBusses[i], ~dirtNumChannels) * (1/(wetSums[i] +1) ));
					});

					// Route the global fx bus to i.e. hardware output
					Out.ar(out, wetSound);
				}).add;
			);

			s.sync;

			(
				~tidalMasterBus = Bus.new; // Usually this is the output of your sound card (it returns the first bus 0)
				~tidalGlobalReverbBus = Bus.audio(~dirt.server, ~dirtNumChannels);
				~tidalWetReverbs = Array.fill(~dirt.orbits.size, 0);
				~tidalWetSums = Array.fill(~dirt.orbits.size, 0);
				~tidalDryBusses = ~dirt.orbits.collect({arg item; item.dryBus});
				~tidalGlobalFXBusses = ~dirt.orbits.collect({arg item; item.globalEffectBus});

				// Initilaize the reverb synth
				// Ensure that this Synth be played at the end of the Synth group
				~valhallaSynth = Synth(\valhalla2, [
					\out, ~tidalGlobalReverbBus,
					id: \valhalla
				], addAction: 'addToTail');

				// Namedcontrol to set all the dryBusses for the global fx synth
				~valhallaSynth.set(\dryBusses, ~tidalDryBusses);

				// Link synth with vst plugin and load it
				~valhalla = VSTPluginController(~valhallaSynth, id: \valhalla);
				~valhalla.open("ValhallaSupermassive.vst3");

				// Test the settings and play around with them
				{~valhalla.editor}.defer(1);

				// The master synth routes the content of the fxBus to an out bus
				// i.e. the hardware output
				~masterSynth = Synth(\masterSynth, [
					\out, ~tidalMasterBus,
					\fxBus, ~tidalGlobalReverbBus,
				], addAction: 'addToTail');

				~masterSynth.set(\dryBusses, ~tidalDryBusses);
				~masterSynth.set(\globalFXBusses, ~tidalGlobalFXBusses);

				// Fix this to swich between VST and Native FX
				~dirt.receiveAction = { |event|
					var e = event.copy;
					var orbit = ~dirt.orbits[e.at(\orbit)];
					var reverb = e.at(\reverb) ?? orbit.defaultParentEvent.at(\reverb) ?? 0;
					var room = e.at(\room) ?? orbit.defaultParentEvent.at(\room) ?? 0;

					var wet = reverb + room;

					~tidalWetReverbs.put(orbit.orbitIndex, reverb);
					~tidalWetSums.put(orbit.orbitIndex, wet);

					~valhallaSynth.set(\wetReverbs, ~tidalWetReverbs);
					~masterSynth.set(\wetSums, ~tidalWetSums);
				};
			);
		});

	"_Init Midi X2.scd".loadRelative;

	~tidalX.gui;
	// ~tidalNetAddr = NetAddr.new("127.0.0.1", 6010); /*~tidalMixer.tidalNetAddr*/ // plus besoin initialisé dans MixerUI.sc, mais aussi besoin dans MasterUI, donc variable d'environnement préférable -> non ...

	// Initialisation of All / Some parameters of SuperDirtMixer
	// Reset All act on everything except Bypass (EQ & Compressor) & all Compressor & EQ on other tracks than the one selected
	// Where is the action on EQ of the track selected in \resetAll in MixerUIX ?
	~tidalResetAllButtonView.valueAction_(1); // If something plays on Tidal, there will a short sound for hundreds of ms

	~tidalGuiElements[0][\eq][\element].valueAction_(1); // To update the number of FX Button
	~tidalTrackSelectionButton.valueAction_(1); // Random Action on first 4 tracks (0 - ALL -> 3 - Track)

	~tidalRytSNumBox.valueAction_(1); // 0 Rhythm Free -> 2 more square
	~tidalLegSNumBox.valueAction_(2); // 0 Duration Free (Sound Length) - 1 (No superposition) - 2 (Slider)
	~tidalBufSNumBox.valueAction_(4); // 0 Buffer Bass Drum -> 4 more open

	// JSON Presets control everything except Live Stage Limiter
	// Then, it can replace action on ~tidalNumBox above & ~tidalResetAllButtonView
	// ~tidalpresetListView.items;
	~tidalWantedIndex = ~tidalPresetListView.items.detectIndex({ |item| item == "Test1.json"});
	~tidalPresetListView.valueAction_(~tidalWantedIndex); // To select Preset index defined above
	// ~tidalLoadPresetButtonView.valueAction_(1); // To trigger Preset selected

	// Stage Limiter - Default Values - To change if needed at StartUp
	// ~stageMasterButton.valueAction_(0);
	// ~stageMasterCompThreshold.valueAction_(0.7);
	// ~stageMasterLimiterLevel.valueAction_(1);
	// ~stageMasterHighEndDb.valueAction_(0.32);

	~tidalMasterMulNumBox.valueAction_(-6); // Réécriture du Tidal Master Volume

	// Global Presets
	~tidalAllPresetListView.items_(["Null", "1", "2", "3", "4", "5", "6", "7", "8", "9"]).maxWidth_(40).maxHeight_(152)
	.action_({ arg sbs;
		case
		{sbs.value == 1}
		{
			~tidalGuiElements[0][\preset][\value].valueAction_(1);
			~tidalGuiElements[1][\preset][\value].valueAction_(2);
			~tidalGuiElements[2][\preset][\value].valueAction_(3);
			~tidalGuiElements[3][\preset][\value].valueAction_(4);
		}
		{sbs.value == 2}
		{
			~tidalGuiElements[0][\preset][\value].valueAction_(5);
			~tidalGuiElements[1][\preset][\value].valueAction_(6);
			~tidalGuiElements[2][\preset][\value].valueAction_(7);
			~tidalGuiElements[3][\preset][\value].valueAction_(8);
		}
	});

};
)


// Live spatialisation over many channels / multichannel routing across non-adjacent channels (SuperDirt Hacks)
// Getting tidal output into node proxies
// But Loss of EQ in SuperDirtMixer
(
Ndef(\dirty).ar(~dirt.numChannels); // make a place for a signal
~dirt.orbits.do { |each| each.outBus = Ndef(\dirty).bus };
// To see what you are doing, you can open a scope for the outputs:
s.scope(~tidalNbOfLoudspeakers).window.alwaysOnTop_(true);
Ndef(\dirty).playN([0, 1], [1, 1], [0, 1]); // Stereo Classic
)

// Outs, Amps, Ins
Ndef(\dirty).playN([0, 1], [1, 1], [0, 1]); // Stereo Classic
Ndef(\dirty).playN([0, 7, 1, 2], [1, 1, 0.7, 0.7], [0, 0, 1, 1], fadeTime: 3); // Stereo Double
Ndef(\dirty).playN([0, 6, 7], [1, 0.5, 0.7], [0, 1, 0], fadeTime: 3);


// Le jux ne marche pas bien dans les configs multicanales ?
// Le bypass du compresseur n'a pas l'air de marcher ? -> est bypassé au départ, mais dès qu'on l'enclenche, on ne peut plus le bypasser
// Res EQ -> ne marche pas ?
// When you move all SeqLine together besides S0, It makes freeze Hydra & even cuts sound from SC ???
// Si le son ne marche plus :
Server.killAll;


// Ajustement de la taille de l'EQ et du Compresseur
// -> Width de l'EQ : ligne 302 de EqualizerUIX
// -> Position X et Width du Compresseur : ligne 248 de CompressorUIX
// -> To not get the ColorPicker : ligne 1046 de MixerUIX





// RMS for Hydra

(
// Run this to start sending the RMS OSC messages to SC.
~dirt.startSendRMS(60, 0.1);

// The bridge will be listening to OSC messages on port UDP 9130.
~tidalOSCAdress = NetAddr.new("127.0.0.1", 4444);

// This is the RMS OSC handler, here we'll forward these messages to our bridge
~tidalRMSFunc = OSCFunc({ |msg|
	// msg.postln;
	// Forward OSC message
	~tidalOSCAdress.sendMsg("/rms", *msg);
}, "/rms");
)

~tidalRMSFunc.free



// Parameter for sending RMS
~dirt.startSendRMS(20, 3); // Default (rmsReplyRate: 20, rmsPeakLag: 3)
~dirt.startSendRMS(20, 1);

~dirt.startSendRMS(6, 3);

~dirt.startSendRMS(30, 0.4);
~dirt.startSendRMS(30, 0.01);

~dirt.startSendRMS(60, 3);
~dirt.startSendRMS(60, 1);
~dirt.startSendRMS(60, 0.1); // Seuil de perception visuel
~dirt.startSendRMS(60, 0.01);
~dirt.startSendRMS(60, 0.001);

~dirt.stopSendRMS;





// VST Vahall Super Massive

~valhalla.numParameters; // 18
~valhalla.set(0, 1.0.rand.postln); // 0 - Mix
~valhalla.set(1, 1); // 1 - Tempo subdivision (0.3: Msec - 0.5: Note - 0.8: Dotted - 1: Dotted)
~valhalla.set(2, 1.0.rand.postln); // 2 - Delay
~valhalla.set(3, 1.0.rand.postln); // 3 Delay ???
~valhalla.set(4, 1.0.rand.postln); // 4 - Warp
~valhalla.set(5, 1); // 5 - Clear (1 ou 0)
~valhalla.set(6, 1.0.rand.postln); // 6 - Feedback
~valhalla.set(7, 1.0.rand.postln); // 7 - Density
~valhalla.set(8, 1.0.rand.postln); // 8 - Width
~valhalla.set(9, 1.0.rand.postln); // 9 - Low
~valhalla.set(10, 1.0.rand.postln); // 10 - High
~valhalla.set(11, 1.0.rand.postln); // 11 - Mod Rate
~valhalla.set(12, 1.0.rand.postln); // 12 - Mod Depth
~valhalla.set(13, 1.0.rand.postln); // 13 - Mode (to define...)

~valhalla.info.printPresets;
~valhalla.set(0, 1.0.rand.postln);
~valhalla.getn(action: {arg p; ~valhallaPreset = p.postln;});
~valhalla.setn(0, ~valhallaPreset);
~valhalla.setn(0, Array.fill(18, {1.0.rand}) );

/*
~valhalla.savePreset("V_Vger");
~valhalla.savePreset("V_TheVoid");
~valhalla.savePreset("V_WeAreStardust");
~valhalla.savePreset("V_HorseHead");
~valhalla.savePreset("V_16NoteSentry");
~valhalla.savePreset("V_16NoteSentry2");
~valhalla.savePreset("V_Planetarium");
~valhalla.savePreset("V_DarkHorse");
~valhalla.savePreset("V_Dotted8Orbits");
~valhalla.savePreset("V_CirrusMajorSynthEchoes");
~valhalla.savePreset("V_Reverse8Note");
~valhalla.savePreset("V_LibraRising");
~valhalla.savePreset("V_LibraNebula");
~valhalla.savePreset("V_LibraHall");
~valhalla.savePreset("V_LibraBloom");
~valhalla.savePreset("V_DarkLibraPlate");
~valhalla.savePreset("V_LostSaucer");
~valhalla.savePreset("V_SlowAttackEnsemble");
~valhalla.savePreset("V_ScorpioRising");
*/
~valhalla.loadPreset("V_Vger");
~valhalla.loadPreset("V_16NoteSentry");
~valhalla.loadPreset("V_Vger");
~valhalla.loadPreset("V_16NoteSentry");
~valhalla.loadPreset("V_Vger");
~valhalla.loadPreset("V_16NoteSentry2");

~valhalla.loadPreset("V_LibraNebula");
~valhalla.loadPreset("V_LibraRising");
~valhalla.loadPreset("V_LibraHall");
~valhalla.loadPreset("V_DarkLibraPlate");
~valhalla.loadPreset("V_LibraBloom");
~valhalla.loadPreset("V_LostSaucer");
~valhalla.loadPreset("V_SlowAttackEnsemble");
~valhalla.loadPreset("V_ScorpioRising");

~valhalla.loadPreset("V_DarkHorse");
~valhalla.loadPreset("V_HorseHead");

~valhalla.loadPreset("V_Dotted8Orbits");
~valhalla.loadPreset("V_Reverse8Note");
~valhalla.loadPreset("V_CirrusMajorSynthEchoes");
~valhalla.loadPreset("V_Planetarium");
~valhalla.loadPreset("V_TheVoid");
~valhalla.loadPreset("V_WeAreStardust");





// To post info about event arguments
(
~dirt.addModule('post',
	{ |dirtEvent|
		"\n------------\n".post;
		dirtEvent.event.pairsDo { |key, val, i|
			"%: % ".format(key, val).post;
			if(i % 4 == 0) { "\n".post };
		}
});
)

// remove it again:
~dirt.removeModule(\post);





(
// WARNING: keyword arg 'uiCallback' not found in call to Meta_EQui:new
s.options.memSize = /*8192 * 256*/ (2.pow(20));
s.options.numBuffers = 1024 * 16;
s.options.maxNodes = /*1024 * 256*/ (8192*500);
s.waitForBoot {
	~dirt = SuperDirt(2, s);
	~dirt.start(57120, 0 ! 14);
	// More SuperDirt ...

	// Initialize the SuperDirtMixer
	~mixer = SuperDirtMixer(~dirt);

	// You can adjust parameters before you use the ui
	~mixer.setMasterBus(0);

	// When you added your settings in you startup file, then you should be able to use the ui at any time in any differtent SuperCollider file.
	~mixer.gui;
}
)





// SuperDirt StartUp Help Example

(
// configure the sound server: here you could add hardware specific options
// see http://doc.sccode.org/Classes/ServerOptions.html
~numOutputBusChannels = 4;
s.options.numBuffers = 1024 * 16; // increase this if you need to load more samples
s.options.memSize = 8192 * 16; // increase this if you get "alloc failed" messages
s.options.maxNodes = 1024 * 32; // increase this if you are getting drop outs and the message "too many nodes"
s.options.numOutputBusChannels = ~numOutputBusChannels; // set this to your hardware output channel size, if necessary
s.options.numInputBusChannels = 2; // set this to your hardware input channel size, if necessary
// boot the server and start SuperDirt
s.waitForBoot {
    ~dirt = SuperDirt(~numOutputBusChannels, s); // two output channels, increase if you want to pan across more channels
    ~dirt.loadSoundFiles;   // load samples (path containing a wildcard can be passed in)
    s.sync; // wait for samples to be read
    ~dirt.start(57120, [0, 0]);   // start listening on port 57120, create two orbits, each sending audio to channel 0. You can direct sounds to the orbits from tidal e.g. by: `# orbit "0 1 1"
}
// now you should be able to send from tidal via port 57120
)

// Below necessary to play Tidal within SuperCollider
SuperDirt.default = ~dirt;

// Test with 4 channels - see ServerMeterView to automatically show the Server Meter

// With a mono sound, play only on one channel
(type:\dirt, orbit:0, s: \cr).play; // Play on the 3rd channel
(type:\dirt, orbit:0, s: \cr, \pan: 0).play; // Play on the 1st channel
(type:\dirt, orbit:0, s: \cr, \pan: 1/4).play; // Play on the 2nd channel
(type:\dirt, orbit:0, s: \cr, \pan: 2/4).play; // Play on the 3rd channel
(type:\dirt, orbit:0, s: \cr, \pan: 3/4).play; // Play on the 4th channel
(type:\dirt, orbit:0, s: \cr, \pan: 4/4).play; // Play on the 1st channel again

// With a stereo sound, play always on two channels even if pan is set at 0
(type:\dirt, orbit:0, s: \clubkick).play; // Play on the 3rd and 4th channel
(type:\dirt, orbit:0, s: \clubkick, \pan: 0).play; // Play on the 1st and 2nd channel
(type:\dirt, orbit:0, s: \clubkick, \pan: 1/4).play; // Play on the 2nd and 3rd channel
(type:\dirt, orbit:0, s: \clubkick, \pan: 2/4).play; // Play on the 3rd and 4th channel
(type:\dirt, orbit:0, s: \clubkick, \pan: 3/4).play; // Play on the 4th and 1st channel
(type:\dirt, orbit:0, s: \clubkick, \pan: 4/4).play; // Play on the 1st and 2nd channel again

(type:\dirt, orbit:0, s: \clubkick, \pan: 0, \channel: 0).play; // Play on the 1st and 2nd channel
(type:\dirt, orbit:0, s: \clubkick, \pan: 0, \channel: 1).play; // Play on the 2nd and 3rd channel - Channel allows to off

DirtPan.defaultPanningFunction
DirtPan.defaultMixingFunction

// with a stereo sound file, how to get only one channel played in Tidal with a multichannel system, e.g. 4 outputs ?
// I would like to have the flexibility of playing a stereo sound file on 2 channels, but also on 1 channel on a multichannel system.
(type:\dirt, orbit:0, s: \clubkick, \pan: -1, \channel: 0).play;
(type:\dirt, orbit:0, s: \clubkick, \pan: 0, \channel: 0, \pansplay: 1).play
(type:\dirt, orbit:0, s: \clubkick, \pan: 0, \channel: 1, \panbus: -1).play
(type:\dirt, orbit:0, s: \clubkick, \pan: 0, \channel: 0, \orbit: 3).play
(type:\dirt, orbit:0, s: \clubkick, \pan: 0, \channel: 0, \span: 0).play
(type:\dirt, orbit:0, s: \clubkick, \pan: 0, \channel: 0, \panwidth: 0).play
(type:\dirt, orbit:0, s: \clubkick, \pan: 0, \channel: 0, \splay: 0).play
(type:\dirt, orbit:0, s: \clubkick, \pan: 0, \channel: 0, \orientation: 0).play

