MasterUIX : UIFactories {
	var handler;
	var switchLiveButtonEvent;
	var leftMasterIndicator, rightMasterIndicator;
	var stageMaster;
	var synth, masterGainSynth;

	*new { | initHandler, initSwitchLiveButtonEvent |
		^super.new.init(initHandler, initSwitchLiveButtonEvent);
	}

	init {
		| initHandler, initSwitchLiveButtonEvent |
		var uiKnobFactories = UIKnobFactories();
		handler = initHandler;
		switchLiveButtonEvent = initSwitchLiveButtonEvent;

		stageMaster = Dictionary.new;

		leftMasterIndicator = LevelIndicator.new.minHeight_(120).maxWidth_(12).drawsPeak_(true).warning_(0.9).critical_(1.0);
		leftMasterIndicator.bounds.resizeTo(120, 12);
		rightMasterIndicator = LevelIndicator.new.minHeight_(120).maxWidth_(12).drawsPeak_(true).warning_(0.9).critical_(1.0);
		rightMasterIndicator.bounds.resizeTo(120, 12);

		if (handler.isNil.not, {
			handler.subscribe(this, \releaseAll);
		});


		// Live Button
		stageMaster.put(\live, Dictionary.newFrom([\element,
			~stageMasterButton = Button.new.maxWidth_(80).string_("Live") // maxWidth added
			.action_({ |button|
				if(button.value == 1, {
					synth.run(true);
					this.changeButtonsStatus(true);
				}, {
					synth.run(false);
					this.changeButtonsStatus(false);
				});

				switchLiveButtonEvent.value(button.value);

			})
		]));

		stageMaster[\live][\element].states = [["Live", Color.black, Color.white], ["Live", Color.black /*white*/, Color.new255(238, 180, 34)]];

		uiKnobFactories.knobWithValueLabelFactory3( stageMaster
			, \compThreshold, "Comp Threshold"
			, {|value| value.ampdb.round(1e-2) } //knobToSynthValue
			, {|value| value} // synthToKnobValue
			, "%dB"
			, 0.7
			, {|value|
				synth.set(\compThreshold, value);
			}
		);

		uiKnobFactories.knobWithValueLabelFactory3( stageMaster
			, \limiterLevel, "Limiter Level"
			, {|value| value.ampdb.round(1e-2)} //knobToSynthValue
			, {|value| value} // synthToKnobValue
			, "%dB"
			, 1
			, {|value|
				synth.set(\limiterLevel, value);
			}
		);

		uiKnobFactories.knobWithValueLabelFactory3( stageMaster
			, \highEndDb, "High End dB"
			, {|value| value.linexp(0.0,1.0,1.0, 31.0).round(1e-2) - 1} //knobToSynthValue
			, {|value| value} // synthToKnobValue
			, "%dBs"
			, (3.0).explin(1.0, 31.0, 0.0,1.0)
			, {|value|
				synth.set(\highEndDb, stageMaster[\highEndDb][\knobToSynthValue].value(value));
			}
		);

		this.changeButtonsStatus(false);

		this.addMasterLevelOSCFunc;

		~tidalPresetTrackSize = PathName(~tidalPresetTrackXPath).files.size;
		~tidalPresetLineBegin = 1 ! ~tidalNbOfOrbits;
		~tidalPresetLineEnd = ~tidalPresetTrackSize ! ~tidalNbOfOrbits;
		~tidalSeqLineBegin = 1 ! ~tidalNbOfOrbits;
		~tidalSeqLineEnd = /*~tidalEvalLine[0][~tidalEvalLine[0].size-1]*/ ~tidalEvalLine[0].last ! ~tidalNbOfOrbits;

	}

	handleEvent { |eventName, eventData|
		if (eventName == \releaseAll, {
			synth.free;
			masterGainSynth.free;
		});
	}

	changeButtonsStatus {
		|enable|

		stageMaster[\compThreshold][\element].enabled_(enable);
		stageMaster[\limiterLevel][\element].enabled_(enable);
		stageMaster[\highEndDb][\element].enabled_(enable);
	}

	createUI {
		|container, prMasterBus|

		var gainSlider = Slider.new.fixedWidth_(25).value_(/*0.4*/ 0.5 ).action_({
			| slider |
			masterGainSynth.set(\masterGain, slider.value.linlin(0,1,0,2/*5*/));
			(slider.value*2).round(0.01).postln; // added // linlin not really necessary if only multiplied by 2
		});

		if (prMasterBus.isNil, {
			stageMaster[\live][\element].enabled_(false);
			stageMaster[\live][\element].states_([["Live", Color.grey(0.4), Color.white]]);
		},{
			if (synth.isNil.not, {synth.set(\out, prMasterBus)});
			if (masterGainSynth.isNil.not, {masterGainSynth.set(\out, prMasterBus)});
		});

		if (synth.isNil && currentEnvironment[\SuperDirtMixer][\wasStageMasterSynthCreated] == false, {
			masterGainSynth = Synth.new(\masterGain, target: RootNode(~dirt.server), addAction: \addToTail);
			synth = Synth.newPaused(\stageMaster, target: RootNode(~dirt.server), addAction: \addToTail);
			currentEnvironment[\SuperDirtMixer].put(\wasStageMasterSynthCreated, true);
		});


		// Impossible d'intégrer un EZRanger ???
		/*~eZRangerView = View(container, Rect(0, 0, 200, 100));
        ~eZRanger = EZRanger(/*~tidalWindow*/ /*container*/ ~eZRangerView, 400@16, " test  ", \freq, { |v| v.value.postln }, [50, 2000], unitWidth: 30);*/

		// To change the Master View
		~scrollMeterView = ScrollView(container,Rect(-20, 0, 20, 20)).minHeight_(100).maxHeight_(250);
		~scrollMeterView.canvas = (~dirt.server.meter/*.serverMeter*/.window.asView.bounds_(Rect(0, 0, 20, 20)));

		container.layout = VLayout(
			// Large additions

			HLayout(
				/*if (prMasterBus.isNil.not, { StaticText.new.string_("Track" ++ "\n" ++ "Seq").background_(Color.red).stringColor_(Color.white).maxWidth_(45).maxHeight_(30).align_(\center)}
				)*/
				~tidalTrackSelectionButton = Button.new.minWidth_(40).minHeight_(26).states_([["ALL Tracks", Color.white, Color.black],["Tracks 1-4", Color.white, Color.black],["Tracks 5-8", Color.white, Color.black],["Track", Color.white, Color.black]])
				.action_({ |v|
					case
					{v.value == 0}
					{~tidalTrackSelection = (0..7)}
					{v.value == 1}
					{~tidalTrackSelection = (0..3)}
					{v.value == 2}
					{~tidalTrackSelection = (4..7)}
					{v.value == 3}
					{~tidalTrackSelection = [~tidalTrackSelectionNumBox.value-1]}
				}),

				~tidalTrackSelectionNumBox = NumberBox.new.maxWidth_(24).background_(Color.black).stringColor_(Color.white).normalColor_(Color.white).clipLo_(1).clipHi_(8).step_(1).align_(\center)
				.action_({ |v|
					case
					{~tidalTrackSelectionButton.value == 0}
					{~tidalTrackSelection = (0..7)}
					{~tidalTrackSelectionButton.value == 1}
					{~tidalTrackSelection = (0..3)}
					{~tidalTrackSelectionButton.value == 2}
					{~tidalTrackSelection = (4..7)}
					{~tidalTrackSelectionButton.value == 3}
					{~tidalTrackSelection = [v.value-1]}
					// ~tidalTrackSelection = [v.value-1];
				});
			),

			// 10,

			HLayout(
				~tidalAll0Rand = Button.new.maxWidth_(24).states_([["0", Color.white, Color.black]])
				.action_({ |v|
					~tidalTrackSelection.do { |i|
						~tidalGuiElements[i][\ryt][\value].valueAction_(0);
						~tidalGuiElements[i][\legS][\value].valueAction_(0);
						~tidalGuiElements[i][\leg][\value].valueAction_(1.0);
						~tidalGuiElements[i][\buf][\value].valueAction_(0);
						~tidalGuiElements[i][\buf2][\value].valueAction_(0);
					};
				}),
				~tidalAllRand = Button.new.maxWidth_(30).states_([["A", Color.black, Color.yellow]])
				.action_({ |v|
					~tidalRytARand.valueAction_(1);
					~tidalLegARand.valueAction_(1);
					~tidalBufARand.valueAction_(1);
					~tidalBuf2ARand.valueAction_(1);
					/*4.do { |i| seqLineKnobs[3-i].valueAction_(1.0.rand) };
					4.do { |i| rytKnobs[i].valueAction_(1.0.rand) };
					4.do { |i| bufKnobs[3-i].valueAction_(1.0.rand) };*/
				}),
				~tidalAllFXRand = Button.new.maxWidth_(24).states_([["AX", Color.black, Color.yellow]])
				.action_({ |v|
					~tidalRytARand.valueAction_(1);
					~tidalLegARand.valueAction_(1);
					~tidalBufARand.valueAction_(1);
					~tidalBuf2ARand.valueAction_(1);
					~tidalFx1ARand.valueAction_(1);
					~tidalFx2ARand.valueAction_(1);
					~tidalFu1ARand.valueAction_(1);
					~tidalFu2ARand.valueAction_(1);
				})
			),

			HLayout(
				~tidalPreset0ARand = Button.new.maxWidth_(24).states_([["P1", Color.white, Color.red]])
				.action_({ |v|
					~tidalTrackSelection.do { |i|
						~tidalGuiElements[i][\preset][\value].valueAction_(0/*1*/)
					}
				}),
				~tidalPresetNumBox = NumberBox.new.maxWidth_(24).background_(Color.red).stringColor_(Color.white).normalColor_(Color.white)
				.clipLo_(1).clipHi_(~tidalTrackPresetListView.items.size).step_(1).align_(\center)
				.action_({ |v|
					~tidalTrackSelection.do { |i|
						~tidalGuiElements[i][\preset][\value].valueAction_(v.value);
					};
				}),
				~tidalPresetARand = Button.new.maxWidth_(30).states_([["Pre", Color.white, Color.red]])
				.action_({ |v|
					// {
					~tidalTrackSelection.do { |i|
						~tidalGuiElements[i][\preset][\value].valueAction_(~tidalTrackPresetListView.items.size.rand+1);
						// 0.1.wait;
					};
					// }.fork(AppClock)
			})),

			HLayout(
				// HLayout(~eZRangerView), // Does not work ???
				~tidalPresetLineBeginNumBox = NumberBox.new.maxWidth_(28).maxHeight_(15).background_(Color.white).stringColor_(Color.red).normalColor_(Color.red).clipLo_(1).clipHi_(~tidalPresetLine[0].last).step_(1).align_(\center)
				.action_({ |v|
					~tidalNbOfOrbits.do { |i|
						~tidalPresetLineBegin[i] = v.value.asInteger;
						~tidalPresetLine[i] = (~tidalPresetLineBegin[i]..~tidalPresetLineEnd[i]);
						~tidalGuiElements[i][\presetMin][\value].valueAction_(~tidalPresetLineBegin[i]);
						~tidalGuiElements[i][\preset][\value].clipLo_(~tidalPresetLineBegin[i]);
					};
					// ~tidalPresetLineBeginNumBox.clipHi_(~tidalPresetLineEnd[i]);
					// ~tidalPresetLine = (~tidalPresetLineBegin .. ~tidalPresetLineEnd /*~tidalTrackPresetListView.items.size*/);

				}),

				~tidalPresetLineEndNumBox = NumberBox.new.maxWidth_(28).maxHeight_(15).background_(Color.red).stringColor_(Color.white).normalColor_(Color.white).clipLo_(1).clipHi_(~tidalPresetLine[0].last).step_(1).align_(\center).value_(~tidalPresetLineEnd[0])
				.action_({ |v|
					~tidalNbOfOrbits.do { |i|
						~tidalPresetLineEnd[i] = v.value.asInteger;
						~tidalPresetLine[i] = (~tidalPresetLineBegin[i]..~tidalPresetLineEnd[i]);
						~tidalGuiElements[i][\presetMax][\value].valueAction_(~tidalPresetLineEnd[i]);
						~tidalGuiElements[i][\preset][\value].clipHi_(~tidalPresetLineEnd[i]);
					};
					// ~tidalPresetLineEndNumBox.clipLo_(~tidalPresetLineBegin[i]);
					// ~tidalPresetLine = (~tidalPresetLineBegin .. ~tidalPresetLineEnd /*~tidalTrackPresetListView.items.size*/);
				}),
				~tidalPresetLineNumBox = Button.new.maxWidth_(22).maxHeight_(15).states_([["Sel", Color.white, Color.red]])
				.action_({ |v|
					~tidalPresetLineBeginNumBox.valueAction_(1);
					~tidalPresetLineEndNumBox.valueAction_(~tidalTrackPresetListView.items.size)
				}),
			),

			HLayout(
				~tidalSeq0ARand = Button.new.maxWidth_(20).states_([["S1", Color.white, Color.blue]])
				.action_({ |v|
					// 4.do { |i| seqLineKnobs[3-i].valueAction_(1.0.rand) };
					~tidalTrackSelection.do { |i|
						~tidalGuiElements[i][\seqLine][\value].valueAction_(1)
					}
				}),
				~tidalSeqNumBox = NumberBox.new.maxWidth_(30).background_(Color.blue).stringColor_(Color.white).normalColor_(Color.white).clipLo_(1).clipHi_(~tidalEvalLine[0].last).step_(1).align_(\center)
				.action_({ |v|
					~tidalTrackSelection.do { |i|
						~tidalGuiElements[i][\seqLine][\value].valueAction_(v.value);
					};
				}),
				~tidalSeqARand = Button.new.maxWidth_(28).states_([["Seq", Color.white, Color.blue]])
				.action_({ |v|
					~tidalTrackSelection.do { |i|
						~tidalGuiElements[i][\seqLine][\value].valueAction_(~tidalEvalLine[i].choose)
					}
				}),
			),

			HLayout(
				// HLayout(~eZRangerView), // Does not work ???
				~tidalSeqLineBeginNumBox = NumberBox.new.maxWidth_(28).maxHeight_(15).background_(Color.white).stringColor_(Color.black).normalColor_(Color.black).clipLo_(1).clipHi_(~tidalEvalLine[0].last).step_(1).align_(\center)
				.action_({ |v|
					~tidalNbOfOrbits.do { |i|
						~tidalSeqLineBegin[i] = v.value.asInteger;
						~tidalEvalLine[i] = (~tidalSeqLineBegin[i]..~tidalSeqLineEnd[i]);
						~tidalGuiElements[i][\seqLineMin][\value].valueAction_(~tidalSeqLineBegin[i]);
						// ~tidalSeqLineBeginNumBox.clipHi_(~tidalSeqLineEnd[i]);
					};
				}),

				~tidalSeqLineEndNumBox = NumberBox.new.maxWidth_(28).maxHeight_(15).background_(Color.blue).stringColor_(Color.yellow).normalColor_(Color.yellow).clipLo_(1).clipHi_(~tidalEvalLine[0].last).step_(1).align_(\center).value_(~tidalEvalLine[0].last)
				.action_({ |v|
					~tidalNbOfOrbits.do { |i|
						~tidalSeqLineEnd[i] = v.value.asInteger;
						~tidalEvalLine[i] = (~tidalSeqLineBegin[i]..~tidalSeqLineEnd[i]);
						~tidalGuiElements[i][\seqLineMax][\value].valueAction_(~tidalSeqLineEnd[i]);
						// ~tidalGuiElements[i][\seqLineMax][\value].clipHi_(~tidalSeqLineEnd)
					};
					// ~tidalSeqLineEndNumBox.clipLo_(~tidalSeqLineBegin[i]);
				}),

				~tidalSeqLineNumBox = Button.new.maxWidth_(22).maxHeight_(15).states_([["Sel", Color.white, Color.blue]])
				.action_({ |v|
					~tidalSeqLineBeginNumBox.valueAction_(1);
					~tidalSeqLineEndNumBox.valueAction_(~tidalSeqLineEndR)
				}),
			),

			5,

			HLayout(
				~tidalRyt0ARand = Button.new.maxWidth_(24).states_([["R0", Color.blue, Color.new255(255, 165, 0)]])
				.action_({ |v|
					/*8*/ ~tidalTrackSelection.do { |i|
						~tidalGuiElements[i][\rytS][\value].valueAction_(0);
						~tidalGuiElements[i][\ryt][\element].valueAction_(0);
					};
				}),
				~tidalRytSNumBox = NumberBox.new.maxWidth_(14).background_(Color.new255(255, 165, 0)).stringColor_(Color.blue).normalColor_(Color.blue).clipLo_(0).clipHi_(5).step_(1)
				.action_({ |v|
					/*8*/ ~tidalTrackSelection.do { |i|
						~tidalGuiElements[i][\rytS][\value].valueAction_(v.value);
					};
				}),
				~tidalRytSARand = Button.new.maxWidth_(24).states_([["RS", Color.blue, Color.new255(255, 165, 0)]])
				.action_({ |v|
					/*8*/ ~tidalTrackSelection.do { |i|
						~tidalGuiElements[i][\rytS][\value].valueAction_(3.rand);
					};
				}),
				~tidalRytARand = Button.new.maxWidth_(24).states_([["Ryt", Color.blue, Color.new255(255, 165, 0)]])
				.action_({ |v|
					/*8*/ ~tidalTrackSelection.do { |i| var val; // rytKnobs[i].valueAction_(1.0.rand) };
						if (~patternSpeedSwitch[i] >= 1, {val=~patternSpeeds[i][1.0.rand*~patternSpeedsSize[i]]}, {val=1.0.rand*64});
						~superDirtOSC.sendMsg("/SuperDirtMixer/ryt", i, val);
						if (val < 1, {~tidalGuiElements[i][\ryt][\value].normalColor_(Color.red)}, {~tidalGuiElements[i][\ryt][\value].normalColor_(Color.blue)});
						// ~superDirtOSC.sendMsg("/SuperDirtMixer/ryt", i, (~patternSpeeds[1.0.rand*(~patternSpeeds.size-1)]))
					};
				})
			),

			HLayout(
				~tidalLegP1 = Button.new.maxWidth_(20).states_([["S0", Color.yellow, Color.new255(139, 58, 58)]])
				.action_({ |v|
					~tidalTrackSelection.do { |i|
						~tidalGuiElements[i][\legS][\value].valueAction_(0);
					};
				}),
				~tidalLegP2 = Button.new.maxWidth_(20).states_([["C1", Color.yellow, Color.new255(139, 58, 58)]])
				.action_({ |v|
					~tidalTrackSelection.do { |i|
						~tidalGuiElements[i][\legS][\value].valueAction_(1);
					};
				}),
				~tidalLegP3 = Button.new.maxWidth_(20).states_([["L1", Color.yellow, Color.new255(139, 58, 58)]])
				.action_({ |v|
					~tidalTrackSelection.do { |i|
						~tidalGuiElements[i][\legS][\value].valueAction_(2);
					};
				}),
				~tidalLegP4 = Button.new.maxWidth_(20).states_([["L2", Color.yellow, Color.new255(139, 58, 58)]])
				.action_({ |v|
					~tidalTrackSelection.do { |i|
						~tidalGuiElements[i][\legS][\value].valueAction_(5);
					};
				}),
			),

			HLayout(
				~tidalLegP5 = Button.new.maxWidth_(20).states_([["L", Color.yellow, Color.new255(139, 58, 58)]])
				.action_({ |v|
					~tidalTrackSelection.do { |i|
						~tidalGuiElements[i][\legS][\value].valueAction_(10);
					};
				}),
				~tidalLegP6 = Button.new.maxWidth_(20).states_([["LS", Color.yellow, Color.new255(139, 58, 58)]])
				.action_({ |v|
					~tidalTrackSelection.do { |i|
						~tidalGuiElements[i][\legS][\value].valueAction_(15);
					};
				}),
				~tidalLegP7 = Button.new.maxWidth_(20).states_([["S1", Color.yellow, Color.new255(139, 58, 58)]])
				.action_({ |v|
					~tidalTrackSelection.do { |i|
						~tidalGuiElements[i][\legS][\value].valueAction_(21);
					};
				}),
				~tidalLegP8 = Button.new.maxWidth_(20).states_([["C-", Color.yellow, Color.new255(139, 58, 58)]])
				.action_({ |v|
					~tidalTrackSelection.do { |i|
						~tidalGuiElements[i][\legS][\value].valueAction_(39);
					};
				}),
			),

			HLayout(
				~tidalLeg0ARand = Button.new.maxWidth_(20).states_([["D0", Color.yellow, Color.new255(139, 58, 58)]])
				.action_({ |v|
					~tidalTrackSelection.do { |i|
						~tidalGuiElements[i][\legS][\value].valueAction_(0);
						~tidalGuiElements[i][\leg][\value].valueAction_(1.0)
					};
				}),
				~tidalLegSNumBox = NumberBox.new.maxWidth_(24).background_(Color.white/*Color.new255(139, 58, 58)*/).stringColor_(Color.black/*yellow*/).normalColor_(Color.black/*yellow*/).align_(\center).clipLo_(0).clipHi_(~tidalnumDurAlgs).step_(1)
				.action_({ |v|
					~tidalTrackSelection.do { |i|
						~tidalGuiElements[i][\legS][\value].valueAction_(v.value);
					};
				}).value_(1),
				~tidalLegSARand = Button.new.maxWidth_(20).states_([["DS", Color.yellow, Color.new255(139, 58, 58)]])
				.action_({ |v|
					~tidalTrackSelection.do { |i|
						~tidalGuiElements[i][\legS][\value].valueAction_(41.rand)
					};
				}),
				~tidalLegARand = Button.new.maxWidth_(20).states_([["Du", Color.yellow, Color.new255(139, 58, 58)]])
				.action_({ |v|
					~tidalTrackSelection.do { |i|
						~tidalGuiElements[i][\leg][\element].valueAction_(1.0.rand)
					};
				})
			),

			HLayout(
				~tidalCut0Rand = Button.new.maxWidth_(22).states_([["CO", Color.yellow, Color.black]])
				.action_({ |v|
					~tidalTrackSelection.do { |i|
						~tidalGuiElements[i][\xon][\value].valueAction_(i+1);
					};
				}),
				~tidalCut1Rand = Button.new.maxWidth_(22).states_([["C1", Color.yellow, Color.black]])
				.action_({ |v|
					~tidalTrackSelection.do { |i|
						~tidalGuiElements[i][\xon][\value].valueAction_(1);
					};
				}),
				~tidalCut2Rand = Button.new.maxWidth_(20).states_([["C2", Color.yellow, Color.black]])
				.action_({ |v|
					~tidalTrackSelection.do { |i|
						~tidalGuiElements[i][\xon][\value].valueAction_([1,1,3,3,5,5,7,7][i]);
					};
				}),
				~tidalCutARand = Button.new.maxWidth_(20).states_([["CR", Color.yellow, Color.black]])
				.action_({ |v|
					~tidalTrackSelection.do { |i|
						~tidalGuiElements[i][\xon][\value].valueAction_(~tidalNbOfOrbits.rand);
					};
				})
			),

			// 5,

			HLayout(
				~tidalBufSyn0Rand = Button.new.maxWidth_(34).maxHeight_(18).states_([["B", Color.white, Color.red(0.7)], ["S", Color.red(0.7), Color.white]])
				.action_({ |v|
					~tidalTrackSelection.do { |i|
						~tidalGuiElements[i][\bufS2][\value].valueAction_(v.value);
					};
				}),
				~tidalBufSynRand = Button.new.maxWidth_(34).maxHeight_(18).states_([["BR", Color.white, Color.red(0.7)], ["SR", Color.red(0.7), Color.white]])
				.action_({ |v|
					~tidalTrackSelection.do { |i|
						~tidalGuiElements[i][\bufS2][\value].valueAction_(2.rand);
					};
				})
			),

			HLayout(
				~tidalBufA0Rand = Button.new.maxWidth_(24).states_([["B4", Color.yellow, Color.red(0.7)],["B0", Color.yellow, Color.red(0.7)]])
				.action_({ |v|
					if (~tidalBuf0ARandSwitch == 0, {
						~tidalBuf0ARandSwitch = 1;
						~tidalTrackSelection.do { |i|
							~tidalGuiElements[i][\bufS][\value].valueAction_(0);
							~tidalGuiElements[i][\buf][\element].valueAction_(0);
							~tidalGuiElements[i][\buf2][\element].valueAction_(0);
						};
					},{
						~tidalTrackSelection.do { |i|
							~tidalBuf0ARandSwitch = 0;
							~tidalGuiElements[i][\bufS][\value].valueAction_(4);
							~tidalGuiElements[i][\buf][\element].valueAction_(0);
							~tidalGuiElements[i][\buf2][\element].valueAction_(0);
						};
					});
				}),
				~tidalBufSNumBox = NumberBox.new.maxWidth_(14).background_(Color.red(0.7)).stringColor_(Color.yellow).normalColor_(Color.yellow).clipLo_(0).clipHi_(4).step_(1)
				.action_({ |v|
					~tidalTrackSelection.do { |i|
						~tidalGuiElements[i][\bufS][\value].valueAction_(v.value);
					};
				}),
				~tidalBufARand = Button.new.maxWidth_(24).states_([["Fo", Color.yellow, Color.red(0.7)]])
				.action_({ |v|
					~tidalTrackSelection.do { |i| // bufKnobs[3-i].valueAction_(1.0.rand) };
						// ~superDirtOSC.sendMsg("/SuperDirtMixer/buf", i, ((1.0.rand*~arrayOfFolderNames4TidalSize[0]).asInteger))
						~tidalGuiElements[i][\buf][\element].valueAction_(1.0.rand)
					};
				}),
				~tidalBuf2ARand = Button.new.maxWidth_(24).states_([["Bu", Color.yellow, Color.magenta(0.7)]])
				.action_({ |v|
					~tidalTrackSelection.do { |i|
						~tidalGuiElements[i][\buf2][\element].valueAction_(1.0.rand)
					};
				})
			),

			HLayout(
				~tidalR0Rand = Button.new.maxWidth_(24).states_([["R0", Color.yellow, Color.green(0.4)]])
				.action_({ |v|
					~tidalTrackSelection.do { |i|
						~tidalGuiElements[i][\rat][\element].valueAction_(0.5);
						~tidalGuiElements[i][\ran][\element].valueAction_(0.5)
					};
				}),
				~tidalRatARand = Button.new.maxWidth_(24).states_([["Rt", Color.yellow, Color.green(0.4)]])
				.action_({ |v|
					~tidalTrackSelection.do { |i| // bufKnobs[3-i].valueAction_(1.0.rand) };
						// ~superDirtOSC.sendMsg("/SuperDirtMixer/buf", i, ((1.0.rand*~arrayOfFolderNames4TidalSize[0]).asInteger))
						~tidalGuiElements[i][\rat][\element].valueAction_(1.0.rand)
					};
				}),
				~tidalRanARand = Button.new.maxWidth_(24).states_([["Ra", Color.yellow, Color.gray(0.4)]])
				.action_({ |v|
					~tidalTrackSelection.do { |i|
						~tidalGuiElements[i][\ran][\element].valueAction_(1.0.rand)
					};
				})
			),

			// 5,

			HLayout(
				~tidalF0ARand = Button.new.maxWidth_(24).states_([["0", Color.black, Color.white], ["1", Color.black, Color.white]])
				.action_({ |v|
					~tidalF0ARandSwitch = v.value;
					if (~tidalF0ARandSwitch == 0,
						{ ~tidalF0ARandSwitch = 1;
							~tidalTrackSelection.do { |i| // rytKnobs[i].valueAction_(1.0.rand) };
								~superDirtOSC.sendMsg("/SuperDirtMixer/fxs", i, 0);
								~superDirtOSC.sendMsg("/SuperDirtMixer/fxp", i, 0);
								~superDirtOSC.sendMsg("/SuperDirtMixer/fxt", i, 0);
								~superDirtOSC.sendMsg("/SuperDirtMixer/fxv", i, 0);
								~superDirtOSC.sendMsg("/SuperDirtMixer/fxs2", i, 0);
								~superDirtOSC.sendMsg("/SuperDirtMixer/fxp2", i, 0);
								~superDirtOSC.sendMsg("/SuperDirtMixer/fxt2", i, 0);
								~superDirtOSC.sendMsg("/SuperDirtMixer/fxv2", i, 0);
								~superDirtOSC.sendMsg("/SuperDirtMixer/fus", i, 0);
								~superDirtOSC.sendMsg("/SuperDirtMixer/fup", i, 0);
								~superDirtOSC.sendMsg("/SuperDirtMixer/fut", i, 0);
								~superDirtOSC.sendMsg("/SuperDirtMixer/fuv", i, 0);
								~superDirtOSC.sendMsg("/SuperDirtMixer/fus2", i, 0);
								~superDirtOSC.sendMsg("/SuperDirtMixer/fup2", i, 0);
								~superDirtOSC.sendMsg("/SuperDirtMixer/fut2", i, 0);
								~superDirtOSC.sendMsg("/SuperDirtMixer/fuv2", i, 0);
							};
						},{ ~tidalF0ARandSwitch = 1;
							~tidalTrackSelection.do { |i| // rytKnobs[i].valueAction_(1.0.rand) };
								~superDirtOSC.sendMsg("/SuperDirtMixer/fxs", i, 1);
								// ~superDirtOSC.sendMsg("/SuperDirtMixer/fxp", i, 0);
								~superDirtOSC.sendMsg("/SuperDirtMixer/fxt", i, 0.9);
								~superDirtOSC.sendMsg("/SuperDirtMixer/fxv", i, 1);
								~superDirtOSC.sendMsg("/SuperDirtMixer/fxs2", i, 1);
								// ~superDirtOSC.sendMsg("/SuperDirtMixer/fxp", i, 0);
								~superDirtOSC.sendMsg("/SuperDirtMixer/fxt2", i, 0.9);
								~superDirtOSC.sendMsg("/SuperDirtMixer/fxv2", i, 1);
								~superDirtOSC.sendMsg("/SuperDirtMixer/fus", i, 1);
								// ~superDirtOSC.sendMsg("/SuperDirtMixer/fup", i, 0);
								~superDirtOSC.sendMsg("/SuperDirtMixer/fut", i, 0.9);
								~superDirtOSC.sendMsg("/SuperDirtMixer/fuv", i, 1);
								~superDirtOSC.sendMsg("/SuperDirtMixer/fus2", i, 1);
								// ~superDirtOSC.sendMsg("/SuperDirtMixer/fup", i, 0);
								~superDirtOSC.sendMsg("/SuperDirtMixer/fut2", i, 0.9);
								~superDirtOSC.sendMsg("/SuperDirtMixer/fuv2", i, 1);
							};
					});
				}),
				~tidalFARand = Button.new.maxWidth_(24).states_([["F", Color.black, Color.white/*(0.6)*/]])
				.action_({ |v|
					~tidalFxARand.valueAction_(1);
					~tidalFuARand.valueAction_(1);
				}),
				~tidalFpARand = Button.new.maxWidth_(24).states_([["P", Color.black, (Color.white/*(0.6)*/)]])
				.action_({ |v|
					~tidalFxpARand.valueAction_(1);
					~tidalFupARand.valueAction_(1);
				})
			),

			HLayout(
				~tidalFx10ARand = Button.new.maxWidth_(24).states_([["10", Color.white, Color.green(0.6)], ["11", Color.white, Color.green(0.6)]])
				.action_({ |v|
					~tidalFx10ARandSwitch = v.value;
					if (~tidalFx10ARandSwitch == 0,
						{ ~tidalFx10ARandSwitch = 1;
							~tidalTrackSelection.do { |i| // rytKnobs[i].valueAction_(1.0.rand) };
								~superDirtOSC.sendMsg("/SuperDirtMixer/fxs", i, 0);
								~superDirtOSC.sendMsg("/SuperDirtMixer/fxp", i, 0);
								~superDirtOSC.sendMsg("/SuperDirtMixer/fxt", i, 0);
								~superDirtOSC.sendMsg("/SuperDirtMixer/fxv", i, 0);
							};
						},{ ~tidalFx10ARandSwitch = 1;
							~tidalTrackSelection.do { |i| // rytKnobs[i].valueAction_(1.0.rand) };
								~superDirtOSC.sendMsg("/SuperDirtMixer/fxs", i, 1);
								// ~superDirtOSC.sendMsg("/SuperDirtMixer/fxp", i, 0);
								~superDirtOSC.sendMsg("/SuperDirtMixer/fxt", i, 0.9);
								~superDirtOSC.sendMsg("/SuperDirtMixer/fxv", i, 1);
							};
					});
				}),
				~tidalFx1ARand = Button.new.maxWidth_(24).states_([["Fx1", Color.white, Color.green(0.6)]])
				.action_({ |v|
					~tidalTrackSelection.do { |i| // rytKnobs[i].valueAction_(1.0.rand) };
						~superDirtOSC.sendMsg("/SuperDirtMixer/fxs", i, 3.rand);
						~superDirtOSC.sendMsg("/SuperDirtMixer/fxp", i, 1.0.rand);
						~superDirtOSC.sendMsg("/SuperDirtMixer/fxt", i, 1.0.rand);
						~superDirtOSC.sendMsg("/SuperDirtMixer/fxv", i, 1.0.rand);
					};
				}),
				~tidalFxp1ARand = Button.new.maxWidth_(24).states_([["Px1", Color.white, Color.green(0.6)]])
				.action_({ |v|
					~tidalTrackSelection.do { |i| // seqLineKnobs[3-i].valueAction_(1.0.rand) };
						~superDirtOSC.sendMsg("/SuperDirtMixer/fxp", i, 1.0.rand)
					};
				})
			),

			HLayout(
				~tidalFx20ARand = Button.new.maxWidth_(24).states_([["20", Color.white, Color.green(0.3)], ["21", Color.white, Color.green(0.6)]])
				.action_({ |v|
					~tidalFx20ARandSwitch = v.value;
					if (~tidalFx20ARandSwitch == 0,
						{ ~tidalFx20ARandSwitch = 1;
							~tidalTrackSelection.do { |i| // rytKnobs[i].valueAction_(1.0.rand) };
								~superDirtOSC.sendMsg("/SuperDirtMixer/fxs2", i, 0);
								~superDirtOSC.sendMsg("/SuperDirtMixer/fxp2", i, 0);
								~superDirtOSC.sendMsg("/SuperDirtMixer/fxt2", i, 0);
								~superDirtOSC.sendMsg("/SuperDirtMixer/fxv2", i, 0);
							};
						},{ ~tidalFx20ARandSwitch = 1;
							~tidalTrackSelection.do { |i| // rytKnobs[i].valueAction_(1.0.rand) };
								~superDirtOSC.sendMsg("/SuperDirtMixer/fxs2", i, 1);
								// ~superDirtOSC.sendMsg("/SuperDirtMixer/fxp2", i, 0);
								~superDirtOSC.sendMsg("/SuperDirtMixer/fxt2", i, 0.9);
								~superDirtOSC.sendMsg("/SuperDirtMixer/fxv2", i, 1);
							};
					});
				}),
				~tidalFx2ARand = Button.new.maxWidth_(24).states_([["Fx2", Color.white, Color.green(0.3)]])
				.action_({ |v|
					~tidalTrackSelection.do { |i| // rytKnobs[i].valueAction_(1.0.rand) };
						~superDirtOSC.sendMsg("/SuperDirtMixer/fxs2", i, 3.rand);
						~superDirtOSC.sendMsg("/SuperDirtMixer/fxp2", i, 1.0.rand);
						~superDirtOSC.sendMsg("/SuperDirtMixer/fxt2", i, 1.0.rand);
						~superDirtOSC.sendMsg("/SuperDirtMixer/fxv2", i, 1.0.rand);
					};
				}),
				~tidalFxp2ARand = Button.new.maxWidth_(24).states_([["Px2", Color.white, Color.green(0.3)]])
				.action_({ |v|
					~tidalTrackSelection.do { |i| // seqLineKnobs[3-i].valueAction_(1.0.rand) };
						~superDirtOSC.sendMsg("/SuperDirtMixer/fxp2", i, 1.0.rand)
					};
				})
			),

			HLayout(
				~tidalFx0ARand = Button.new.maxWidth_(24).states_([["F0", Color.white, Color.green(0.6)], ["F1", Color.white, Color.green(0.6)]])
				.action_({ |v|
					~tidalFx0ARandSwitch = v.value;
					if (~tidalFx0ARandSwitch == 0,
						{ ~tidalFx0ARandSwitch = 1;
							~tidalTrackSelection.do { |i| // rytKnobs[i].valueAction_(1.0.rand) };
								~superDirtOSC.sendMsg("/SuperDirtMixer/fxs", i, 0);
								~superDirtOSC.sendMsg("/SuperDirtMixer/fxp", i, 0);
								~superDirtOSC.sendMsg("/SuperDirtMixer/fxt", i, 0);
								~superDirtOSC.sendMsg("/SuperDirtMixer/fxv", i, 0);
								~superDirtOSC.sendMsg("/SuperDirtMixer/fxs2", i, 0);
								~superDirtOSC.sendMsg("/SuperDirtMixer/fxp2", i, 0);
								~superDirtOSC.sendMsg("/SuperDirtMixer/fxt2", i, 0);
								~superDirtOSC.sendMsg("/SuperDirtMixer/fxv2", i, 0);
							};
						},{ ~tidalFx0ARandSwitch = 1;
							~tidalTrackSelection.do { |i| // rytKnobs[i].valueAction_(1.0.rand) };
								~superDirtOSC.sendMsg("/SuperDirtMixer/fxs", i, 1);
								// ~superDirtOSC.sendMsg("/SuperDirtMixer/fxp", i, 0);
								~superDirtOSC.sendMsg("/SuperDirtMixer/fxt", i, 0.9);
								~superDirtOSC.sendMsg("/SuperDirtMixer/fxv", i, 1);
								~superDirtOSC.sendMsg("/SuperDirtMixer/fxs2", i, 1);
								// ~superDirtOSC.sendMsg("/SuperDirtMixer/fxp", i, 0);
								~superDirtOSC.sendMsg("/SuperDirtMixer/fxt2", i, 0.9);
								~superDirtOSC.sendMsg("/SuperDirtMixer/fxv2", i, 1);
							};
					});
				}),
				~tidalFxARand = Button.new.maxWidth_(24).states_([["Fx", Color.white, Color.green(0.6)]])
				.action_({ |v|
					~tidalFx1ARand.valueAction_(1);
					~tidalFx2ARand.valueAction_(1);
				}),
				~tidalFxpARand = Button.new.maxWidth_(24).states_([["Px", Color.white, Color.green(0.6)]])
				.action_({ |v|
					~tidalFxp1ARand.valueAction_(1);
					~tidalFxp2ARand.valueAction_(1);
				})
			),

			HLayout(
				~tidalFu10ARand = Button.new.maxWidth_(24).states_([["10", Color.white, Color.blue(1)], ["11", Color.white, Color.blue(1)]])
				.action_({ |v|
					~tidalFu10ARandSwitch = v.value;
					if (~tidalFu10ARandSwitch == 0,
						{ ~tidalFu10ARandSwitch = 1;
							~tidalTrackSelection.do { |i| // rytKnobs[i].valueAction_(1.0.rand) };
								~superDirtOSC.sendMsg("/SuperDirtMixer/fus", i, 0);
								~superDirtOSC.sendMsg("/SuperDirtMixer/fup", i, 0);
								~superDirtOSC.sendMsg("/SuperDirtMixer/fut", i, 0);
								~superDirtOSC.sendMsg("/SuperDirtMixer/fuv", i, 0);
							};
						},{ ~tidalFu10ARandSwitch = 1;
							~tidalTrackSelection.do { |i| // rytKnobs[i].valueAction_(1.0.rand) };
								~superDirtOSC.sendMsg("/SuperDirtMixer/fus", i, 1);
								// ~superDirtOSC.sendMsg("/SuperDirtMixer/fup", i, 0);
								~superDirtOSC.sendMsg("/SuperDirtMixer/fut", i, 0.9);
								~superDirtOSC.sendMsg("/SuperDirtMixer/fuv", i, 1);
							};
					});
				}),
				~tidalFu1ARand = Button.new.maxWidth_(24).states_([["Fu1", Color.white, Color.blue(1)]])
				.action_({ |v|
					~tidalTrackSelection.do { |i| // rytKnobs[i].valueAction_(1.0.rand) };
						~superDirtOSC.sendMsg("/SuperDirtMixer/fus", i, 3.rand);
						~superDirtOSC.sendMsg("/SuperDirtMixer/fup", i, 1.0.rand);
						~superDirtOSC.sendMsg("/SuperDirtMixer/fut", i, 1.0.rand);
						~superDirtOSC.sendMsg("/SuperDirtMixer/fuv", i, 1.0.rand);
					};
				}),
				~tidalFup1ARand = Button.new.maxWidth_(24).states_([["Pu1", Color.white, Color.blue(1)]])
				.action_({ |v|
					~tidalTrackSelection.do { |i| // seqLineKnobs[3-i].valueAction_(1.0.rand) };
						~superDirtOSC.sendMsg("/SuperDirtMixer/fup", i, 1.0.rand)
					};
				})
			),

			HLayout(
				~tidalFu20ARand = Button.new.maxWidth_(24).states_([["20", Color.white, Color.blue(0.3)], ["21", Color.white, Color.blue(0.6)]])
				.action_({ |v|
					~tidalFu20ARandSwitch = v.value;
					if (~tidalFu20ARandSwitch == 0,
						{ ~tidalFu20ARandSwitch = 1;
							~tidalTrackSelection.do { |i| // rytKnobs[i].valueAction_(1.0.rand) };
								~superDirtOSC.sendMsg("/SuperDirtMixer/fus2", i, 0);
								~superDirtOSC.sendMsg("/SuperDirtMixer/fup2", i, 0);
								~superDirtOSC.sendMsg("/SuperDirtMixer/fut2", i, 0);
								~superDirtOSC.sendMsg("/SuperDirtMixer/fuv2", i, 0);
							};
						},{ ~tidalFu20ARandSwitch = 1;
							~tidalTrackSelection.do { |i| // rytKnobs[i].valueAction_(1.0.rand) };
								~superDirtOSC.sendMsg("/SuperDirtMixer/fus2", i, 1);
								// ~superDirtOSC.sendMsg("/SuperDirtMixer/fup2", i, 0);
								~superDirtOSC.sendMsg("/SuperDirtMixer/fut2", i, 0.9);
								~superDirtOSC.sendMsg("/SuperDirtMixer/fuv2", i, 1);
							};
					});
				}),
				~tidalFu2ARand = Button.new.maxWidth_(24).states_([["Fu2", Color.white, Color.blue(0.3)]])
				.action_({ |v|
					~tidalTrackSelection.do { |i| // rytKnobs[i].valueAction_(1.0.rand) };
						~superDirtOSC.sendMsg("/SuperDirtMixer/fus2", i, 3.rand);
						~superDirtOSC.sendMsg("/SuperDirtMixer/fup2", i, 1.0.rand);
						~superDirtOSC.sendMsg("/SuperDirtMixer/fut2", i, 1.0.rand);
						~superDirtOSC.sendMsg("/SuperDirtMixer/fuv2", i, 1.0.rand);
					};
				}),
				~tidalFup2ARand = Button.new.maxWidth_(24).states_([["Pu2", Color.white, Color.blue(0.3)]])
				.action_({ |v|
					~tidalTrackSelection.do { |i| // seqLineKnobs[3-i].valueAction_(1.0.rand) };
						~superDirtOSC.sendMsg("/SuperDirtMixer/fup2", i, 1.0.rand)
					};
				})
			),

			HLayout(
				~tidalFu0ARand = Button.new.maxWidth_(24).states_([["U0", Color.white, Color.blue(1)], ["U1", Color.white, Color.blue(1)]])
				.action_({ |v|
					~tidalFu0ARandSwitch = v.value;
					if (~tidalFu0ARandSwitch == 0,
						{ ~tidalFu0ARandSwitch = 1;
							~tidalTrackSelection.do { |i| // rytKnobs[i].valueAction_(1.0.rand) };
								~superDirtOSC.sendMsg("/SuperDirtMixer/fus", i, 0);
								~superDirtOSC.sendMsg("/SuperDirtMixer/fup", i, 0);
								~superDirtOSC.sendMsg("/SuperDirtMixer/fut", i, 0);
								~superDirtOSC.sendMsg("/SuperDirtMixer/fuv", i, 0);
								~superDirtOSC.sendMsg("/SuperDirtMixer/fus2", i, 0);
								~superDirtOSC.sendMsg("/SuperDirtMixer/fup2", i, 0);
								~superDirtOSC.sendMsg("/SuperDirtMixer/fut2", i, 0);
								~superDirtOSC.sendMsg("/SuperDirtMixer/fuv2", i, 0);
							};
						},{ ~tidalFx0ARandSwitch = 1;
							~tidalTrackSelection.do { |i| // rytKnobs[i].valueAction_(1.0.rand) };
								~superDirtOSC.sendMsg("/SuperDirtMixer/fus", i, 1);
								// ~superDirtOSC.sendMsg("/SuperDirtMixer/fup", i, 0);
								~superDirtOSC.sendMsg("/SuperDirtMixer/fut", i, 0.9);
								~superDirtOSC.sendMsg("/SuperDirtMixer/fuv", i, 1);
								~superDirtOSC.sendMsg("/SuperDirtMixer/fus2", i, 1);
								// ~superDirtOSC.sendMsg("/SuperDirtMixer/fup", i, 0);
								~superDirtOSC.sendMsg("/SuperDirtMixer/fut2", i, 0.9);
								~superDirtOSC.sendMsg("/SuperDirtMixer/fuv2", i, 1);
							};
					});
				}),
				~tidalFuARand = Button.new.maxWidth_(24).states_([["Fu", Color.white, Color.blue(1)]])
				.action_({ |v|
					~tidalFu1ARand.valueAction_(1);
					~tidalFu2ARand.valueAction_(1);
				}),
				~tidalFupARand = Button.new.maxWidth_(24).states_([["Pu", Color.white, Color.blue(1)]])
				.action_({ |v|
					~tidalFup1ARand.valueAction_(1);
					~tidalFup2ARand.valueAction_(1);
				})
			),

			HLayout(
				~tidalSpa0ARand = Button.new.maxWidth_(25).states_([["S0", Color.black, Color.yellow/*, Color.new255(139, 58, 58)*/]])
				.action_({ |v|
					~tidalTrackSelection.do { |i|
						~tidalGuiElements[i][\spa][\value].valueAction_(1);
					};
				}),
				~tidalSpaANumBox = NumberBox.new.maxWidth_(25).background_(Color.yellow/*Color.new255(139, 58, 58)*/).stringColor_(Color.black).normalColor_(Color.black).align_(\center).clipLo_(0).clipHi_(~tidalnumSpaAlgs).step_(1)
				.action_({ |v|
					~tidalTrackSelection.do { |i|
						~tidalGuiElements[i][\spa][\value].valueAction_(v.value);
					};
				}).value_(1),
				~tidalSpaARand = Button.new.maxWidth_(25).states_([["SR", Color.black, Color.yellow /*, Color.new255(139, 58, 58)*/]])
				.action_({ |v|
					~tidalTrackSelection.do { |i|
						~tidalGuiElements[i][\spa][\value].valueAction_(~tidalnumSpaAlgs.rand)
					};
				})
			),

			5,
			HLayout(StaticText.new.string_("---------------").fixedHeight_(15).maxWidth_(100).align_(\center)),


			/* DEFINE MASTER UI : EXTRACT -> Stage master */
			/*2,
			StaticText.new.string_("Master").align_(\center),*/
			5 /*2*/,
			HLayout(stageMaster[\live][\element]),
			5,
			HLayout(stageMaster[\compThreshold][\title]),
			HLayout(~stageMasterCompThreshold = stageMaster[\compThreshold][\element]),
			HLayout(stageMaster[\compThreshold][\value]),
			5,
			HLayout(stageMaster[\limiterLevel][\title]),
			HLayout(~stageMasterLimiterLevel = stageMaster[\limiterLevel][\element]),
			HLayout(stageMaster[\limiterLevel][\value]),
			5,
			HLayout(stageMaster[\highEndDb][\title]),
			HLayout(~stageMasterHighEndDb = stageMaster[\highEndDb][\element]),
			HLayout(stageMaster[\highEndDb][\value]),
			5,

			HLayout(StaticText.new.string_("---------------").fixedHeight_(15).maxWidth_(100).align_(\center)),
			5,

			HLayout(if (prMasterBus.isNil.not, { StaticText.new.string_("Master")/*.minWidth_(40 /*100*/)*/.maxWidth_(38).maxHeight_(30).align_(\center).font = Font(/*"Monaco"*/ /*"Verdana"*/ "Arial", 12, bold: true);
			}),

			// To control the Master Volume
			~tidalMasterMulNumBox = NumberBox().maxHeight_(20).maxWidth_(44) // maxHeight & maxWidth added


			.decimals_(0)
			.clipLo_(-80).clipHi_(0).align_(\center)
			.scroll_step_(1).value_(0).action_({|a|

			/*.decimals_(2)
			.clipLo_(0).clipHi_(1).align_(\center)
			.scroll_step_(0.05).value_(1).action_({|a|*/

				~tidalMasterMul = a.value/*.ampdb*/;
				~dirt.server.volume = ~tidalMasterMul;
			});

			).spacing_(0).margins_(0), // minWidth & maxHeight added
			// Below to uncomment to get original stereo Master
			// HLayout(if (prMasterBus.isNil.not, { HLayout(leftMasterIndicator,rightMasterIndicator).spacing_(4)})),

			// Trial to get an ServerMeter within a ScrollView, but not resizable - To do it with another way XXX
			// HLayout(leftMasterIndicator,rightMasterIndicator, gainSlider).spacing_(0),
			HLayout(gainSlider, if (prMasterBus.isNil.not, { ~scrollMeterView })).spacing_(0).margins_(0),

			5,

			// if (this.prMasterBus.isNil.not, { HLayout(leftMasterIndicator,rightMasterIndicator).spacing_(0)}),
			HLayout(
				~tidalMuteAllButton = Button.new.maxWidth_(40).states_([["M All", Color.black, Color.white], ["M All", Color.white, Color.red]])
				.action_({ |view|
					// (0..(dirt.orbits.size - 1)).do({ |i| muteButtons[i].value_(view.value) });
					// Comment mettre à jour chaque bouton mute de chaque piste ???
					{if (view.value == 1, { ~tidalNbOfOrbits.do { |i| ~tidalGuiElements[i][\mute][\value].valueAction_(1) } }, { ~tidalNbOfOrbits.do { |i| ~tidalGuiElements[i][\mute][\value].valueAction_(0) } }) }.defer;
				}),
				~tidalMute2AllButton = Button.new.maxWidth_(40).states_([["X All", Color.black, Color.white], ["X All", Color.white, Color.red]])
				.action_({ |view|
					// (0..(dirt.orbits.size - 1)).do({ |i| muteButtons[i].value_(view.value) });
					// Comment mettre à jour chaque bouton mute de chaque piste ???
					{if (view.value == 1, { ~tidalNbOfOrbits.do { |i| ~tidalGuiElements[i][\mute2][\value].valueAction_(1) } }, { ~tidalNbOfOrbits.do { |i| ~tidalGuiElements[i][\mute2][\value].valueAction_(0) } }) }.defer;
				})
			),
			2,
			HLayout(Button.new.maxWidth_(50).string_("Res EQ").action_({
				/*equiView.value = EQuiParams.new();
				equiView.target = activeOrbit.globalEffects[0].synth;*/
			}))
		).spacing_(2).margins_(2); // to modify to 2
	}

	addMasterLevelOSCFunc {
		OSCFunc({ |msg|
			{
				try {
					var rmsL = msg[4].ampdb.linlin(-80, 0, 0, 1);
					var peakL = msg[3].ampdb.linlin(-80, 0, 0, 1, \min);
					var rmsR = msg[6].ampdb.linlin(-80, 0, 0, 1);
					var peakR = msg[5].ampdb.linlin(-80, 0, 0, 1, \min);
					leftMasterIndicator.value = rmsL;
					leftMasterIndicator.peakLevel = peakL;
					rightMasterIndicator.value = rmsR;
					rightMasterIndicator.peakLevel = peakR;

				} { |error|
					if(error.isKindOf(PrimitiveFailedError).not) { error.throw }
				};
			}.defer;
		}, ("/MixerMasterOutLevels").asSymbol, Server.local.addr).fix;
	}

}