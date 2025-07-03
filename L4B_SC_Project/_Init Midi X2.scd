~xController = MKtl('xoneK2', "xoneK2");

// To evaluate lines of code via OSC - To connect via MIDI with XoneK2 - DJ Livecoding
~tidalEvalAddr = NetAddr.new("127.0.0.1", 3333);

~tidalEvalFuncP = { |tr|
	~tidalEvalPos[tr] = ~tidalEvalPos[tr]+1; if (~tidalEvalPos[tr] >= (~tidalEvalLine[tr].size), {~tidalEvalPos[tr] = 0});
	{~tidalGuiElements[tr][\seqLine][\value].valueAction_(~tidalEvalLine[tr][~tidalEvalPos[tr]])}.defer;
	// ~tidalEvalAddr.sendMsg("/pulsar/eval", \type, 'line', 'tab', (0+tr), \row, ~tidalEvalLine[tr][~tidalEvalPos[tr]], \column, 1);
	// ~superDirtOSC.sendMsg("/SuperDirtMixer/seqLine", tr, ~tidalEvalLine[tr][~tidalEvalPos[tr]]);
	// ("Track" + (tr+1) + "-> Line" + ~tidalEvalLine[tr][~tidalEvalPos[tr]]).postln
};
~tidalEvalFuncM = { |tr|
	~tidalEvalPos[tr] = (~tidalEvalPos[tr]-1); if ((~tidalEvalPos[tr]) <= (-1), {~tidalEvalPos[tr] = (~tidalEvalLine[tr].size-1)});
	{~tidalGuiElements[tr][\seqLine][\value].valueAction_(~tidalEvalLine[tr][~tidalEvalPos[tr]])}.defer;
	// ~tidalEvalAddr.sendMsg("/pulsar/eval", \type, 'line', 'tab', (0+tr), \row, ~tidalEvalLine[tr][~tidalEvalPos[tr]], \column, 1);
	// ~superDirtOSC.sendMsg("/SuperDirtMixer/seqLine", tr, ~tidalEvalLine[tr][~tidalEvalPos[tr]]);
	// ("Track" + (tr+1) + "-> Line" + ~tidalEvalLine[tr][~tidalEvalPos[tr]]).postln
};


~tidalPresetTrackSize = PathName(~tidalPresetTrackXPath).files.size;
~tidalPresetLine = (1 .. ~tidalPresetTrackSize /*~tidalTrackPresetListView.items.size*/) ! ~tidalNbOfOrbits;

~tidalPresetFuncP = { |tr|
	~tidalPresetNb[tr] = ~tidalPresetNb[tr]+1; if (~tidalPresetNb[tr] >= (~tidalPresetLineEnd[tr] /*~tidalPresetTrackSize*/), {~tidalPresetNb[tr] = 0});
	// {~tidalGuiElements[tr][\preset][\value].valueAction_(~tidalPresetLine[~tidalPresetNb[tr]])}.defer;
	{~tidalGuiElements[tr][\preset][\value].valueAction_(~tidalPresetNb[tr])}.defer;
};

~tidalPresetFuncM = { |tr|
	// ~tidalPresetTrackSize
	~tidalPresetNb[tr] = (~tidalPresetNb[tr]-1); if ((~tidalPresetNb[tr]) <= ~tidalPresetLineBegin[tr], {~tidalPresetNb[tr] = (~tidalPresetLineEnd[tr]/*~tidalPresetTrackSize*/)});
	{~tidalGuiElements[tr][\preset][\value].valueAction_(~tidalPresetNb[tr])}.defer;
};


~tidalAllPresetsPos = 0;
~tidalAllPresetsLine = (1 .. /*13*/ ~tidalAllPresetListView.items.size-1 /*~tidalTrackPresetListView.items.size*/);

~tidalAllPresetsFuncP = { |tr|
	~tidalAllPresetsPos = ~tidalAllPresetsPos+1; if (~tidalAllPresetsPos >=  ~tidalAllPresetListView.items.size ? 10 /*13*/, {~tidalAllPresetsPos = 0});
	{~tidalAllPresetListView.valueAction_(~tidalAllPresetsPos)}.defer;
};
~tidalAllPresetsFuncM = { |tr|
	~tidalAllPresetsPos = (~tidalAllPresetsPos-1); if ((~tidalAllPresetsPos) <= (-1), {~tidalAllPresetsPos = (~tidalAllPresetListView.items.size-1 /*13-1*/)});
	{~tidalAllPresetListView.valueAction_(~tidalAllPresetsPos)}.defer;
};


// ~tidalGuiElements[0][\fxs][\value].value;

~tidalFXFuncEnc1P = { // |tr|
	var sel, newPos;
	case
	{~tidalFxFuncSwitch == 0 and: {~tidalFxFuncNb == 0}}
	{sel = \fxs}
	{~tidalFxFuncSwitch == 1 and: {~tidalFxFuncNb == 0}}
	{sel = \fus}
	{~tidalFxFuncSwitch == 0 and: {~tidalFxFuncNb == 1}}
	{sel = \fxs2}
	{~tidalFxFuncSwitch == 1 and: {~tidalFxFuncNb == 1}}
	{sel = \fus2};
	{newPos = ~tidalGuiElements[~tidalCurrentOrbit][sel][\value].value+1; if (newPos > ~tidalnumWhenS, {newPos = 0});
		~tidalGuiElements[~tidalCurrentOrbit][sel][\value].valueAction_(newPos)}.defer;
};

~tidalFXFuncEnc1M = { // |tr|
	var sel, newPos;
	case
	{~tidalFxFuncSwitch == 0 and: {~tidalFxFuncNb == 0}}
	{sel = \fxs}
	{~tidalFxFuncSwitch == 1 and: {~tidalFxFuncNb == 0}}
	{sel = \fus}
	{~tidalFxFuncSwitch == 0 and: {~tidalFxFuncNb == 1}}
	{sel = \fxs2}
	{~tidalFxFuncSwitch == 1 and: {~tidalFxFuncNb == 1}}
	{sel = \fus2};
	{newPos = ~tidalGuiElements[~tidalCurrentOrbit][sel][\value].value-1; if (newPos <= (-1), {newPos = ~tidalnumWhenS});
		~tidalGuiElements[~tidalCurrentOrbit][sel][\value].valueAction_(newPos)}.defer;
};


// ~tidalGuiElements[0][\fxx][\value].value

~tidalFXFuncEnc2P = { // |tr|
	var sel, newPos;
	case
	{~tidalFxFuncSwitch == 0 and: {~tidalFxFuncNb == 0}}
	{sel = \fxx}
	{~tidalFxFuncSwitch == 1 and: {~tidalFxFuncNb == 0}}
	{sel = \fux}
	{~tidalFxFuncSwitch == 0 and: {~tidalFxFuncNb == 1}}
	{sel = \fxx2}
	{~tidalFxFuncSwitch == 1 and: {~tidalFxFuncNb == 1}}
	{sel = \fux2};
	{newPos = ~tidalGuiElements[~tidalCurrentOrbit][sel][\value].value+1; if (newPos > ~tidalnumSwitchX, {newPos = 0});
		~tidalGuiElements[~tidalCurrentOrbit][sel][\value].valueAction_(newPos)}.defer;
};

~tidalFXFuncEnc2M = { // |tr|
	var sel, newPos;
	case
	{~tidalFxFuncSwitch == 0 and: {~tidalFxFuncNb == 0}}
	{sel = \fxx}
	{~tidalFxFuncSwitch == 1 and: {~tidalFxFuncNb == 0}}
	{sel = \fux}
	{~tidalFxFuncSwitch == 0 and: {~tidalFxFuncNb == 1}}
	{sel = \fxx2}
	{~tidalFxFuncSwitch == 1 and: {~tidalFxFuncNb == 1}}
	{sel = \fux2};
	{newPos = ~tidalGuiElements[~tidalCurrentOrbit][sel][\value].value-1; if (newPos <= (-1), {newPos = ~tidalnumSwitchX});
		~tidalGuiElements[~tidalCurrentOrbit][sel][\value].valueAction_(newPos)}.defer;
};


// ~tidalGuiElements[0][\fxp][\value].value

~tidalFXFuncEnc3P = { // |tr|
	var sel, selMax, newPos;
	case
	{~tidalFxFuncSwitch == 0 and: {~tidalFxFuncNb == 0}}
	{sel = \fxp; selMax = ~tidalnumEffects}
	{~tidalFxFuncSwitch == 1 and: {~tidalFxFuncNb == 0}}
	{sel = \fup; selMax = ~tidalnumFunctions}
	{~tidalFxFuncSwitch == 0 and: {~tidalFxFuncNb == 1}}
	{sel = \fxp2; selMax = ~tidalnumEffects}
	{~tidalFxFuncSwitch == 1 and: {~tidalFxFuncNb == 1}}
	{sel = \fup2; selMax = ~tidalnumFunctions};
	{newPos = ~tidalGuiElements[~tidalCurrentOrbit][sel][\value].value+1; if (newPos > selMax, {newPos = 0});
		~tidalGuiElements[~tidalCurrentOrbit][sel][\value].valueAction_(newPos)}.defer;
};

~tidalFXFuncEnc3M = { // |tr|
	var sel, selMax, newPos;
	case
	{~tidalFxFuncSwitch == 0 and: {~tidalFxFuncNb == 0}}
	{sel = \fxp; selMax = ~tidalnumEffects}
	{~tidalFxFuncSwitch == 1 and: {~tidalFxFuncNb == 0}}
	{sel = \fup; selMax = ~tidalnumFunctions}
	{~tidalFxFuncSwitch == 0 and: {~tidalFxFuncNb == 1}}
	{sel = \fxp2; selMax = ~tidalnumEffects}
	{~tidalFxFuncSwitch == 1 and: {~tidalFxFuncNb == 1}}
	{sel = \fup2; selMax = ~tidalnumFunctions};
	{newPos = ~tidalGuiElements[~tidalCurrentOrbit][sel][\value].value-1; if (newPos <= (-1), {newPos = selMax});
		~tidalGuiElements[~tidalCurrentOrbit][sel][\value].valueAction_(newPos)}.defer;
};


// ~tidalGuiElements[0][\fxt][\value].value;

~tidalFXFuncBut3 = { |val|
	var sel, newPos;
	case
	{~tidalFxFuncSwitch == 0 and: {~tidalFxFuncNb == 0}}
	{sel = \fxt}
	{~tidalFxFuncSwitch == 1 and: {~tidalFxFuncNb == 0}}
	{sel = \fut}
	{~tidalFxFuncSwitch == 0 and: {~tidalFxFuncNb == 1}}
	{sel = \fxt2}
	{~tidalFxFuncSwitch == 1 and: {~tidalFxFuncNb == 1}}
	{sel = \fut2};
	{~tidalGuiElements[~tidalCurrentOrbit][sel][\value].valueAction_(val)}.defer;
};


// ~tidalGuiElements[0][\fxv][\value].value;

~tidalFXFuncBut4 = { |val|
	var sel, newPos;
	case
	{~tidalFxFuncSwitch == 0 and: {~tidalFxFuncNb == 0}}
	{sel = \fxv}
	{~tidalFxFuncSwitch == 1 and: {~tidalFxFuncNb == 0}}
	{sel = \fuv}
	{~tidalFxFuncSwitch == 0 and: {~tidalFxFuncNb == 1}}
	{sel = \fxv2}
	{~tidalFxFuncSwitch == 1 and: {~tidalFxFuncNb == 1}}
	{sel = \fuv2};
	{~tidalGuiElements[~tidalCurrentOrbit][sel][\value].valueAction_(val)}.defer;
};



// All individual Controls

~xEnc1 = ~xController.elementAt(\Enc1);
~xEnc2 = ~xController.elementAt(\Enc2);
~xEnc3 = ~xController.elementAt(\Enc3);
~xEnc4 = ~xController.elementAt(\Enc4);
~xEnc5 = ~xController.elementAt(\Enc5);
~xEnc6 = ~xController.elementAt(\Enc6);
~xEnc1On = ~xController.elementAt(\Enc1On); ~xEnc1Off = ~xController.elementAt(\Enc1Off);
~xEnc2On = ~xController.elementAt(\Enc2On); ~xEnc2Off = ~xController.elementAt(\Enc2Off);
~xEnc3On = ~xController.elementAt(\Enc3On); ~xEnc3Off = ~xController.elementAt(\Enc3Off);
~xEnc4On = ~xController.elementAt(\Enc4On); ~xEnc4Off = ~xController.elementAt(\Enc4Off);
~xEnc5On = ~xController.elementAt(\Enc5On); ~xEnc5Off = ~xController.elementAt(\Enc5Off);
~xEnc6On = ~xController.elementAt(\Enc6On); ~xEnc6Off = ~xController.elementAt(\Enc5Off);
~xHi1 = ~xController.elementAt(\Hi1);
~xHi2 = ~xController.elementAt(\Hi2);
~xHi3 = ~xController.elementAt(\Hi3);
~xHi4 = ~xController.elementAt(\Hi4);
~xHi1On = ~xController.elementAt(\Hi1On);
~xHi2On = ~xController.elementAt(\Hi2On);
~xHi3On = ~xController.elementAt(\Hi3On);
~xHi4On = ~xController.elementAt(\Hi4On);
~xMi1 = ~xController.elementAt(\Mi1);
~xMi2 = ~xController.elementAt(\Mi2);
~xMi3 = ~xController.elementAt(\Mi3);
~xMi4 = ~xController.elementAt(\Mi4);
~xMi1On = ~xController.elementAt(\Mi1On); ~xMi1Off = ~xController.elementAt(\Mi1Off);
~xMi2On = ~xController.elementAt(\Mi2On); ~xMi2Off = ~xController.elementAt(\Mi2Off);
~xMi3On = ~xController.elementAt(\Mi3On); ~xMi3Off = ~xController.elementAt(\Mi3Off);
~xMi4On = ~xController.elementAt(\Mi4On); ~xMi4Off = ~xController.elementAt(\Mi4Off);
~xLo1 = ~xController.elementAt(\Lo1);
~xLo2 = ~xController.elementAt(\Lo2);
~xLo3 = ~xController.elementAt(\Lo3);
~xLo4 = ~xController.elementAt(\Lo4);
~xLo1On = ~xController.elementAt(\Lo1On); ~xLo1Off = ~xController.elementAt(\Lo1Off);
~xLo2On = ~xController.elementAt(\Lo2On); ~xLo2Off = ~xController.elementAt(\Lo2Off);
~xLo3On = ~xController.elementAt(\Lo3On); ~xLo3Off = ~xController.elementAt(\Lo3Off);
~xLo4On = ~xController.elementAt(\Lo4On); ~xLo4Off = ~xController.elementAt(\Lo4Off);

~xAOn = ~xController.elementAt(\AOn);
~xBOn = ~xController.elementAt(\BOn);
~xCOn = ~xController.elementAt(\COn);
~xDOn = ~xController.elementAt(\DOn);
~xEOn = ~xController.elementAt(\EOn);
~xFOn = ~xController.elementAt(\FOn);
~xGOn = ~xController.elementAt(\GOn);
~xHOn = ~xController.elementAt(\HOn);
~xIOn = ~xController.elementAt(\IOn);
~xJOn = ~xController.elementAt(\JOn);
~xKOn = ~xController.elementAt(\KOn);
~xLOn = ~xController.elementAt(\LOn);
~xMOn = ~xController.elementAt(\MOn);
~xNOn = ~xController.elementAt(\NOn);
~xOOn = ~xController.elementAt(\OOn);
~xPOn = ~xController.elementAt(\POn);

// List of Controls

~xEncs = [~xEnc1, ~xEnc2, ~xEnc3, ~xEnc4];
~xEncsOn = [~xEnc1On, ~xEnc2On, ~xEnc3On, ~xEnc4On];
~xHis = [~xHi1, ~xHi2, ~xHi3, ~xHi4];
~xHisOn = [~xHi1On, ~xHi2On, ~xHi3On, ~xHi4On];
~xMis = [~xMi1, ~xMi2, ~xMi3, ~xMi4];
~xMisOn = [~xMi1On, ~xMi2On, ~xMi3On, ~xMi4On];
~xLos = [~xLo1, ~xLo2, ~xLo3, ~xLo4];
~xLosOn = [~xLo1On, ~xLo2On, ~xLo3On, ~xLo4On];


// Controller Actions

// Too diffcult to standardize - too many different actions between encoders
/*~xEncs.do { |c, i|
	c.action_({ |v|
		case
		{ ~tidalPreSeqSwitch == 0 }
		{ if (v.value == 1, { ~tidalEvalFuncM.(i) /*("down"+i).postln*/ }, { ~tidalEvalFuncP.(i) /*("up"+i).postln*/ }) }
		{ ~tidalPreSeqSwitch == 1 }
		{ if (v.value == 1, { ~tidalPresetFuncM.(i) /*("down"+i).postln*/}, { ~tidalPresetFuncP.(i) /*("up"+i).postln*/ }) };
		/*if (~tidalPreSeqSwitch == 0,
			{
if (v.value == 1, { ~tidalEvalFuncM.(i) /*("down"+i).postln*/ }, { ~tidalEvalFuncP.(i) /*("up"+i).postln*/ })
			},{
if (v.value == 1, { ~tidalPresetFuncM.(i) /*("down"+i).postln*/}, { ~tidalPresetFuncP.(i) /*("up"+i).postln*/ })
		})*/
	});
};*/

~xEnc1.action_({ |v|
	case
	{ ~tidalPreSeqSwitch == 0 }
{ if (v.value == 1, { ~tidalEvalFuncM.(0) /*"down1".postln*/}, { ~tidalEvalFuncP.(0) /*"up1".postln*/ }) }
	{ ~tidalPreSeqSwitch == 1 }
{ if (v.value == 1, { ~tidalPresetFuncM.(0) /*"down1".postln*/}, { ~tidalPresetFuncP.(0) /*"up1".postln*/ }) }
	{ ~tidalPreSeqSwitch == 2 }
{ if (v.value == 1, { ~tidalFXFuncEnc1M.(0) /*"down1".postln*/}, { ~tidalFXFuncEnc1P.(0) /*"up1".postln*/ }) }
});

~xEnc2.action_({ |v|
	case
	{ ~tidalPreSeqSwitch == 0 }
{ if (v.value == 1, { ~tidalEvalFuncM.(1) /*"down2".postln*/}, { ~tidalEvalFuncP.(1) /*"up2".postln*/ }) }
	{ ~tidalPreSeqSwitch == 1 }
{ if (v.value == 1, { ~tidalPresetFuncM.(1) /*"down2".postln*/}, { ~tidalPresetFuncP.(1) /*"up2".postln*/ }) }
	{ ~tidalPreSeqSwitch == 2 }
{ if (v.value == 1, { ~tidalFXFuncEnc2M.(0) /*"down1".postln*/}, { ~tidalFXFuncEnc2P.(0) /*"up1".postln*/ }) }
});

~xEnc3.action_({ |v|
	case
	{ ~tidalPreSeqSwitch == 0 }
{ if (v.value == 1, { ~tidalEvalFuncM.(2) /*"down3".postln*/}, { ~tidalEvalFuncP.(2) /*"up3".postln*/ }) }
	{ ~tidalPreSeqSwitch == 1 }
{ if (v.value == 1, { ~tidalPresetFuncM.(2) /*"down3".postln*/}, { ~tidalPresetFuncP.(2) /*"up3".postln*/ }) }
	{ ~tidalPreSeqSwitch == 2 }
{ if (v.value == 1, { ~tidalFXFuncEnc3M.(0) /*"down1".postln*/}, { ~tidalFXFuncEnc3P.(0) /*"up1".postln*/ }) }
});

~xEnc4.action_({ |v|
	case
	{ ~tidalPreSeqSwitch == 0 }
{ if (v.value == 1, { ~tidalEvalFuncM.(3) /*"down4".postln*/}, { ~tidalEvalFuncP.(3) /*"up4".postln*/ }) }
	{ ~tidalPreSeqSwitch == 1 }
{ if (v.value == 1, { ~tidalPresetFuncM.(3) /*"down4".postln*/}, { ~tidalPresetFuncP.(3) /*"up4".postln*/ }) }
});


/*~xEncsOn.do { |c, i| c.action_({ |v|
{~tidalGuiElements[i][\eq][\element].valueAction_(1)}.defer;
}) };*/

~xEnc1On.action_({ |v|
	// ~tidalEvalAddr.sendMsg("/pulsar/eval", \type, 'line', 'tab', (4), \row, ~tidalEvalLine[0].choose.postln, \column, 1);
	// ~superDirtOSC.sendMsg("/SuperDirtMixer/seqLine", 0, ~tidalEvalLine[0].choose); // "Enc1On".postln
	{~tidalGuiElements[0][\preset][\value].valueAction_(~tidalPresetLine[0][~tidalPresetNb[0]]);
		~tidalGuiElements[0][\eq][\element].valueAction_(1)}.defer;
	~xEnc1On.value_(1); ~xEnc2On.value_(0); ~xEnc3On.value_(0); ~xEnc4On.value_(0);
});

~xEnc2On.action_({ |v|
	{~tidalGuiElements[1][\preset][\value].valueAction_(~tidalPresetLine[1][~tidalPresetNb[1]]);
		~tidalGuiElements[1][\eq][\element].valueAction_(1)}.defer;
	~xEnc1On.value_(0); ~xEnc2On.value_(1); ~xEnc3On.value_(0); ~xEnc4On.value_(0);
});

~xEnc3On.action_({ |v|
	{~tidalGuiElements[2][\preset][\value].valueAction_(~tidalPresetLine[2][~tidalPresetNb[2]]);
		~tidalGuiElements[2][\eq][\element].valueAction_(1)}.defer;
	~xEnc1On.value_(0); ~xEnc2On.value_(0); ~xEnc3On.value_(1); ~xEnc4On.value_(0);
});

~xEnc4On.action_({ |v|
	{~tidalGuiElements[3][\preset][\value].valueAction_(~tidalPresetLine[3][~tidalPresetNb[3]]);
		~tidalGuiElements[3][\eq][\element].valueAction_(1)}.defer;
	~xEnc1On.value_(0); ~xEnc2On.value_(0); ~xEnc3On.value_(0); ~xEnc4On.value_(1);
});

// {~xEnc1On.doAction}.defer;
~xEnc1On.value_(1); ~xEnc2On.value_(0); ~xEnc3On.value_(0); ~xEnc4On.value_(0);


~tidalReverbType = 0;
~tidalReverbVST = ["V_Vger", "V_16NoteSentry", "V_LibraNebula", "V_LibraRising", "V_LibraHall", "V_DarkLibraPlate", "V_LibraBloom", "V_LostSaucer", "V_SlowAttackEnsemble", "V_ScorpioRising", "V_DarkHorse", "V_HorseHead", "V_Dotted8Orbits", "V_Reverse8Note", "V_CirrusMajorSynthEchoes", "V_Planetarium", "V_TheVoid", "V_WeAreStardust"];
~tidalReverbVSTSize = ~tidalReverbVST.size;
~tidalReverbPos = 0;

~tidalReverbP = {
	~tidalReverbPos = ~tidalReverbPos+1; if (~tidalReverbPos >= ~tidalReverbVSTSize, {~tidalReverbPos = 0});
	~valhalla.loadPreset(~tidalReverbVST[~tidalReverbPos]);
	(~tidalReverbPos + ~tidalReverbVST[~tidalReverbPos]).postln
};
~tidalReverbM = {
	~tidalReverbPos = (~tidalReverbPos-1); if ((~tidalReverbPos) <= (-1), {~tidalReverbPos = ~tidalReverbVSTSize-1});
	~valhalla.loadPreset(~tidalReverbVST[~tidalReverbPos]);
	(~tidalReverbPos + ~tidalReverbVST[~tidalReverbPos]).postln
};

~xEnc5On.action_({ |v| if (~tidalReverbType == 0, { // ~xEOn.valueAction_(1);
	~tidalX.setReverbVariableName(\reverb); "Custom VST".postln; ~tidalReverbType = 1;
	{~tidalGuiElements[0][\reverb][\element].valueAction_(~tidalprevRev1Value)}.defer;
},{ // ~xEOn.valueAction_(0);
	~tidalX.setReverbVariableName(\room); "Native Reverb".postln; ~tidalReverbType = 0;
	{~tidalGuiElements[0][\reverb][\element].valueAction_(~tidalprevRev1Value)}.defer;
})
});

~xEnc5.action_({ |v| if (v.value == 1, {
	~tidalX.setReverbVariableName(\reverb); "Custom VST".postln; ~tidalReverbM.();
{~tidalGuiElements[0][\reverb][\element].valueAction_(~tidalprevRev1Value); ~tidalGuiElements[1][\reverb][\element].valueAction_(~tidalprevRev2Value); ~tidalGuiElements[2][\reverb][\element].valueAction_(~tidalprevRev3Value); ~tidalGuiElements[3][\reverb][\element].valueAction_(~tidalprevRev4Value)}.defer; /*"down1".postln*/
},{
	~tidalX.setReverbVariableName(\reverb); "Custom VST".postln; ~tidalReverbP.();
{~tidalGuiElements[0][\reverb][\element].valueAction_(~tidalprevRev1Value); ~tidalGuiElements[1][\reverb][\element].valueAction_(~tidalprevRev2Value); ~tidalGuiElements[2][\reverb][\element].valueAction_(~tidalprevRev3Value); ~tidalGuiElements[3][\reverb][\element].valueAction_(~tidalprevRev4Value)}.defer; /*"up1".postln*/})
});


~tidalRecTempPos = 0;

~xEnc6.action_({ |v| // v.value.postln;

	if (v.value == 1, {
		~tidalAllPresetsFuncM.();
	},{
		~tidalAllPresetsFuncP.()
	});

	/*if (v.value == 1, {
		~tidalRecTempPos = (~tidalRecTempPos -1) % 3
	},{
		~tidalRecTempPos = (~tidalRecTempPos +1) % 3
	});

	// ~tidalRecTempPos.postln;

	if (~tidalRecTempPos == 0, {
		if (~tidalRecTemp[~tidalRecTempPos] != 2, {
			~tidalRecTemp[~tidalRecTempPos] = 1;
			if (~tidalRecTemp[~tidalRecTempPos-1%3] != 2, {~tidalRecTemp[~tidalRecTempPos-1%3] = 0});
			if (~tidalRecTemp[~tidalRecTempPos+1%3] != 2, {~tidalRecTemp[~tidalRecTempPos+1%3] = 0});
		})
	});

	if (~tidalRecTempPos == 1, {
		if (~tidalRecTemp[~tidalRecTempPos] != 2, {
			~tidalRecTemp[~tidalRecTempPos] = 1;
			if (~tidalRecTemp[~tidalRecTempPos-1%3] != 2, {~tidalRecTemp[~tidalRecTempPos-1%3] = 0});
			if (~tidalRecTemp[~tidalRecTempPos+1%3] != 2, {~tidalRecTemp[~tidalRecTempPos+1%3] = 0});
		})
	});

	if (~tidalRecTempPos == 2, {
		if (~tidalRecTemp[~tidalRecTempPos] != 2, {
			~tidalRecTemp[~tidalRecTempPos] = 1;
			if (~tidalRecTemp[~tidalRecTempPos-1%3] != 2, {~tidalRecTemp[~tidalRecTempPos-1%3] = 0});
			if (~tidalRecTemp[~tidalRecTempPos+1%3] != 2, {~tidalRecTemp[~tidalRecTempPos+1%3] = 0;});
		})
	});

	// ~tidalRecTemp.postln;

	{
		case
		{~tidalRecTemp[0] == 0}
		{~tidalRecTempButton1.states_([["1", Color.white, Color.black]])}
		{~tidalRecTemp[0] == 1}
		{~tidalRecTempButton1.states_([["1", Color.black, Color.yellow]])}
		{~tidalRecTemp[0] == 2}
		{~tidalRecTempButton1.states_([["1", Color.white, Color.red]])};

		case
		{~tidalRecTemp[1] == 0}
		{~tidalRecTempButton2.states_([["2", Color.white, Color.black]])}
		{~tidalRecTemp[1] == 1}
		{~tidalRecTempButton2.states_([["2", Color.black, Color.yellow]])}
		{~tidalRecTemp[1] == 2}
		{~tidalRecTempButton2.states_([["2", Color.white, Color.red]])};

		case
		{~tidalRecTemp[2] == 0}
		{~tidalRecTempButton3.states_([["3", Color.white, Color.black]])}
		{~tidalRecTemp[2] == 1}
		{~tidalRecTempButton3.states_([["3", Color.black, Color.yellow]])}
		{~tidalRecTemp[2] == 2}
		{~tidalRecTempButton3.states_([["3", Color.white, Color.red]])};

		// if (~tidalRecTemp[1] == 1, {~tidalRecTempButton2.states_([["2", Color.black, Color.yellow]])}, {~tidalRecTempButton2.states_([["2", Color.white, Color.black]])} );
		// if (~tidalRecTemp[2] == 1, {~tidalRecTempButton3.states_([["3", Color.black, Color.yellow]])}, {~tidalRecTempButton3.states_([["3", Color.white, Color.black]])} );

	}.defer;*/
});


~xEnc6On.action_({ |v| if (v.value == 1, { ("Record Temp Preset" + (~tidalRecTrack+1)).postln;
	// ~tidalRecTemp[~tidalRecTempPos] = 2; ~tidalRecTemp.postln; // To do XXX
	{ ~tidalNbOfOrbits.do { |i|
		~tidalRecSeqline[~tidalRecTrack][i] = ~tidalGuiElements[i][\seqLine][\value].value;
		~tidalRecRytS[~tidalRecTrack][i] = ~tidalGuiElements[i][\rytS][\value].value;
		~tidalRecRyt[~tidalRecTrack][i] = ~tidalGuiElements[i][\ryt][\value].value;
		~tidalRecLegS[~tidalRecTrack][i] = ~tidalGuiElements[i][\legS][\value].value;
		~tidalRecLeg[~tidalRecTrack][i] = ~tidalGuiElements[i][\leg][\value].value;
		~tidalRecBufS[~tidalRecTrack][i] = ~tidalGuiElements[i][\bufS][\value].value;
		~tidalRecBuf[~tidalRecTrack][i] = ~tidalGuiElements[i][\buf][\value].value;
		~tidalRecBuf2[~tidalRecTrack][i] = ~tidalGuiElements[i][\buf2][\value].value;
		[~tidalRecTempButton1,~tidalRecTempButton2,~tidalRecTempButton3][~tidalRecTrack].valueAction_(0);
	};
	}.defer;
})
});


// Pattern Speed Multiplier for each Track
// ~patternSpeeds = [4, 8, 16, 24, 32];
// ~patternSpeeds = [0, 1, 2, 4, 8, 16, 32, 64];
// ~patternSpeeds = [0, 1, 2, 3, 4, 6, 8, 12, 16, 24, 32, 48, 64];
// ~patternSpeeds = (0..64) ! 8;
// ~patternSpeedsSize = (~patternSpeeds.size-1) ! 8;

~xHis.do { |c, i| c.action_({ |v|
	{ var linCurve;
		case
		{~tidalGuiElements[i][\rytS][\value].value == 0} {linCurve = 0}
		{~tidalGuiElements[i][\rytS][\value].value <= 3} {linCurve = -2}
		{~tidalGuiElements[i][\rytS][\value].value <= 5 } {linCurve = -4};
		~superDirtOSC.sendMsg("/SuperDirtMixer/ryt", i, (~patternSpeeds[i][~patternSpeedsSize[i]*v.value.lincurve(curve:linCurve)]));
		if (v.value == 0, {~tidalGuiElements[i][\ryt][\value].normalColor_(Color.red)}, {~tidalGuiElements[i][\ryt][\value].normalColor_(Color.blue)});
		// ~superDirtOSC.sendMsg("/SuperDirtMixer/ryt", i, (~patternSpeeds[i][~patternSpeedsSize[i]*v.value]));
		// Ligne ci-dessous à retirer au besoin pour avoir un réel mute
	/*{*/if (~tidalGuiElements[i][\mute][\value].value == 1, { ~tidalGuiElements[i][\mute][\value].valueAction_(0); ~xHisOn[i].value_(0) })}.defer;
}) };

/*~xHi1.action_({ |v|
// ("Track 1 pat speed -> "+~patternSpeeds[(v.value*~patternSpeedsSize).asInteger]).postln;
// ~tidalNetAddr.sendMsg("/ctrl", "pattern1Speed", ~patternSpeeds[(v.value*~patternSpeedsSize).asInteger]);
// if (~patternSpeedSwitch[0] == 1, {~superDirtOSC.sendMsg("/SuperDirtMixer/ryt", 0, (~patternSpeeds[0][v.value*(~patternSpeeds[0].size-1)]))},{~superDirtOSC.sendMsg("/SuperDirtMixer/ryt", 0, v.value*64)});
~superDirtOSC.sendMsg("/SuperDirtMixer/ryt", 0, (~patternSpeeds[0][~patternSpeedsSize[0]*v.value]));
{if (~tidalGuiElements[0][\mute][\value].value == 1, { ~tidalGuiElements[0][\mute][\value].valueAction_(0); ~xHi1On.value_(0) })}.defer;
});*/


// Mute for each Track
// ~tidalMute1 = 0; ~tidalMute2 = 0; ~tidalMute3 = 0; ~tidalMute4 = 0;
// Same as Pattern Speed Multiplier == 0 -> NO besoin de de demuter et d'appuyer sur le bouton, ce qui permet de configurer la nouvelle vitesse, mais donc moins rapide pour redéclencher le son imméditement en manipulant le bouton rotatif
// ~xHi1On.action_({ |v| ~tidalNetAddr.sendMsg("/ctrl", "pattern1Speed", 0); ~xHi1On.value_(1); "Track 1 Pat Speed -> 0".postln });

~xHisOn.do { |c, i| c.action_({ |v|
	// if (~tidalMute1 == 0, {~tidalNetAddr.sendMsg("/mute", 1); ~xHi1On.value_(1); ~tidalMute1 = 1; ~superDirtOSC.sendMsg("/SuperDirtMixer/mute", 0, 1); /*"Mute Track 1".postln*/}, {~tidalNetAddr.sendMsg("/unmute", 1); ~xHi1On.value_(0); ~tidalMute1 = 0; ~superDirtOSC.sendMsg("/SuperDirtMixer/mute", 0, 0); /*"Unmute Track 1".postln*/});
	// Action via OSC au dessus et via GUI ci-dessous
	{if (~tidalGuiElements[i][\mute][\value].value == 0, { ~tidalGuiElements[i][\mute][\value].valueAction_(1); ~xHisOn[i].value_(1) }, { ~tidalGuiElements[i][\mute][\value].valueAction_(0); ~xHisOn[i].value_(0) })}.defer;
}) };

/*~xHi1On.action_({ |v|
// if (~tidalMute1 == 0, {~tidalNetAddr.sendMsg("/mute", 1); ~xHi1On.value_(1); ~tidalMute1 = 1; ~superDirtOSC.sendMsg("/SuperDirtMixer/mute", 0, 1); /*"Mute Track 1".postln*/}, {~tidalNetAddr.sendMsg("/unmute", 1); ~xHi1On.value_(0); ~tidalMute1 = 0; ~superDirtOSC.sendMsg("/SuperDirtMixer/mute", 0, 0); /*"Unmute Track 1".postln*/});
// Action via OSC au dessus et via GUI ci-dessous
{if (~tidalGuiElements[0][\mute][\value].value == 0, { ~tidalGuiElements[0][\mute][\value].valueAction_(1); ~xHi1On.value_(1) }, { ~tidalGuiElements[0][\mute][\value].valueAction_(0); ~xHi1On.value_(0) })}.defer;
});*/


~xMis.do { |c, i| c.action_({ |v|
	// ("Track 1 Buf Folder -> " ++ (v.value*~arrayOfFolderNames4TidalSize[0]).asInteger + ~arrayOfFolderNames4Tidal[(v.value*~arrayOfFolderNames4TidalSize[0]).asInteger]).postln;
	// ~tidalNetAddr.sendMsg("/ctrl", "1Fol", ~arrayOfFolderNames4Tidal[(v.value*~arrayOfFolderNames4TidalSize[0]).asInteger]);
	~superDirtOSC.sendMsg("/SuperDirtMixer/buf", i, (v.value*~arrayOfFolderNames4TidalSize[i]).asInteger)
}) };

/*~xMi1.action_({ |v|
// ("Track 1 Buf Folder -> " ++ (v.value*~arrayOfFolderNames4TidalSize[0]).asInteger + ~arrayOfFolderNames4Tidal[(v.value*~arrayOfFolderNames4TidalSize[0]).asInteger]).postln;
// ~tidalNetAddr.sendMsg("/ctrl", "1Fol", ~arrayOfFolderNames4Tidal[(v.value*~arrayOfFolderNames4TidalSize[0]).asInteger]);
~superDirtOSC.sendMsg("/SuperDirtMixer/buf", 0, (v.value*~arrayOfFolderNames4TidalSize[0]).asInteger)
});*/


// Selection Buf Folder Plage for each Track

~xMi1On.action_({ |v|
	// ~arrayOfFolderNames4TidalSize[0] = 17; ~arrayOfFolderNames4TidalSize[1] = 17; ~arrayOfFolderNames4TidalSize[2] = 17; ~arrayOfFolderNames4TidalSize[3] = 17;
	{4.do { |i| ~tidalGuiElements[i][\bufS][\value].valueAction_(0/*v.value*/) }}.defer;
	~xMi1On.value_(1); ~xMi2Off.value_(1); ~xMi3Off.value_(1); ~xMi4Off.value_(1);
});

~xMi2On.action_({ |v|
	// ~arrayOfFolderNames4TidalSize[0] = 28; ~arrayOfFolderNames4TidalSize[1] = 28; ~arrayOfFolderNames4TidalSize[2] = 28; ~arrayOfFolderNames4TidalSize[3] = 28;
	{4.do { |i| ~tidalGuiElements[i][\bufS][\value].valueAction_(1/*v.value*/) }}.defer;
	~xMi1Off.value_(1); ~xMi2On.value_(1); ~xMi3Off.value_(1); ~xMi4Off.value_(1)
});

~xMi3On.action_({ |v|
	// ~arrayOfFolderNames4TidalSize[0] = 65; ~arrayOfFolderNames4TidalSize[1] = 75; ~arrayOfFolderNames4TidalSize[2] = 75; ~arrayOfFolderNames4TidalSize[3] = 75;
	{4.do { |i| ~tidalGuiElements[i][\bufS][\value].valueAction_(2/*v.value*/) }}.defer;
	~xMi1Off.value_(1); ~xMi2Off.value_(1); ~xMi3On.value_(1); ~xMi4Off.value_(1)
});

~xMi4On.action_({ |v|
	// ~arrayOfFolderNames4TidalSize[0] = 97; ~arrayOfFolderNames4TidalSize[1] = 97; ~arrayOfFolderNames4TidalSize[2] = 97; ~arrayOfFolderNames4TidalSize[3] = 97;
	{4.do { |i| if ( ~tidalGuiElements[i][\bufS][\value].value == 3, { ~tidalGuiElements[i][\bufS][\value].valueAction_(4/*v.value*/)}, {~tidalGuiElements[i][\bufS][\value].valueAction_(3/*v.value*/)} ) }}.defer;
	~xMi1Off.value_(1); ~xMi2Off.value_(1); ~xMi3Off.value_(1); ~xMi4On.value_(1)
});

// {~xMi1On.doAction}.defer; // Pas possible car la GUI n'est pas initialialisée - TO DO
~xMi1On.value_(1); ~xMi2Off.value_(1); ~xMi3Off.value_(1); ~xMi4Off.value_(1);

// Reverb SC Amount for each Track
/*~xLo1On.action_({ |v| ~superDirtOSC.sendMsg("/SuperDirtMixer/reverb", 0, 0); ~xLo1On.value_(1) });
~xLo2On.action_({ |v| ~superDirtOSC.sendMsg("/SuperDirtMixer/reverb", 1, 0); ~xLo2On.value_(1) });
~xLo3On.action_({ |v| ~superDirtOSC.sendMsg("/SuperDirtMixer/reverb", 2, 0); ~xLo3On.value_(1) });
~xLo4On.action_({ |v| ~superDirtOSC.sendMsg("/SuperDirtMixer/reverb", 3, 0); ~xLo4On.value_(1) });*/

~xloSelectKnob = 0; ~xLo1On.value_(1); ~xLo2On.value_(0); ~xLo3On.value_(0); ~xLo4On.value_(0);
~xLo1On.action_({ |v| ~xloSelectKnob = 0; ~xLo1On.value_(1); ~xLo2On.value_(0); ~xLo3On.value_(0); ~xLo4On.value_(0); });
~xLo2On.action_({ |v| ~xloSelectKnob = 1; ~xLo1On.value_(0); ~xLo2On.value_(1); ~xLo3On.value_(0); ~xLo4On.value_(0); });
~xLo3On.action_({ |v| ~xloSelectKnob = 2; ~xLo1On.value_(0); ~xLo2On.value_(0); ~xLo3On.value_(1); ~xLo4On.value_(0); });
~xLo4On.action_({ |v| ~xloSelectKnob = 3; ~xLo1On.value_(0); ~xLo2On.value_(0); ~xLo3On.value_(0); ~xLo4On.value_(1); });

~xLos.do { |c, i| c.action_({ |v|
	/*~superDirtOSC.sendMsg("/SuperDirtMixer/reverb", 0, v.value);
	if (v.value > 0, {~xLo1Off.value_(1)}, {~xLo1On.value_(1)});*/
	// {~superDirtOSC.sendMsg("/SuperDirtMixer/buf", 0, v.value * ~tidalFolderSizes[~tidalGuiElements[0][\buf][\value].value])}.defer;
	case
	{~xloSelectKnob == 0}
	{{~tidalGuiElements[i][\buf2][\element].valueAction_(v.value)}.defer}
	{~xloSelectKnob == 1}
	{{~tidalGuiElements[i][\legS][\value].valueAction_(v.value*~tidalnumDurAlgs)}.defer}
	{~xloSelectKnob == 2}
	{{~tidalGuiElements[i][\leg][\element].valueAction_(v.value)}.defer}
	{~xloSelectKnob == 3}
	// {~superDirtOSC.sendMsg("/SuperDirtMixer/reverb", i, v.value);};
	{{~tidalGuiElements[i][\reverb][\element].valueAction_(v.value)}.defer}
}) };

// Distinction pour prendre en compte le switch vers les valeurs et fonctions
~xLo3.action_({ |v|
	/*~superDirtOSC.sendMsg("/SuperDirtMixer/reverb", 0, v.value);
	if (v.value > 0, {~xLo1Off.value_(1)}, {~xLo1On.value_(1)});*/
	// {~superDirtOSC.sendMsg("/SuperDirtMixer/buf", 0, v.value * ~tidalFolderSizes[~tidalGuiElements[0][\buf][\value].value])}.defer;
	if (~tidalPreSeqSwitch == 2, {
		~tidalFXFuncBut3.(v.value)
	},{
		case
		{~xloSelectKnob == 0}
		{{~tidalGuiElements[2][\buf2][\element].valueAction_(v.value)}.defer}
		{~xloSelectKnob == 1}
		{{~tidalGuiElements[2][\legS][\value].valueAction_(v.value*~tidalnumDurAlgs)}.defer}
		{~xloSelectKnob == 2}
		{{~tidalGuiElements[2][\leg][\element].valueAction_(v.value)}.defer}
		{~xloSelectKnob == 3}
		// {~superDirtOSC.sendMsg("/SuperDirtMixer/reverb", i, v.value);};
		{{~tidalGuiElements[2][\reverb][\element].valueAction_(v.value)}.defer}
	});
});

~xLo4.action_({ |v|
	/*~superDirtOSC.sendMsg("/SuperDirtMixer/reverb", 0, v.value);
	if (v.value > 0, {~xLo1Off.value_(1)}, {~xLo1On.value_(1)});*/
	// {~superDirtOSC.sendMsg("/SuperDirtMixer/buf", 0, v.value * ~tidalFolderSizes[~tidalGuiElements[0][\buf][\value].value])}.defer;
	if (~tidalPreSeqSwitch == 2, {
		~tidalFXFuncBut4.(v.value)
	},{
		case
		{~xloSelectKnob == 0}
		{{~tidalGuiElements[3][\buf2][\element].valueAction_(v.value)}.defer}
		{~xloSelectKnob == 1}
		{{~tidalGuiElements[3][\legS][\value].valueAction_(v.value*~tidalnumDurAlgs)}.defer}
		{~xloSelectKnob == 2}
		{{~tidalGuiElements[3][\leg][\element].valueAction_(v.value)}.defer}
		{~xloSelectKnob == 3}
		// {~superDirtOSC.sendMsg("/SuperDirtMixer/reverb", i, v.value);};
		{{~tidalGuiElements[3][\reverb][\element].valueAction_(v.value)}.defer}
	});
});

/*~xLo1.action_({ |v|
/*~superDirtOSC.sendMsg("/SuperDirtMixer/reverb", 0, v.value);
if (v.value > 0, {~xLo1Off.value_(1)}, {~xLo1On.value_(1)});*/
// {~superDirtOSC.sendMsg("/SuperDirtMixer/buf", 0, v.value * ~tidalFolderSizes[~tidalGuiElements[0][\buf][\value].value])}.defer;
{~tidalGuiElements[0][\buf2][\element].valueAction_(v.value)}.defer;
});*/

//

~tidalprevRev1Value = 0.3;
~tidalprevRev2Value = 0.3;
~tidalprevRev3Value = 0.3;
~tidalprevRev4Value = 0.3;

~tidalSwitchRev1 = 0; ~xAOn.value_(0);
~xAOn.action_({ |v|
	// ~tidalNetAddr.sendMsg("/ctrl", "1Fol", "DB_A0KikCE"); ~tidalNetAddr.sendMsg("/ctrl", "2Fol", "DB_MoOKick"); "DB_A0KikCE".postln
	{
		if (~tidalSwitchRev1 == 1, {
			// ~tidalX.setReverbVariableName(\room); "Native Reverb".postln;
			~tidalprevRev1Value = ~tidalGuiElements[0][\reverb][\element].value/*.postln*/;
			~tidalGuiElements[0][\reverb][\element].valueAction_(0); ~xAOn.value_(0); ~tidalSwitchRev1 = 0;
			~xBOn.valueAction_(1); ~xCOn.valueAction_(1); ~xDOn.valueAction_(1);
		},{ // ~tidalX.setReverbVariableName(\room);
			~tidalGuiElements[0][\reverb][\element].valueAction_(~tidalprevRev1Value); ~xAOn.value_(1); ~tidalSwitchRev1 = 1;
			~xBOn.valueAction_(0); ~xCOn.valueAction_(0); ~xDOn.valueAction_(0);
			// ~tidalprevRev1Value = ~tidalGuiElements[0][\reverb][\element].value.postln;
		})
	}.defer
});

~tidalSwitchRev2 = 0; ~xBOn.value_(0);
~xBOn.action_({ |v|
	// ~tidalNetAddr.sendMsg("/ctrl", "1Fol", "DB_MoOKick"); ~tidalNetAddr.sendMsg("/ctrl", "2Fol", "DB_PulsarB"); "DB_MoOKick".postln
	{
		if (~tidalSwitchRev2 == 1, {
			~tidalprevRev2Value = ~tidalGuiElements[1][\reverb][\element].value/*.postln*/;
			~tidalGuiElements[1][\reverb][\element].valueAction_(0); ~xBOn.value_(0); ~tidalSwitchRev2 = 0;
		},{
			~tidalGuiElements[1][\reverb][\element].valueAction_(~tidalprevRev2Value); ~xBOn.value_(1); ~tidalSwitchRev2 = 1;
		})
	}.defer
});

~tidalSwitchRev3 = 0; ~xCOn.value_(0);
~xCOn.action_({ |v|
	// ~tidalNetAddr.sendMsg("/ctrl", "1Fol", "DB_PulsarB"); ~tidalNetAddr.sendMsg("/ctrl", "2Fol", "DB_TechGab"); "DB_PulsarB".postln
	{
		if (~tidalSwitchRev3 == 1, {
			~tidalprevRev3Value = ~tidalGuiElements[2][\reverb][\element].value/*.postln*/;
			~tidalGuiElements[2][\reverb][\element].valueAction_(0); ~xCOn.value_(0); ~tidalSwitchRev3 = 0;
		},{
			~tidalGuiElements[2][\reverb][\element].valueAction_(~tidalprevRev3Value); ~xCOn.value_(1); ~tidalSwitchRev3 = 1;
		})
	}.defer
});

~tidalSwitchRev4 = 0; ~xDOn.value_(0);
~xDOn.action_({ |v|
	// ~tidalNetAddr.sendMsg("/ctrl", "1Fol", "DB_TechGab"); ~tidalNetAddr.sendMsg("/ctrl", "2Fol", "DB_A0KikCE"); "DB_TechGab".postln
	{
		if (~tidalSwitchRev4 == 1, {
			~tidalprevRev4Value = ~tidalGuiElements[3][\reverb][\element].value/*.postln*/;
			~tidalGuiElements[3][\reverb][\element].valueAction_(0); ~xDOn.value_(0); ~tidalSwitchRev4 = 0;
		},{
			~tidalGuiElements[3][\reverb][\element].valueAction_(~tidalprevRev4Value); ~xDOn.value_(1); ~tidalSwitchRev4 = 1;
		})
	}.defer
});

~xEOn.action_({ |v|
	{~tidalGuiElements[0][\seqLine][\value].valueAction_(~tidalRecSeqline[~tidalRecTrack][0]);
		~tidalGuiElements[0][\rytS][\value].valueAction_(~tidalRecRytS[~tidalRecTrack][0]);
		~tidalGuiElements[0][\ryt][\value].valueAction_(~tidalRecRyt[~tidalRecTrack][0]);
		~tidalGuiElements[0][\legS][\value].valueAction_(~tidalRecLegS[~tidalRecTrack][0]);
		~tidalGuiElements[0][\leg][\value].valueAction_(~tidalRecLeg[~tidalRecTrack][0]);
		~tidalGuiElements[0][\bufS][\value].valueAction_(~tidalRecBufS[~tidalRecTrack][0]);
		~tidalGuiElements[0][\buf][\value].valueAction_(~tidalRecBuf[~tidalRecTrack][0]);
		~tidalGuiElements[0][\buf2][\value].valueAction_(~tidalRecBuf2[~tidalRecTrack][0]);}.defer;
	/*~tidalEvalAddr.sendMsg("/pulsar/eval", \type, 'line', 'tab', (4), \row, 73, \column, 1);
~tidalEvalAddr.sendMsg("/pulsar/eval", \type, 'line', 'tab', (5), \row, 73, \column, 1);
~tidalEvalAddr.sendMsg("/pulsar/eval", \type, 'line', 'tab', (6), \row, 73, \column, 1);
~tidalEvalAddr.sendMsg("/pulsar/eval", \type, 'line', 'tab', (7), \row, 73, \column, 1); [73, 73, 73, 73].postln*/
});

/*~xEOn.action_({ |v| ~xEOn.value_(~tidalSwitchRev1);
	{~xAOn.valueAction_(~tidalSwitchRev1); ~xBOn.valueAction_(~tidalSwitchRev2); ~xCOn.valueAction_(~tidalSwitchRev2); ~xDOn.valueAction_(~tidalSwitchRev4)}.defer;
});*/

~xFOn.action_({ |v|
	{~tidalGuiElements[1][\seqLine][\value].valueAction_(~tidalRecSeqline[~tidalRecTrack][1]);
		~tidalGuiElements[1][\rytS][\value].valueAction_(~tidalRecRytS[~tidalRecTrack][1]);
		~tidalGuiElements[1][\ryt][\value].valueAction_(~tidalRecRyt[~tidalRecTrack][1]);
		~tidalGuiElements[1][\legS][\value].valueAction_(~tidalRecLegS[~tidalRecTrack][1]);
		~tidalGuiElements[1][\leg][\value].valueAction_(~tidalRecLeg[~tidalRecTrack][1]);
		~tidalGuiElements[1][\bufS][\value].valueAction_(~tidalRecBufS[~tidalRecTrack][1]);
		~tidalGuiElements[1][\buf][\value].valueAction_(~tidalRecBuf[~tidalRecTrack][1]);
		~tidalGuiElements[1][\buf2][\value].valueAction_(~tidalRecBuf2[~tidalRecTrack][1]);}.defer;
	/*~tidalEvalAddr.sendMsg("/pulsar/eval", \type, 'line', 'tab', (4), \row, 92, \column, 1);
~tidalEvalAddr.sendMsg("/pulsar/eval", \type, 'line', 'tab', (5), \row, 117, \column, 1);
~tidalEvalAddr.sendMsg("/pulsar/eval", \type, 'line', 'tab', (6), \row, 81, \column, 1); [92, 117, 81].postln*/
});

~xGOn.action_({ |v|
	{~tidalGuiElements[2][\seqLine][\value].valueAction_(~tidalRecSeqline[~tidalRecTrack][2]);
		~tidalGuiElements[2][\rytS][\value].valueAction_(~tidalRecRytS[~tidalRecTrack][2]);
		~tidalGuiElements[2][\ryt][\value].valueAction_(~tidalRecRyt[~tidalRecTrack][2]);
		~tidalGuiElements[2][\legS][\value].valueAction_(~tidalRecLegS[~tidalRecTrack][2]);
		~tidalGuiElements[2][\leg][\value].valueAction_(~tidalRecLeg[~tidalRecTrack][2]);
		~tidalGuiElements[2][\bufS][\value].valueAction_(~tidalRecBufS[~tidalRecTrack][2]);
		~tidalGuiElements[2][\buf][\value].valueAction_(~tidalRecBuf[~tidalRecTrack][2]);
		~tidalGuiElements[2][\buf2][\value].valueAction_(~tidalRecBuf2[~tidalRecTrack][2]);}.defer;
	/*~tidalEvalAddr.sendMsg("/pulsar/eval", \type, 'line', 'tab', (4), \row, 93, \column, 1);
~tidalEvalAddr.sendMsg("/pulsar/eval", \type, 'line', 'tab', (5), \row, 102, \column, 1);
~tidalEvalAddr.sendMsg("/pulsar/eval", \type, 'line', 'tab', (6), \row, 81, \column, 1); [93, 102, 81].postln*/
});

~xHOn.action_({ |v|
	{~tidalGuiElements[3][\seqLine][\value].valueAction_(~tidalRecSeqline[~tidalRecTrack][3]);
		~tidalGuiElements[3][\rytS][\value].valueAction_(~tidalRecRytS[~tidalRecTrack][3]);
		~tidalGuiElements[3][\ryt][\value].valueAction_(~tidalRecRyt[~tidalRecTrack][3]);
		~tidalGuiElements[3][\legS][\value].valueAction_(~tidalRecLegS[~tidalRecTrack][3]);
		~tidalGuiElements[3][\leg][\value].valueAction_(~tidalRecLeg[~tidalRecTrack][3]);
		~tidalGuiElements[3][\bufS][\value].valueAction_(~tidalRecBufS[~tidalRecTrack][3]);
		~tidalGuiElements[3][\buf][\value].valueAction_(~tidalRecBuf[~tidalRecTrack][3]);
		~tidalGuiElements[3][\buf2][\value].valueAction_(~tidalRecBuf2[~tidalRecTrack][3]);}.defer;
	/*~tidalEvalAddr.sendMsg("/pulsar/eval", \type, 'line', 'tab', (4), \row, 102, \column, 1);
~tidalEvalAddr.sendMsg("/pulsar/eval", \type, 'line', 'tab', (5), \row, 117, \column, 1);
~tidalEvalAddr.sendMsg("/pulsar/eval", \type, 'line', 'tab', (6), \row, 81, \column, 1); [102, 117, 81].postln*/
});


// Encodeur du haut - Switch Sequence / Preset
~tidalPreSeqSwitch = 1; ~xIOn.value_(1); // Initialisation sur les Presets at StartUp
~xIOn.action_({ |v|
	if (~tidalPreSeqSwitch == 0, { ~tidalPreSeqSwitch = 1; ~xIOn.value_(1) }, { ~tidalPreSeqSwitch = 0; ~xIOn.value_(0) });
	/*~tidalEvalAddr.sendMsg("/pulsar/eval", \type, 'line', 'tab', (4), \row, 103, \column, 1);
	~tidalEvalAddr.sendMsg("/pulsar/eval", \type, 'line', 'tab', (5), \row, 117, \column, 1);
	~tidalEvalAddr.sendMsg("/pulsar/eval", \type, 'line', 'tab', (6), \row, 81, \column, 1); [103, 117, 81].postln*/
});

~tidalFuncSwitch = 0; ~xJOn.value_(0);
~xJOn.action_({ |v|
	if (~tidalFuncSwitch == 0, { ~tidalFuncSwitch = 1; ~xJOn.value_(1); }, { ~tidalFuncSwitch = 0; ~xJOn.value_(0); });
	if (~tidalFuncSwitch ==  1,
		{
			~tidalPreSeqSwitch = 2;
		},{
			if (~xIOn.value == 1, { ~tidalPreSeqSwitch = 1; }, { ~tidalPreSeqSwitch = 0; });
	});
	// if (~tidalPreSeqSwitch == 0, { ~tidalPreSeqSwitch = 1; ~xJOn.value_(1) }, { ~tidalPreSeqSwitch = 0; ~xJOn.value_(0) });
    /*~tidalEvalAddr.sendMsg("/pulsar/eval", \type, 'line', 'tab', (4), \row, 117, \column, 1);
	~tidalEvalAddr.sendMsg("/pulsar/eval", \type, 'line', 'tab', (5), \row, 120, \column, 1);
	~tidalEvalAddr.sendMsg("/pulsar/eval", \type, 'line', 'tab', (6), \row, 81, \column, 1); [118, 120, 81].postln*/
});

~xKOn.value_(0);
~xKOn.action_({ |v|
	{if (~tidalGuiElements[0][\legS][\value].value != 6, { ~tidalNbOfOrbits.do { |i| ~tidalGuiElements[i/*orbit.orbitIndex*/][\legS][\value].valueAction_(6)}; ~xKOn.value_(1); ~xLOn.value_(0)}, { ~tidalNbOfOrbits.do { |i| ~tidalGuiElements[i/*orbit.orbitIndex*/][\legS][\value].valueAction_(10)}; ~xKOn.value_(1); ~xLOn.value_(0)} )}.defer;
});

~xLOn.value_(1);
~xLOn.action_({ |v|
	// ~tidalEvalAddr.sendMsg("/pulsar/eval", \type, 'line', 'tab', (4), \row, 121, \column, 1); ~tidalEvalAddr.sendMsg("/pulsar/eval", \type, 'line', 'tab', (5), \row, 117, \column, 1); ~tidalEvalAddr.sendMsg("/pulsar/eval", \type, 'line', 'tab', (6), \row, 81, \column, 1); [93, 117, 81].postln
	{if (~tidalGuiElements[0][\legS][\value].value != 1, { ~tidalNbOfOrbits.do { |i| ~tidalGuiElements[i/*orbit.orbitIndex*/][\legS][\value].valueAction_(1)}; ~xLOn.value_(1); ~xKOn.value_(0)}, { ~tidalNbOfOrbits.do { |i| ~tidalGuiElements[i/*orbit.orbitIndex*/][\legS][\value].valueAction_(0)}; ~xLOn.value_(0); ~xKOn.value_(0)} )}.defer;
});

// Mute All Tracks
~xMOn.action_({ |v|
	{if (~tidalMuteAllButton.value == 0, {~tidalMuteAllButton.valueAction_(1); ~xHi1On.value_(1); ~xHi2On.value_(1); ~xHi3On.value_(1); ~xHi4On.value_(1); ~xMOn.value_(1)},{~tidalMuteAllButton.valueAction_(0); ~xHi1On.value_(0); ~xHi2On.value_(0); ~xHi3On.value_(0); ~xHi4On.value_(0); ~xMOn.value_(0)})}.defer;
	// if (~tidalMute1 == 0, {~tidalNetAddr.sendMsg("/muteAll"); ~xHi1On.value_(1); ~xHi2On.value_(1); ~xHi3On.value_(1); ~xHi4On.value_(1); ~xMOn.value_(1); ~tidalMute1 = 1; ~tidalMute2 = 1; ~tidalMute3 = 1; ~tidalMute4 = 1; 4.do { |i|~superDirtOSC.sendMsg("/SuperDirtMixer/mute", i, 1) }; /*"Mute All Tracks".postln*/}, {~tidalNetAddr.sendMsg("/unmuteAll"); ~xHi1On.value_(0); ~xHi2On.value_(0); ~xHi3On.value_(0); ~xHi4On.value_(0); ~xMOn.value_(0); ~tidalMute1 = 0; ~tidalMute2 = 0; ~tidalMute3 = 0; ~tidalMute4 = 0; 4.do { |i| ~superDirtOSC.sendMsg("/SuperDirtMixer/mute", i, 0) } /*"Unmute All Tracks".postln*/});
});

~tidalFxFuncSwitch = 0; ~xNOn.value_(0);
~xNOn.action_({ |v|
	if (~tidalFxFuncSwitch == 0, { ~tidalFxFuncSwitch = 1; ~xNOn.value_(1) }, { ~tidalFxFuncSwitch = 0; ~xNOn.value_(0) });
	// "n".postln
});

~tidalFxFuncNb = 0; ~xOOn.value_(0);
~xOOn.action_({ |v|
	if (~tidalFxFuncNb == 0, { ~tidalFxFuncNb = 1; ~xOOn.value_(1) }, { ~tidalFxFuncNb = 0; ~xOOn.value_(0) });
	// "o".postln
});

// Quantize
~xPOn.value_(0);
~xPOn.action_({ |v|
	{
		if (~tidalGuiElements[0/*orbit.orbitIndex*/][\rytS][\value].value == 0, {
		~tidalNbOfOrbits.do { |i| ~tidalGuiElements[i/*orbit.orbitIndex*/][\rytS][\value].valueAction_(1)}; ~xPOn.value_(1)
		},{
			~tidalNbOfOrbits.do { |i| ~tidalGuiElements[i/*orbit.orbitIndex*/][\rytS][\value].valueAction_(0)}; ~xPOn.value_(0)
		});
	}.defer;

/*case
		{~tidalGuiElements[0/*orbit.orbitIndex*/][\rytS][\value].value == 0}
		{ ~tidalNbOfOrbits.do { |i| ~tidalGuiElements[i/*orbit.orbitIndex*/][\rytS][\value].valueAction_(1)}; ~xPOn.value_(1) }
		{~tidalGuiElements[0/*orbit.orbitIndex*/][\rytS][\value].value == 1}
		{ ~tidalNbOfOrbits.do { |i| ~tidalGuiElements[i/*orbit.orbitIndex*/][\rytS][\value].valueAction_(2)}; ~xPOn.value_(1) }
		{~tidalGuiElements[0/*orbit.orbitIndex*/][\rytS][\value].value == 2}
		{ ~tidalNbOfOrbits.do { |i| ~tidalGuiElements[i/*orbit.orbitIndex*/][\rytS][\value].valueAction_(0)}; ~xPOn.value_(0) }
	}.defer;*/

	// if (~patternSpeedSwitch[0] == 0, {~patternSpeeds[0] = [0, 1, 2, 3, 4, 6, 8, 12, 16, 24, 32, 48, 64]; ~xPOn.value_(1); "Pattern Speed Quantize ON".postln; ~patternSpeedSwitch[0] = 1 }, {~patternSpeeds[0] = (0..64); ~xPOn.value_(0); "Pattern Speed Quantize OFF".postln;  ~patternSpeedSwitch[0] = 0 }); ~patternSpeedsSize[0] = ~patternSpeeds[0].size-1;
});

// Volume for each Track
~xSli1 = ~xController.elementAt(\Sli1);
// ~xSli1.action_({ |v| ~superDirtOSC.sendMsg("/SuperDirtMixer/masterGain", 0, v.value*2); /*v.value.postln;*/ });
~xSli1.action_({ |v| {~tidalGuiElements[0][\masterGain][\element].valueAction_(v.value)}.defer; /*v.value.postln;*/ });
~xSli2 = ~xController.elementAt(\Sli2);
// ~xSli2.action_({ |v| ~superDirtOSC.sendMsg("/SuperDirtMixer/masterGain", 1, v.value*2); });
~xSli2.action_({ |v| {~tidalGuiElements[1][\masterGain][\element].valueAction_(v.value)}.defer });
~xSli3 = ~xController.elementAt(\Sli3);
// ~xSli3.action_({ |v| ~superDirtOSC.sendMsg("/SuperDirtMixer/masterGain", 2, v.value*2); });
~xSli3.action_({ |v| {~tidalGuiElements[2][\masterGain][\element].valueAction_(v.value)}.defer });
~xSli4 = ~xController.elementAt(\Sli4);
// ~xSli4.action_({ |v| ~superDirtOSC.sendMsg("/SuperDirtMixer/masterGain", 3, v.value*2); });
~xSli4.action_({ |v| {~tidalGuiElements[3][\masterGain][\element].valueAction_(v.value)}.defer });


~tidalMute2Switch = 0;
~xExitOn = ~xController.elementAt(\ExitOn); ~xExitOn.action_({ |v|
// 4.do { |tr| ~dirt.orbits[tr].freeSynths /*.release(0)*//*cmdPeriod*//*initNodeTree*//*freeSynths*/ }; "Free Synths".postln;
	// {~tidalMute2AllButton.valueAction_(1);}.defer; "Fade Out Synths".postln;
	if (~tidalMute2Switch == 0, {
		if (~tidalEvalTabPaneSwitch == 1, {
			~tidalEvalAddr.sendMsg("/pulsar/eval", \type, 'line', \pane, 0, 'tab', 0 /*%4*/, \row, 502 /*a.value*2*/, \column, 1);
		},{
			~tidalEvalAddr.sendMsg("/pulsar/eval", \type, 'line', 'tab', 0 /*%4*/, \row, 502 /*a.value*2*/, \column, 1);
		});
		~tidalNbOfOrbits.do { |i| ~dirt.orbits[i].amp = 0; };
		~tidalMute2Switch = 1; {~tidalMute2AllButton.valueAction_(1);}.defer;
		~xExitOn.value_(1); "Cut Amps - Mute".postln;
		if (~tidalSwitchRev1 == 1, {~xEOn.valueAction_(~tidalSwitchRev1)});
	},{
		if (~tidalEvalTabPaneSwitch == 1, {
			~tidalEvalAddr.sendMsg("/pulsar/eval", \type, 'line', \pane, 0, 'tab', 0 /*%4*/, \row, 503 /*a.value*2*/, \column, 1);
		},{
			~tidalEvalAddr.sendMsg("/pulsar/eval", \type, 'line', 'tab', 0 /*%4*/, \row, 503 /*a.value*2*/, \column, 1);
		});
		~tidalNbOfOrbits.do { |i| ~dirt.orbits[i].amp = 0.4; };
		~tidalMute2Switch = 0; {~tidalMute2AllButton.valueAction_(0);}.defer;
		~xExitOn.value_(0); "Trigger Synths".postln; // ~xEOn.valueAction_(~tidalSwitchRev1);
		if (~tidalSwitchRev1 == 0, {~xEOn.valueAction_(~tidalSwitchRev1)});
	});
});

// 4.do { |tr| ~dirt.orbits[tr].initNoteTree };
// ~dirt.orbits[0].group.free
// g = ~dirt.orbits[0].group.class.postln
// g.queryTree

/*
~xMi1OnO = ~xController.elementAt(\Mi1OnO);
~xMi1OnG = ~xController.elementAt(\Mi1OnG);

~xMi1On.value_(1);
~xMi1OnO.value_(1);
~xMi1OnG.value_(1);
*/
