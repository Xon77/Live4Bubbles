{

	MIDIClient.sources.do { |i, id| /*i.device.postln;*/
		if (i.device == "Midi Fighter Twister 1", {~twisterIndex1 = id});
		if (i.device == "Midi Fighter Twister 2", {~twisterIndex2 = id});
		if (i.device == "Midi Fighter Twister 3", {~twisterIndex3 = id});
	};


	~bufFolColor = Color.red(0.9);
	~bufColor = Color.new255(/*255*/240, 75/*10*/, 110/*120*/);
	~offColor = Color.new255(148, 0, 211);
	~legColor = Color.blue;
	~rtmColor = Color.yellow(0.6);
	~proColor = Color.new255(139, 90, 0);
	~cenColor = Color.cyan(0.6)/*new255(0, 206, 209)*/ /*new255(34, 39, 34)*//*Color.green*/;
	~strColor = Color.green(0.6); // dark sea green


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

		~tidalRatValues = 4.collect { NumericControlValue(spec:[0, 1]) };
		~tidalRanValues = 4.collect { NumericControlValue(spec:[0, 1]) };
		~tidalSpaValues = 4.collect { NumericControlValue(spec:[0, 25]) };
		~tidalRevValues = 4.collect { NumericControlValue(spec:[0, 1]) };

		~tidalRatButtons = 4.collect { OnOffControlValue() };
		~tidalRanButtons = 4.collect { OnOffControlValue() };
		~tidalSpaButtons = 4.collect { OnOffControlValue() };
		~tidalRevButtons = 4.collect { OnOffControlValue() };

		~tidalRatButtons.do { |i, id| i.signal(\off).connectToUnique({"test".postln; ~tidalGuiElements[id][\rat][\element].valueAction_(0.5) }) };
		~tidalRatButtons.do { |i, id| i.signal(\on).connectToUnique({"test".postln; ~tidalGuiElements[id][\rat][\element].valueAction_(0.5) }) };

		// ~tidalGuiElements[0][\rat][\element].valueAction_(0.5)

		~connectionsTidalRatMake = {

			4.do { |i|

				~twister.knobs[i].knobCV = ~tidalRatValues[i];
				~twister.knobs[i].buttonCV = ~tidalRatButtons[i];
				// ~twister.buttons[i].cv = ~tidalRatButtons[i];
				~tidalRatValues[i].value = 0.5;
				~twister.knobs[i].ledColor_(Color.green);

				~twister.knobs[i+4].knobCV = ~tidalRanValues[i];
				~twister.knobs[i+4].buttonCV = ~tidalRanButtons[i];
				~tidalRanValues[i].value = 0.5;
				~twister.knobs[i+4].ledColor_(Color.grey);

				~twister.knobs[i+8].knobCV = ~tidalSpaValues[i];
				~twister.knobs[i+8].buttonCV = ~tidalSpaButtons[i];
				~tidalSpaValues[i].value = 0;
				~twister.knobs[i+8].ledColor_(Color.yellow);

				~twister.knobs[i+12].knobCV = ~tidalRevValues[i];
				~twister.knobs[i+12].buttonCV = ~tidalRevButtons[i];
				~tidalRevValues[i].value = 0;
				~twister.knobs[i+12].ledColor_(Color.blue);
			};

			~tidalRatButtons.[0].value.postln;
			// ~tidalRatButtons.[0].signal(\off);
			// ~tidalRatValues[0].value
			// ~tidalRatButtons.do { |i, id| i.signal(\off).connectToUnique({ ~tidalRatValues[id].value = 0.5; }); i.postln; };

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

			// ~tidalRatButtons.do { |i, id| i.signal(\off).connectToUnique({ {~tidalGuiElements[id][\rat][\element].valueAction_(0.5)}.defer; i.postln; }) };
			~tidalRatButtons.do { |i, id| i.signal(\on).connectToUnique({ {~tidalGuiElements[id][\rat][\element].valueAction_(0.5)}.defer }) };

			};
		};

		"Twister connected".postln;

	});

	~connectionsTidalRatMake.();
	~connectionsTidalRat.connect;

}.fork(AppClock);


// ~tidalRatButtons[0].signal(\off).connectToUnique({ ~tidalGuiElements[id][\rat][\element].valueAction_(0.5) })
