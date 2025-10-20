{

	MIDIClient.sources.do { |i, id| /*i.device.postln;*/
		if (i.device == "Midi Fighter Twister 1", {~twisterIndex1 = id});
		if (i.device == "Midi Fighter Twister 2", {~twisterIndex2 = id});
		if (i.device == "Midi Fighter Twister 3", {~twisterIndex3 = id});
	};


	~colorRed1 = Color.red(0.9);
	~colorRed2 = Color.new255(/*255*/240, 75/*10*/, 110/*120*/);
	~colorRed3 = Color.new255(148, 0, 211);
	~colorBlue = Color.blue;
	~colorYellow1 = Color.yellow(0.6);
	~colorOther = Color.new255(139, 90, 0);
	~colorCyan1 = Color.cyan(0.6)/*new255(0, 206, 209)*/ /*new255(34, 39, 34)*//*Color.green*/;
	~colorGreen1 = Color.green(0.6); // dark sea green


	if (~twisterIndex1.notNil, {

		if (~twisterIndex2.isNil, {
			~twister2 = Twister(TwisterDevice(\twister, MIDIClient.sources[~twisterIndex1])); // Création d'une deuxième instance // this one is now connected, because Twister objects are connected when they are constructed
			~twister3 = Twister(TwisterDevice(\twister, MIDIClient.sources[~twisterIndex1]));
			~twister4 = Twister(TwisterDevice(\twister, MIDIClient.sources[~twisterIndex1]));
			~twister5 = Twister(TwisterDevice(\twister, MIDIClient.sources[~twisterIndex1]));
		},{
			if (~twisterIndex3.isNil, {
				~twister3 = Twister(TwisterDevice(\twister2, MIDIClient.sources[~twisterIndex2]));
				~twister4 = Twister(TwisterDevice(\twister2, MIDIClient.sources[~twisterIndex2]));
				~twister5 = Twister(TwisterDevice(\twister2, MIDIClient.sources[~twisterIndex2]));
				~twister2 = Twister(TwisterDevice(\twister2, MIDIClient.sources[~twisterIndex2]));
			},{
				~twister2 = Twister(TwisterDevice(\twister2, MIDIClient.sources[~twisterIndex2]));
				~twister4 = Twister(TwisterDevice(\twister3, MIDIClient.sources[~twisterIndex3]));
				~twister5 = Twister(TwisterDevice(\twister3, MIDIClient.sources[~twisterIndex3]));
				~twister3 = Twister(TwisterDevice(\twister3, MIDIClient.sources[~twisterIndex3]));
			});
		});

		/*
		~twister.connect(TwisterDevice(\twister, MIDIClient.sources[~twisterIndex1])); // Connexion avec la première instance
		~twister2.connect(TwisterDevice(\twister, MIDIClient.sources[~twisterIndex1]));
		*/
		~twister = Twister(TwisterDevice(\twister, MIDIClient.sources[~twisterIndex1])); // Init XXXXXXXXX

		1.wait;


		// Value Initialisation
		~tidalPreValues = 4.collect { NumericControlValue(spec:[0, 1]) };
		~tidalSeqValues = 4.collect { NumericControlValue(spec:[0, 1]) };
		~tidalRytValues = 4.collect { NumericControlValue(spec:[0, 1]) };
		~tidalDurAValues = 4.collect { NumericControlValue(spec:[0, ~tidalnumDurAlgs]) };

		~tidalFolValues = 4.collect { NumericControlValue(spec:[0, 1]) };
		~tidalBufValues = 4.collect { NumericControlValue(spec:[0, 1]) };
		// ~tidalRytValues = 4.collect { NumericControlValue(spec:[0, 1]) };
		~tidalDurValues = 4.collect { NumericControlValue(spec:[0, 1]) };

		~tidalRatValues = 4.collect { NumericControlValue(spec:[0, 1]) };
		~tidalRanValues = 4.collect { NumericControlValue(spec:[0, 1]) };
		~tidalSpaValues = 4.collect { NumericControlValue(spec:[0, ~tidalnumSpaAlgs]) };
		~tidalRevValues = 4.collect { NumericControlValue(spec:[0, 1]) };


		~tidalPreButtons = 4.collect { OnOffControlValue() };
		~tidalSeqButtons = 4.collect { OnOffControlValue() };
		~tidalRytButtons = 4.collect { OnOffControlValue() };
		~tidalDurAButtons = 4.collect { OnOffControlValue() };

		~tidalFolButtons = 4.collect { OnOffControlValue() };
		~tidalBufButtons = 4.collect { OnOffControlValue() };
		~tidalDurButtons = 4.collect { OnOffControlValue() };

		~tidalRatButtons = 4.collect { OnOffControlValue() };
		~tidalRanButtons = 4.collect { OnOffControlValue() };
		~tidalSpaButtons = 4.collect { OnOffControlValue() };
		~tidalRevButtons = 4.collect { OnOffControlValue() };

		/*~tidalPreButtons2 = 4.collect { OnOffControlValue() };
		~tidalSeqButtons2 = 4.collect { OnOffControlValue() };
		~tidalRytButtons2 = 4.collect { OnOffControlValue() };
		~tidalDurAButtons2 = 4.collect { OnOffControlValue() };

		~tidalFolButtons2 = 4.collect { OnOffControlValue() };
		~tidalBufButtons2 = 4.collect { OnOffControlValue() };
		~tidalDurButtons2 = 4.collect { OnOffControlValue() };

		~tidalRatButtons2 = 4.collect { OnOffControlValue() };
		~tidalRanButtons2 = 4.collect { OnOffControlValue() };
		~tidalSpaButtons2 = 4.collect { OnOffControlValue() };
		~tidalRevButtons2 = 4.collect { OnOffControlValue() };*/


		4.do { |i|
			~tidalPreValues[i].value = 0;
			~tidalSeqValues[i].value = 0;
			~tidalRytValues[i].value = 0;
			~tidalDurAValues[i].value = 1; // or O for sound in entire or 1 for cut

			~tidalFolValues[i].value = 0;
			~tidalBufValues[i].value = 0;
			~tidalDurValues[i].value = 1;

			~tidalRatValues[i].value = 0.5;
			~tidalRanValues[i].value = 0.5;
			~tidalSpaValues[i].value = 0;
			~tidalRevValues[i].value = 0;
		};

		~tidalPreButtons.do { |i, id| i.on.connectToUnique({ {~tidalGuiElements[id][\preset][\element].valueAction_(0)}.defer }) };
		~tidalSeqButtons.do { |i, id| i.on.connectToUnique({ {~tidalGuiElements[id][\seqLine][\element].valueAction_(0)}.defer }) };
		~tidalRytButtons.do { |i, id| i.on.connectToUnique({ {~tidalGuiElements[id][\ryt][\element].valueAction_(0)}.defer }) };
		~tidalDurAButtons.do { |i, id| i.on.connectToUnique({ {~tidalGuiElements[id][\legS][\value].valueAction_(1)}.defer }) };
		~tidalFolButtons.do { |i, id| i.on.connectToUnique({ {~tidalGuiElements[id][\buf][\element].valueAction_(0)}.defer }) };
		~tidalBufAButtons.do { |i, id| i.on.connectToUnique({ {~tidalGuiElements[id][\buf2][\element].valueAction_(0)}.defer }) };
		~tidalDurButtons.do { |i, id| i.on.connectToUnique({ {~tidalGuiElements[id][\leg][\element].valueAction_(0.5)}.defer }) };
		~tidalRatButtons.do { |i, id| i.on.connectToUnique({ {~tidalGuiElements[id][\rat][\element].valueAction_(0.5)}.defer }) };
		~tidalRanButtons.do { |i, id| i.on.connectToUnique({ {~tidalGuiElements[id][\ran][\element].valueAction_(0.5)}.defer }) };
		~tidalSpaButtons.do { |i, id| i.on.connectToUnique({ {~tidalGuiElements[id][\spa][\value].valueAction_(0)}.defer }) };
		~tidalRevButtons.do { |i, id| i.on.connectToUnique({ {~tidalGuiElements[id][\reverb][\element].valueAction_(0)}.defer }) };


		~twisterButtons = 6.collect { OnOffControlValue() };
		6.do { |i| ~twister.buttons[i].cv = ~twisterButtons[i] };
		~twister2Buttons = 6.collect { OnOffControlValue() };
		6.do { |i| ~twister2.buttons[i].cv = ~twister2Buttons[i] };

		/*6.do { |i| ~twister3.buttons[i].cv = ~twisterButtons[i] };
		6.do { |i| ~twister4.buttons[i].cv = ~twisterButtons[i] };
		6.do { |i| ~twister5.buttons[i].cv = ~twisterButtons[i] };*/


		// Connexions
		~connectionsTidalPreMake = {

			4.do { |i|
				~twister.knobs[i].knobCV = ~tidalPreValues[i];
				~twister.knobs[i].buttonCV = ~tidalPreValues[i];
				~twister.knobs[i].ledColor_(Color.red);

				~twister.knobs[i+4].knobCV = ~tidalSeqValues[i];
				~twister.knobs[i+4].buttonCV = ~tidalSeqButtons[i];
				~twister.knobs[i+4].ledColor_(Color.blue/*grey*/);

				~twister.knobs[i+8].knobCV = ~tidalRytValues[i];
				~twister.knobs[i+8].buttonCV = ~tidalRytButtons[i];
				~twister.knobs[i+8].ledColor_(Color.yellow);

				~twister.knobs[i+12].knobCV = ~tidalDurAValues[i];
				~twister.knobs[i+12].buttonCV = ~tidalDurAButtons[i];
				~twister.knobs[i+12].ledColor_(Color.blue);
			};

			~connectionsTidalPre = ConnectionList.make {

				4.do { |i|
					[~tidalPreValues[i]].connectEach(\value, [~tidalGuiElements[i][\preset][\element]], _.valueSlot(\valueAction));
					[~tidalGuiElements[i][\preset][\element]].connectEach(\value, [~tidalPreValues[i]], _.valueSlot);
					ViewActionUpdater.enable(~tidalGuiElements[i][\preset][\value]);

					[~tidalSeqValues[i]].connectEach(\value, [~tidalGuiElements[i][\seqLine][\element]], _.valueSlot(\valueAction));
					[~tidalGuiElements[i][\seqLine][\element]].connectEach(\value, [~tidalSeqValues[i]], _.valueSlot);
					ViewActionUpdater.enable(~tidalGuiElements[i][\seqLine][\value]);

					[~tidalRytValues[i]].connectEach(\value, [~tidalGuiElements[i][\ryt][\element]], _.valueSlot(\valueAction));
					[~tidalGuiElements[i][\ryt][\element]].connectEach(\value, [~tidalRytValues[i]], _.valueSlot);
					ViewActionUpdater.enable(~tidalGuiElements[i][\ryt][\value]);

					[~tidalDurAValues[i]].connectEach(\value, [~tidalGuiElements[i][\legS][\value]], _.valueSlot(\valueAction));
					[~tidalGuiElements[i][\legS][\value]].connectEach(\value, [~tidalDurAValues[i]], _.valueSlot);
					ViewActionUpdater.enable(~tidalGuiElements[i][\legS][\value]);
				};
			};
		};

		~connectionsTidalRatMake = {

			4.do { |i|
				~twister.knobs[i].knobCV = ~tidalRatValues[i];
				~twister.knobs[i].buttonCV = ~tidalRatButtons[i];
				~twister.knobs[i].ledColor_(Color.green);

				~twister.knobs[i+4].knobCV = ~tidalRanValues[i];
				~twister.knobs[i+4].buttonCV = ~tidalRanButtons[i];
				~twister.knobs[i+4].ledColor_(Color.grey);

				~twister.knobs[i+8].knobCV = ~tidalSpaValues[i];
				~twister.knobs[i+8].buttonCV = ~tidalSpaButtons[i];
				~twister.knobs[i+8].ledColor_(Color.yellow);

				~twister.knobs[i+12].knobCV = ~tidalRevValues[i];
				~twister.knobs[i+12].buttonCV = ~tidalRevButtons[i];
				~twister.knobs[i+12].ledColor_(Color.blue);
			};

			~connectionsTidalRat = ConnectionList.make {

				4.do { |i|
					[~tidalRatValues[i]].connectEach(\value, [~tidalGuiElements[i][\rat][\element]], _.valueSlot(\valueAction));
					[~tidalGuiElements[i][\rat][\element]].connectEach(\value, [~tidalRatValues[i]], _.valueSlot);
					ViewActionUpdater.enable(~tidalGuiElements[i][\rat][\value]);

					[~tidalRanValues[i]].connectEach(\value, [~tidalGuiElements[i][\ran][\element]], _.valueSlot(\valueAction));
					[~tidalGuiElements[i][\ran][\element]].connectEach(\value, [~tidalRanValues[i]], _.valueSlot);
					ViewActionUpdater.enable(~tidalGuiElements[i][\ran][\value]);

					[~tidalSpaValues[i]].connectEach(\value, [~tidalGuiElements[i][\spa][\value]], _.valueSlot(\valueAction));
					[~tidalGuiElements[i][\spa][\value]].connectEach(\value, [~tidalSpaValues[i]], _.valueSlot);
					ViewActionUpdater.enable(~tidalGuiElements[i][\spa][\value]);

					[~tidalRevValues[i]].connectEach(\value, [~tidalGuiElements[i][\reverb][\element]], _.valueSlot(\valueAction));
					[~tidalGuiElements[i][\reverb][\element]].connectEach(\value, [~tidalRevValues[i]], _.valueSlot);
					ViewActionUpdater.enable(~tidalGuiElements[i][\reverb][\element]);
				};
			};
		};


		4.do { |i|
			~twister2.knobs[i].knobCV = ~tidalRatValues[i];
			~twister2.knobs[i].buttonCV = ~tidalRatButtons[i];
			~twister2.knobs[i].ledColor_(Color.green);

			~twister2.knobs[i+4].knobCV = ~tidalRanValues[i];
			~twister2.knobs[i+4].buttonCV = ~tidalRanButtons[i];
			~twister2.knobs[i+4].ledColor_(Color.grey);

			~twister2.knobs[i+8].knobCV = ~tidalSpaValues[i];
			~twister2.knobs[i+8].buttonCV = ~tidalSpaButtons[i];
			~twister2.knobs[i+8].ledColor_(Color.yellow);

			~twister2.knobs[i+12].knobCV = ~tidalRevValues[i];
			~twister2.knobs[i+12].buttonCV = ~tidalRevButtons[i];
			~twister2.knobs[i+12].ledColor_(Color.blue);
		};


		~twisterButtons[3].on.connectToUnique({ // "test3".postln;
			// ~twister.connect(TwisterDevice(\twister, MIDIClient.sources[~twisterIndex1])); ~twisterInterface = 1;
			// if (~connectionsTidalPre.notNil, {~connectionsTidalPre.disconnect});
			if (~connectionsTidalRat.notNil, {~connectionsTidalRat.disconnect});
			~connectionsTidalPreMake.();
			~connectionsTidalPre.connect;
		});
		~twisterButtons[3].off.connectToUnique({ // "test3".postln;
			// ~twister.connect(TwisterDevice(\twister, MIDIClient.sources[~twisterIndex1])); ~twisterInterface = 1;
			// if (~connectionsTidalPre.notNil, {~connectionsTidalPre.disconnect});
			if (~connectionsTidalRat.notNil, {~connectionsTidalRat.disconnect});
			~connectionsTidalPreMake.();
			~connectionsTidalPre.connect;
		});

		~twisterButtons[4].on.connectToUnique({ // "test1".postln;
			// ~twister2.connect(TwisterDevice(\twister, MIDIClient.sources[~twisterIndex2])); ~twisterInterface = 2;
			if (~connectionsTidalPre.notNil, {~connectionsTidalPre.disconnect});
			// if (~connectionsTidalRat.notNil, {~connectionsTidalRat.disconnect});
			~connectionsTidalRatMake.();
			~connectionsTidalRat.connect;
		});
		~twisterButtons[4].off.connectToUnique({ // "test1".postln;
			// ~twister2.connect(TwisterDevice(\twister, MIDIClient.sources[~twisterIndex2])); ~twisterInterface = 2;
			if (~connectionsTidalPre.notNil, {~connectionsTidalPre.disconnect});
			// if (~connectionsTidalRat.notNil, {~connectionsTidalRat.disconnect});
			~connectionsTidalRatMake.();
			~connectionsTidalRat.connect;
		});

		~twister2Buttons[3].on.connectToUnique({ // "test3".postln;
			// ~twister.connect(TwisterDevice(\twister, MIDIClient.sources[~twisterIndex1])); ~twisterInterface = 1;
			// if (~connectionsTidalPre.notNil, {~connectionsTidalPre.disconnect});
			if (~connectionsTidalRat.notNil, {~connectionsTidalRat.disconnect});
			~connectionsTidalPreMake.();
			~connectionsTidalPre.connect;
		});
		~twister2Buttons[3].off.connectToUnique({ // "test3".postln;
			// ~twister.connect(TwisterDevice(\twister, MIDIClient.sources[~twisterIndex1])); ~twisterInterface = 1;
			// if (~connectionsTidalPre.notNil, {~connectionsTidalPre.disconnect});
			if (~connectionsTidalRat.notNil, {~connectionsTidalRat.disconnect});
			~connectionsTidalPreMake.();
			~connectionsTidalPre.connect;
		});

		~twister2Buttons[4].on.connectToUnique({ // "test1".postln;
			// ~twister2.connect(TwisterDevice(\twister, MIDIClient.sources[~twisterIndex2])); ~twisterInterface = 2;
			if (~connectionsTidalPre.notNil, {~connectionsTidalPre.disconnect});
			// if (~connectionsTidalRat.notNil, {~connectionsTidalRat.disconnect});
			~connectionsTidalRatMake.();
			~connectionsTidalRat.connect;
		});
		~twister2Buttons[4].off.connectToUnique({ // "test1".postln;
			// ~twister2.connect(TwisterDevice(\twister, MIDIClient.sources[~twisterIndex2])); ~twisterInterface = 2;
			if (~connectionsTidalPre.notNil, {~connectionsTidalPre.disconnect});
			// if (~connectionsTidalRat.notNil, {~connectionsTidalRat.disconnect});
			~connectionsTidalRatMake.();
			~connectionsTidalRat.connect;
		});

		"Twister connected".postln;

	});

	// ~connectionsTidalRatMake.();
	// ~connectionsTidalRat.connect;

	~connectionsTidalPreMake.();
	~connectionsTidalPre.connect;

}.fork(AppClock);

