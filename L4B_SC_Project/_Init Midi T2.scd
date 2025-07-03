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

		~tidalrytValues = 4.collect { NumericControlValue(spec:[0, 1]) };
		4.do { |i|
		~twister.knobs[i].knobCV = ~tidalrytValues[i];
		~twister.knobs[i].ledColor_(Color.yellow);
		}


	});



		"Twister connected".postln;

	// });

}.fork;