// TidalCycles Live Coding with GUI & Controllers - with SuperDirt & SuperDirtMixer & Live 4 Bubbles
// ~mainServer1Window sould be set at 0 in the file "_0B_Init_Config" in Live 4 Life Project if both are used simultaneously
// To include TidalLooper

// Synchronization via Ableton Link to check ?
// https://tidalcycles.org/blog/link_as_scheduler
// https://tidalcycles.org/docs/configuration/MIDIOSC/midi#synchronising-midi-clock-using-the-link-protocol
// https://club.tidalcycles.org/t/olatency-positive-or-negative-value/4812

// NetAddr // soon.localIP, network, localEndPoint, port in next release

// FXs code with issues with new version GHC 9.12.2 vs 9.6.7 & Tidal 1.10.1 vs 1.9.5 ?


Server.killAll;


( // TIDAL Xon - Live 4 Bubbles

~tidalHydra = 1; // Send OSC data to (Fizzy) Hydra (RMS / Event data per channel & other controls
~tidalOutputChannels = 2; // Nb of Channels in Tidal XXXXXXXXX - Error with Mi-UGens with 8 channels
~tidalNbOfLoudspeakers = 2; // Nb of Loudspeakers XXXXXXXXX - Error with Mi-UGens with 8 channels

// GUI Size and Position parameters
// Size for 4 Tracks
~tidalNbOfOrbits = 4; // Nb of Tracks in Tidal XXXXXXXXX
~tidalWindowSize = 534;
~tidalWindowMasterSize = 100;
~tidalWindowMixerSize = 300;

// Size for 8 Tracks
/*~tidalNbOfOrbits = 8; // Nb of Tracks in Tidal XXXXXXXXX
~tidalWindowSize = 844;
~tidalWindowMasterSize = 100;
~tidalWindowMixerSize = 592;*/

// For optimal position with Live 4 Life
~tidalWindowPosX = 1600;
~tidalWindowPosY = 55;
/*
~tidalWindowPosX = 0;
~tidalWindowPosY = 250;
*/

~tidalMasterMul = 1.ampdb; // Initialisation du conrôle du Master en dB

// Server Options
s.options.numInputBusChannels = 0;
s.options.numOutputBusChannels = ~tidalNbOfLoudspeakers;
s.options.memSize = 2.pow(20); /*8192 * 256*/
s.options.numBuffers = 1024 * 16;
s.options.maxNodes = 8192 * 500; /*1024 * 256*/
// s.latency -> Too many lates ??? with some effects and functions

// Structured Folder of sound files - Base de données sonores XXXXXXXXX
// ~tidalsoundsFolder = "/Users/xon/Documents/Projets/SC_LFL/Live4LifeProjects/TechnoMachineS/_Sounds2024A/";
~tidalsoundsFolder = "/Users/xon/Documents/Projets/SC_LFL/Live4LifeProjects/TechnoMachineS/_Sounds2025A/";

// Names of sound folders
~arrayOfFolderNames4Tidal = ("ls -m" + ~tidalsoundsFolder).unixCmdGetStdOut;
~arrayOfFolderNames4Tidal = ~arrayOfFolderNames4Tidal.split($, ).collect { |i| i.stripWhiteSpace };
~arrayOfFolderNames4Tidal = ~arrayOfFolderNames4Tidal.collect { |i| i.replace(" ","_")};

// Distinction des dossiers sonores
~arrayOfFolderNames4TidalSizeS0 = 18; ~arrayOfFolderNames4TidalSizeS1 = 29; ~arrayOfFolderNames4TidalSizeS2 = 67+2; ~arrayOfFolderNames4TidalSizeS3 = 76+2; ~arrayOfFolderNames4TidalSizeS4 = 99+2;

~arrayOfFolderNames4TidalSize = (~arrayOfFolderNames4Tidal.size-1) ! ~tidalNbOfOrbits;
~tidalFolderSizeSwitch = 0 ! ~tidalNbOfOrbits;

~patternSpeedSwitch = 0 ! ~tidalNbOfOrbits;
~patternSpeeds = (0..64) ! ~tidalNbOfOrbits;
~patternSpeedsSize = (~patternSpeeds[0].size-1) ! ~tidalNbOfOrbits;

// TIDAL LINE EVALUATION
~tidalEvalPos = 0 ! ~tidalNbOfOrbits; // not used ???
~tidalSeqLineBegin = 1;
~tidalSeqLineEndR = (476/2).asInteger; // Set the number of lines that can be triggered in Pulsar Tidal code XXXXXXXXXXXX
~tidalSeqLineEnd = ~tidalSeqLineEndR; // ~tidalSeqLineEnd because the selection can be changed
~tidalEvalLine = (1..~tidalSeqLineEnd) ! ~tidalNbOfOrbits;
~tidalPreviousEvalLine = 1 ! ~tidalNbOfOrbits;
// ~tidalEvalLine[0] = (1..26); ~tidalEvalLine[1] = (1..26); ~tidalEvalLine[2] = (1..26); ~tidalEvalLine[3] = (1..26);
// ~tidalSilenceLine = 96; // Line in Pulsar to silence the Track
~tidalXfadeOutLine = ~tidalSeqLineEndR*2.asInteger + 6; // Line in Pulsar to fade out the Track XXXXXXXXX
~tidalEvalTabPaneSwitch = 1; // 1 Pulsar display in panes / splits - 0 Pulsar dispaly in tab

~tidalMainControlPane = 0; // For main controls
~tidalMainControlTab = 4;
~tidalMainControlInterpolationA = 2;
~tidalMainControlBPMA = 22;
~tidalMainControlBPMADur = 47;

// TIDAL Nb of FX & functions
~tidalnumEffects = 122; // Set the value of fxR in Tidal Cycles XXXXXXXXX
~tidalnumFunctions = 264; // Set the value of fuR in Tidal Cycles XXXXXXXXX
~tidalnumSwitchX = 24; // Set the last number of the list sfxx / sfux (superposition / jusxtaposition types with offset or alone) -> just to indicate the highest value of NumberBox XXXXXXXXX
~tidalnumWhenS = 39; // Set the last number of the list below fx / fu XXXXXXXXX
~tidalnumDurAlgs = 44; // Set the last number of dur (Nb of duration algorhithms)
~tidalnumSpaAlgs = 29; // // Set the last number of Spa (Channel algorhithm)

~tidalTrackSelection = (0..7);
~tidalCurrentOrbit = 0;
~tidalFx10ARandSwitch = 0; ~tidalFx20ARandSwitch = 0; ~tidalFx0ARandSwitch = 0;
~tidalFu10ARandSwitch = 0; ~tidalFu20ARandSwitch = 0; ~tidalFu0ARandSwitch = 0; ~tidalBuf0ARandSwitch = 0;

~tidalPresetXPath = PathName(thisProcess.nowExecutingPath).pathOnly;
~tidalPresetTrackXPath = ~tidalPresetXPath++"Xpresets/";
~tidalPresetTrackXFileName = "PresetsTrax.txt";
~tidalPresetTrackXPath2 = ~tidalPresetXPath ++ ~tidalPresetTrackXFileName;
~tidalPresetSeqLineMinMax = 0; // 1 to include Min & Max of Presets & SeqLines in X Presets, otherwise 0

~tidalPresetNb = 0 ! ~tidalNbOfOrbits;
~tidalPreviousPresetNb = 0 ! ~tidalNbOfOrbits; // To limit the action on GUI only when necessary
~tidalPresetLine = (1..PathName(~tidalPresetTrackXPath).files.size) ! ~tidalNbOfOrbits;
~orbitUpdateNb = 0;

~tidalNoteVariation = 48; // Range of note parameter at startup, e.g. 12 / 24 / 48 / 60
~tidalSynths = ["super808", "skick", "gabkick", "superhat", "superclap", "superperc", "donk", "wub", "wob", "sawpluck", "vibsawpluck", "plucklead", "noisebuild", "fmp", "fmtribe", "ab2", "flutter", "futuresub", "futuresaw", "ultrasaw", "windsaw", "saw", "acid", "acid2", "acid309", "cs80leadClick", "cs80lead", "sinosc", "ike2", "ike3", "supersin", "myDrone", "myfm", "rhodes", "rhodes2", "superrhodes", "superlaser", "superfadehammond", "myblip", "super303", "supermoog", "bmoog", "superbass", "mybass", "bass8", "pbass", "bazz", "bfm", "superp5str", "superost", "superwho", "superdown", "superup", "superfbk", "supersawfbk", "superfmfbk", "dummy", "click", "stiff", "atari", "bow", "plastickystrings", "plucking", "pitchednoise", "werkit", "trianglewavebells", "voicform", "synchronicity", "spook", "spook2", "sosbell", "apad", "babbl", "bbdd", "beating", "blips1", "blowbotl", "cymbal808_ryan", "flute", "forestbird", "goeysynth", "handclapsmash", "handclapsmash2", "harpsichord", "hat", "snare", "kick", "impulsetrain", "jilet", "justwannahavefun", "kick_oto309", "laserbeam", "mrostinato", "noisy", "oneclap_thor", "organdonor", "ping_mh", "sillyVoice", "situationsynth", "yazoodelayeffect", "snare_oto309", "fmbass", "lpg", "hammondserrano", "bytebeat", "tb303", "mogo", "velobass", "reslab", "supersine", "guitar"]; // 111
// 26 Atsuhi Tadokoro Synths :
// -> ["skick", "gabkick", "superperc", "ike2", "ike3", "supersin", "myDrone", "myfm", "superrhodes", "superlaser", "superfadehammond", "myblip", "super303", "supermoog", "bmoog", "superbass", "mybass", "bfm", "superp5str", "superost", "superwho", "superdown", "superup", "superfbk", "supersawfbk", "superfmfbk"]
// 15 Eris FairBanks Synths :
// -> ["donk", "wub", "sawpluck", "vibsawpluck", "plucklead", "wob", "noisebuild", "fmp", "ab2", "flutter", "futuresaw", "futuresub", "ultrasaw", "acid", "sinosc"].size
// 10 Gilfuser :
// -> ["dummy", "click", "stiff", "atari", "bow", "windsaw", "saw", "bazz", "rhodes2", "acid2"]
// 7 Diego Dorado Synths : (cs80leadClick to withdraw)
// -> ["acid309", "bass8", "cs80leadClick", "cs80lead", "fmtribe", "pbass", "rhodes"]
// 3 Other Synths (default?) :
// -> ["super808", "superhat", "superclap"]
// 40 AndreaBertagnolli  P(Synths Extra) :
// -> ["plastickystrings", "plucking", "pitchednoise", "werkit", "trianglewavebells", "voicform", "synchronicity", "spook", "spook2", "sosbell", "apad", "babbl", "bbdd", "beating", "blips1", "blowbotl", "cymbal808_ryan", "flute", "forestbird", "goeysynth", "handclapsmash", "handclapsmash2", "harpsichord", "hat", "snare", "kick", "impulsetrain", "jilet", "justwannahavefun", "kick_oto309", "laserbeam", "mrostinato", "noisy", "oneclap_thor", "organdonor", "ping_mh", "sillyVoice", "situationsynth", "yazoodelayeffect", "snare_oto309"]
// 10 Loopier-RogerPibernat
// -> ["fmbass", "lpg", "hammondserrano", "bytebeat", "tb303", "mogo", "velobass", "reslab", "supersine", "guitar"].size

// Switches to setup specific Synths XXXXXXXXX
~tidalExtraSynths = 1; // Collection of SC synths XXXXXXXXX
~tidalExtraMiFX = 0; // Collection of Mi-UGens FX synths XXXXXXXXX

// Switches to setup specific VSTs
~tidalExtraValhallaReverb = 1; // VST Vahalla Reverb XXXXXXXXX
~tidalExtraVST = 0; // VST Instrument XXXXXXXXX

~vstName = \dexed; ~vstNameString = "Dexed.vst3"; // XXXXXXXXX
// ~vstName = \vital; ~vstNameString = "Vital.vst3";
// ~vstName = \surge; ~vstNameString = "Surge XT.vst3";

~tidalLooper = 0;

// Problème de click d'envelope with VST instruments ?
/*
~dirt.orbits[1].set(\fadeTime, 0);
~dirt.orbits[1].set(\fadeTime, 0.00001);
~dirt.orbits[1].set(\fadeTime, 0.001); // By default
~dirt.orbits[1].set(\fadeTime, 0.01);
~dirt.orbits[1].set(\fadeTime, 0.1);
~dirt.orbits[1].set(\fadeTime, 0.2);
~dirt.orbits[0].amp
~dirt.orbits[0].group
~dirt.orbits[0].fadeTime
~dirt.orbits[0].minSustain
~dirt.orbits[0].outBus
*/

~superDirtOSC = NetAddr.new("127.0.0.1", 57120);

s.waitForBoot {
	~dirt = SuperDirt(~tidalOutputChannels, s);
	~dirt.loadSoundFiles(~tidalsoundsFolder++"*"); // ~dirt.loadSoundFiles;

	if (~tidalExtraSynths == 1, {
		load(~tidalPresetXPath++"Synths/Mi-UGens.scd");
		load(~tidalPresetXPath++"Synths/AtsuhiTadokoro-Synths.scd"); // 26
		load(~tidalPresetXPath++"Synths/ErisFairbanks-Synths.scd"); // 15
		load(~tidalPresetXPath++"Synths/Gilfuser-Synths.scd"); // 10
		load(~tidalPresetXPath++"Synths/DiegoDorado-Synths.scd"); // 7
		load(~tidalPresetXPath++"Synths/AndreaBertagnolli-Synths.scd"); // 40
		load(~tidalPresetXPath++"Synths/LoopierRogerPibernat-Synths.scd"); // 10
	});

	/*if (~tidalExtraVST == 1, {
		load("/Users/xon/Desktop/Live_Coding/Tidal/ExtraSynths/Tidal-VST.scd");
		s.sync; // Necessary to load VST synth
		load("/Users/xon/Desktop/Live_Coding/Tidal/ExtraSynths/Tidal-VST2.scd");
	});*/

	s.sync; // Wait for samples to be read

	~dirt.start(57120, 0 ! ~tidalNbOfOrbits); // Start listening on port 57120, create X orbits

	// Taille des Fichiers de Tidal // ~dirt.soundLibrary.buffers.size
	// ~dirt.soundLibrary.bufferEvents.keys.asArray // Get info with bufferEvents
	~tidalFolderSizes = (~dirt.soundLibrary.buffers.size+1).collect { |i| ~dirt.soundLibrary.buffers[~arrayOfFolderNames4Tidal[i].asSymbol].size-1 };

	// Initialize the SuperDirtMixer
	~tidalX = SuperDirtMixerX(~dirt /*, 6010*/);
	// You can adjust parameters before you use the ui
	~tidalX.setOrbitLabels(/*["1","2","3","4","5","6","7","8"]*/ ["1 Fuk", "2 Pee", "3 Pou", "4 Nah", "5 Lead", "6 Bass", "7 Key", "8 Pad"] );
	~tidalX.setMasterBus(0);

	s.sync; // Necessary, otherwise global Effects are added twice

	if (~tidalLooper == 1, {
		// Initialize the TidalLooper
		~looper = TidalLooper(~dirt);

		// You can adjust these parameter even in runtime
		~looper.rLevel = 2.5;
		~looper.pLevel = 0.8;
		~looper.linput = 0; // Set this to your main input port.
		~looper.lname = "mybuffer";
		~looper.debugMode = true;
		// ~looper.numBuffers
		// ~looper.looperSynth = \buffRecord1
		// ~looper.gui
	});

	if (~tidalExtraVST == 1, {

		load(~tidalPresetXPath++"Synths/Tidal-VST.scd");
		s.sync; // Necessary to load VST synth
		load(~tidalPresetXPath++"Synths/Tidal-VST2.scd");

		{~instruments[~vstName].editor}.defer(1); // To open the GUI
	});

	if (~tidalExtraMiFX == 1, {
		// define global effects for mutable instruments effects
		~dirt.orbits.do { |x|
			var clouds = GlobalDirtEffect(\global_mi_clouds, [\cloudspitch, \cloudspos, \cloudssize, \cloudsdens, \cloudstex, \cloudswet, \cloudsgain, \cloudsspread, \cloudsrvb, \cloudsfb, \cloudsfreeze, \cloudsmode, \cloudslofi]);
			var verb = GlobalDirtEffect(\global_mi_verb, [\verbwet, \verbtime, \verbdamp, \verbhp, \verbfreeze, \verbdiff, \verbgain]);
			x.globalEffects = x.globalEffects
			.addFirst(clouds)
			.addFirst(verb);
			x.initNodeTree;
		};
	});
	// ~dirt.orbits[0].globalEffects.cs
	// ~dirt.orbits[0].globalEffects[0].synth;

	if (~tidalOutputChannels != 32 and: {~tidalExtraValhallaReverb == 1}, {

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

   ~tidalEvalAddr = NetAddr.new("127.0.0.1", 3333); // necessaire pour ~tidalXFadeANumBox & ~tidalBPMANumBox dans Utility ?
   // ~tidalNetAddr = NetAddr.new("127.0.0.1", 6010); /*~tidalMixer.tidalNetAddr*/ // plus besoin initialisé dans MixerUI.sc, mais aussi besoin dans MasterUI, donc variable d'environnement préférable -> non ...

	~tidalX.gui;

	"_Init Midi X2.scd".loadRelative; // Xone K2
	"_Init Midi T2.scd".loadRelative; // Midi Fighter Twister

	// Initialisation of All / Some parameters of SuperDirtMixer
	// Reset All act on everything except Bypass (EQ & Compressor) & all Compressor & EQ on other tracks than the one selected
	// Where is the action on EQ of the track selected in \resetAll in MixerUIX ?
	~tidalResetAllButtonView.valueAction_(1); // If something plays on Tidal, there will a short sound for hundreds of ms

	~tidalGuiElements[0][\eq][\element].valueAction_(1); // To update the number of FX Button
	~tidalTrackSelectionButton.valueAction_(1); // Random Action on first 4 tracks (0 - ALL -> 3 - Track)

	~tidalRytSNumBox.valueAction_(0); // 0 Rhythm Free -> 2 more square
	~tidalLegSNumBox.valueAction_(1); // 0 Duration Free (Sound Length) - 1 (Cut 1) - 2 (Legato 1) - X (Slider)
	~tidalBufSNumBox.valueAction_(0); // 0 Buffer Bass Drum -> 4 more open

	/*~tidalRytSwitchButton.valueAction_(1); // 0 \rytS is updated with Track preset - otherwise 0
	~tidalLegSwitchButton.valueAction_(0); // 0 \legS is updated with Track preset - otherwise 0
	~tidalFolSwitchButton.valueAction_(0);*/ // 0 \BufS is updated with Track preset - otherwise 0

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

	// ~tidalMasterMulNumBox.valueAction_(-12); // Réécriture du Tidal Master Volume
	~tidalMasterMulNumBox.valueAction_(-6);

	// RMS Volume per track & events per track & parameter for Hydra

	if (~tidalHydra == 1, {
		(
			// Run this to start sending the RMS OSC messages to SC.
			// ~dirt.startSendRMS(60, 0.1);
			// ~dirt.startSendRMS(30, 0.5);

			// The bridge will be listening to OSC messages on port UDP 4444.
			~tidalOSCAdress = NetAddr.new("127.0.0.1", 4444);

			// This is the RMS OSC handler, here we'll forward these messages to our bridge
			~tidalRMSFunc = OSCFunc({ |msg|
				// msg.postln;
				// Forward OSC message
				~tidalOSCAdress.sendMsg("/rms", *msg);
			}, "/rms");

			~tidalDirtMsgFunc = OSCFunc({ |msg, time, tidalAddr|
				var latency = time - Main.elapsedTime;
				// var addr = NetAddr.new("127.0.0.1", 4444 /*3333*/);
				msg = msg ++ ["time", time, "latency", latency];
				// msg.postln;
			/*addr*/ ~tidalOSCAdress.sendBundle(latency, msg)
		}, '/dirt/play' /*'/play2'*/).fix;

			// Fonction pour changer tidalDirtMsgActions
			~setTidalMode = { |mode = 9|
				// Validation de la valeur (0-23)
				mode = mode.clip(0, 23);

				// Envoyer le message OSC vers Hydra
				~tidalOSCAdress.sendMsg("/tidalDirtMsgActions", mode);

				// Affichage local pour confirmation
				// ("OSC: tidalDirtMsgActions set to " ++ mode).postln;
			};

			~setSourceMode = { |mode = 0|
				// mode = mode.clip(0, 23);

				// Envoyer le message OSC vers Hydra
				~tidalOSCAdress.sendMsg("/tidalSourceMsgActions", mode);

				// Affichage local pour confirmation
				// ("OSC: tidalSourceMsgActions set to " ++ mode).postln;
			};

			// [/dirt/play, _id_, 1, begin, 0.9375, cps, 0.5625, cut, 1, cycle, 244.01042175293, delta, 0.0092590004205704, djf, 0.5, end, 0.95833331346512, n, 89.0, orbit, 0, pan, 1.0, s, DS_ViRide, speed, 1.0, time, 71301.815930501, latency, 0.18750583507062]

		);
	});

};
)





~setTidalMode.(0);
~setTidalMode.(1);
~setTidalMode.(7);
~setTidalMode.(8);
~setTidalMode.(9);

~setSourceMode.(0);
~setSourceMode.(1);
~setSourceMode.(2);

// Pour changer automatiquement pendant une performance
~tidalModeSequence = Routine({
	loop {
		~setTidalMode.(9);    // Tracks
		4.wait;               // Attendre 4 secondes
		~setTidalMode.(8);    // Random
		2.wait;
		~setTidalMode.(7);    // All
		3.wait;
	};
});

// Démarrer la séquence
~tidalModeSequence.play;

// Arrêter la séquence
~tidalModeSequence.stop;

// Pour intégrer dans vos patterns existants
Pdef(\tidalModeChanger,
	Pbind(
		\type, \rest,
		\dur, Prand([2, 3, 4, 6], inf),
		\mode, Prand([0, 7, 8, 9], inf),
		\func, Pfunc({ |event|
			~setTidalMode.(event[\mode]);
		})
	)
);

// Jouer le pattern
Pdef(\tidalModeChanger).play;



// SC Graph on 2 tracks
NODE TREE Group 0
---1 group
------1041 temp__0
------2 group
---------4 group
---------1045 dirt_master_mix2
---------1049 dirt_global_eq2
---------1054 dirt_global_compressor2
---------1030 dirt_delay2
---------1029 dirt_reverb2
---------1028 dirt_leslie2
---------1027 dirt_rms2
---------1026 dirt_monitor2
---------3 group
---------1044 dirt_master_mix2
---------1048 dirt_global_eq2
---------1053 dirt_global_compressor2
--------- //1048 global_mi_verb2 (with ~~tidalExtraMiFX = 1 / No effect on CPU to determine ?)
--------- //1047 global_mi_clouds2 (with ~~tidalExtraMiFX = 1 / No effect on CPU to determine ?)
---------1025 dirt_delay2
---------1024 dirt_reverb2
---------1023 dirt_leslie2
---------1022 dirt_rms2
---------1021 dirt_monitor2
------1000 VST // (with ~tidalExtraVST = 1 / No effect on CPU to determine ?)
------1042 valhalla2 // (with ~tidalExtraValhallaReverb = 1 / adds 5/6% CPU -> Global 8% CPU)
------1043 masterSynth  // (with ~tidalExtraValhallaReverb = 1 The master synth routes the content of the fxBus to an out bus, i.e. the hardware output)
---1059 volumeAmpControl8
---1052 system_freqScope1_shm
---1057 stageMaster
---1058 localhostOutputLevels



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
Ndef(\dirty).playN([0, 7, 1, 2], [1, 1, 0.9, 0.9], [0, 0, 1, 1], fadeTime: 3); // Stereo Double G/D
Ndef(\dirty).playN([0, 1, 7, 2], [1, 1, 0.9, 0.9], [0, 0, 1, 1], fadeTime: 3); // Stereo Double F/R
Ndef(\dirty).playN([0, 2, 7, 1], [1, 1, 0.9, 0.9], [0, 0, 1, 1], fadeTime: 3); // Stereo Double

Ndef(\dirty).playN([0,1,2,3,4,5,6,7], [1,1,1,1,1,1,1,1], [0,1,0,1,0,1,0,1], fadeTime: 3);

Ndef(\dirty).playN([0, 6, 7], [1, 0.5, 0.7], [0, 1, 0], fadeTime: 3);





// Prepare the recordings for each of both servers on your Desktop - Change the recording file name and path accordingly
(
~recordingFileName = "Record"; // Change accordingly your recording file name
~recordingPath = "/Users/xon/Desktop"; // Change accordingly your recording path
s.recChannels_(8);
// If you want to record in the SuperCollider specific recording folder, put instead of the string this code in your recording path: thisProcess.platform.recordingsDir

~recordingDate = Date.getDate; ~recordingDate = ~recordingDate.stamp;
~recordingPathFile1 = ~recordingPath ++ "/" ++ ~recordingFileName ++ "_" ++ ~recordingDate ++ "-S1" ++ ".wav";
if (File.exists(~recordingPathFile1), { ~recordingPathFile1 = ~recordingPath ++ "/" ++ ~recordingFileName ++ "_" ++ ~recordingDate ++ "-S1" ++ ".wav"; });
s.prepareForRecord(~recordingPathFile1); ~recordingPathFile1.postln;
)

// Start the recording
s.record;

// Stop the recording
s.stopRecording;






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
// ~dirt.startSendRMS(60, 0.1);
~dirt.startSendRMS(30, 0.5);

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

~dirt.stopSendRMS

// Parameter for sending RMS
~dirt.startSendRMS(20, 3); // Default (rmsReplyRate: 20, rmsPeakLag: 3)
~dirt.startSendRMS(20, 1);

~dirt.startSendRMS(6, 3);

~dirt.startSendRMS(12, 0.4);
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







// Test Code Evaluation

// osc-loader de Hydra.js in lib

// Before without pane attribution via OSC -> selects the tab of the pane selected
                /*if (resultDict['tab'] !== undefined) {
                  atom.workspace.getPanes()[0].setActiveItem(atom.workspace.getTextEditors()[resultDict['tab']])
                }*/

// After With pane attribution via OSC
// The attribution number of pane goes from left to right (if any), and then from top to down.
if (resultDict['tab'] !== undefined) {
	if (resultDict['pane'] !== undefined) {
		const targetPane = atom.workspace.getPanes()[resultDict['pane']];
		const targetItem = targetPane.getItems()[resultDict['tab']];
		targetPane.activate();
		targetPane.setActiveItem(targetItem);
                    /*atom.workspace.getPanes()[resultDict['pane']].activate();
                    atom.workspace.getPanes()[resultDict['pane']].setActiveItem(atom.workspace.getPanes()[resultDict['pane']].getItems()[resultDict['tab']])*/
	} else {
		// atom.workspace.getPanes()[0].activate(); // not necessary ? Yes because of getTextEditors
		atom.workspace.getPanes()[0].setActiveItem(atom.workspace.getTextEditors()[resultDict['tab']])
	}
}

// Affichage dans la console JS ou dans la fenêtre post de Tidal Cycles

/*console.log("Nombre de panes :", atom.workspace.getPanes().length);
                console.log("Nombre d'éditeurs :", atom.workspace.getTextEditors().length);

                // Inspecter chaque pane
                atom.workspace.getPanes().forEach((pane, index) => {
                console.log(`Pane ${index} :`, pane);
                });

                // Inspecter chaque éditeur
                atom.workspace.getTextEditors().forEach((editor, index) => {
                console.log(`Editeur ${index} :`, editor.getPath());
                });*/

o = NetAddr.new("127.0.0.1", 3333);
o.sendMsg("/pulsar/eval", \type, 'line', \tab, 0, \row, 1, \column, 1);
o.sendMsg("/pulsar/eval", \type, 'line', \tab, 0, \row, 1, \column, 1);
o.sendMsg("/pulsar/eval", \type, 'line', \tab, 1, \row, 100, \column, 10);
o.sendMsg("/pulsar/eval", \type, 'line', \tab, 2, \row, 1, \column, 1);
o.sendMsg("/pulsar/eval", \type, 'line', \pane, 0, \tab, 0, \row, 1, \column, 1);
o.sendMsg("/pulsar/eval", \type, 'line', \pane, 0, \tab, 1, \row, 1, \column, 1);
o.sendMsg("/pulsar/eval", \type, 'line', \pane, 1, \tab, 0, \row, 1, \column, 1);
o.sendMsg("/pulsar/eval", \type, 'line', \pane, 1, \tab, 1, \row, 1, \column, 1);
o.sendMsg("/pulsar/eval", \type, 'line', \pane, 2, \tab, 0, \row, 1, \column, 1); // si le pane n'est pas available, sélectionne le projet, le post window ou le browser selon l'ordre, et déclence le dernier pane ?
o.sendMsg("/pulsar/eval", \type, 'line', \pane, 2, \tab, 1, \row, 1, \column, 1);
o.sendMsg("/pulsar/eval", \type, 'line', \pane, 2, \tab, 2, \row, 1, \column, 1);
o.sendMsg("/pulsar/eval", \type, 'line', \tab, 0, \pane, 3, \row, 1, \column, 1);







// Posting-tidal-parameters.scd

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







SuperDirt
DirtOrbit
DirtEvent
DirtModule
DirtPan
DirtPanBalance2
DirtSplay2
DirtSplayAz

DirtPan.defaultPanningFunction
DirtPan.defaultMixingFunction


// https://github.com/musikinformatik/SuperDirt/issues/36
// let's sort out the panning scheme
// DirtPan

(
DirtPan.defaultMixingFunction = #{ |channels|
	channels.flop.collect { |ch, i| ch[i] ?? { DC.ar(0) } }
};
~dirt.loadSynthDefs;
)

(
DirtPan.defaultMixingFunction = #{ |channels|
	var which = MouseX.kr(0, 1);
	var first, second;
	first = channels.first.size.collect({ |i| channels @@ i @ i });
	second = channels.collect({ |x, i| x.rotate(i.neg) });
	LinXFade2.ar(first, second, which * 2 - 1)
	//first;
	//channels @@ 0
};
~dirt.loadSynthDefs;
)

// back to "normal"
(
DirtPan.defaultMixingFunction = #{ |channels|
	channels.postcs;
	channels.sum
};
~dirt.loadSynthDefs;
)

(
DirtPan.defaultPanningFunction = #{ | signals, numChannels, pan, mul |
	var channels, inNumChannels;
	var spread, width, splay, orientation;
	//pan.poll;
	width = \panwidth.ir(2);
	spread = \spread.ir(1);
	orientation = \orientation.ir(0.5);
	if(numChannels > 2) {
		signals.collect { |x, i|
			PanAz.ar(numChannels, x, (i / numChannels * 2 * spread) + pan + 1, width: width, orientation: orientation)
		}.sum;
	} {
		//DirtSplay2.ar(signals, \spread.ir(1), pan, mul)
		DirtPanFixed2.ar(signals, \spread.ir(1), pan, mul)
	}
};
~dirt.loadSynthDefs;
)


