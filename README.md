# Welcome to ***Live 4 Bubbles !*** &nbsp;&nbsp; <span class="badge-licence"><a href="https://creativecommons.org/licenses/by-nc-sa/4.0/" title="Licence"><img src="https://licensebuttons.net/l/by-nc-sa/3.0/88x31.png" alt="Licence link" /></a></span> &nbsp;&nbsp; <span class="badge-buymeacoffee"><a href="https://ko-fi.com/Live4Life" title="Donate to this project using Buy Me A Coffee"><img src="https://img.shields.io/badge/buy%20me%20a%20coffee-donate-yellow.svg" alt="Buy Me A Coffee donate button" /></a></span> &nbsp;&nbsp; <span class="badge-patreon"><a href="https://patreon.com/Live4Life" title="Donate to this project using Patreon"><img src="https://img.shields.io/badge/patreon-donate-yellow.svg" alt="Patreon donate button" /></a></span>

<p align="center">
<img src="Images/live4bubbles_overview.gif" />
</p>

<div align="center">

| [**Overview**](#-overview) | [**Installation**](#-installation) | [**Setup**](#%EF%B8%8F-setup) | [**Documentation**](#-Documentation) | [**References**](#-references) | [**Contribute**](#-contribute) | [**Acknowledgements**](#-acknowledgements) | [**Licence**](#-licence) |

</div>

---

# 📖 Overview

---

## 🫧 What is *Live 4 Bubbles* ?

**A performance system for controlling Live Coding Databases from [Tidal Cycles](https://tidalcycles.org/) and [Hydra](https://hydra.ojack.xyz/) in the [Pulsar](https://pulsar-edit.dev/) text editor with [SuperCollider](https://supercollider.github.io/) GUI and controllers.**

*This is my current main life-long project on which I dedicate myself to develop further and perform with this audiovisual system, for as many years as possible until the end of my life, hopefully!*

## 🔮 Vision

*Live 4 Bubbles* is partly an extension and modified version based on [SuperDirtMixer](https://github.com/thgrund/SuperDirtMixer) from Thomas Grund. But it goes far beyond a mixer for Tidal Cycles. It transforms your live coding setup into a **performance system with control over your rhythmic, sonic and spatial patterns** of sound files and SuperCollider synthesis in real-time.

⚠️ While *Live 4 Bubbles* can be launched quickly once installed, the installation process involves editing some configuration files and setting up numerous open-source dependencies and programs to maximize control possibilities over sound synthesis and pattern manipulation.

As shown in the images above representing a four-track configuration, this project is also designed to be used in conjunction with [Fizzy Hydra](https://github.com/Xon77/FizzyHydra) if you want to add audio-reactive visuals and a VJ system controlled notably by volume data and event triggering per track.

## ✨ Core Features

### 🎚️ Multi-Track Control System

- 🎹 **Dynamic code execution control** - Up to **8 independent tracks** with dozens of parameters each,
- 🎶 **Trigger and control Tidal Cycles patterns** - From SuperCollider GUI and MIDI controllers to switch between different code patterns on the fly,
- 💾 **Preset system** - Instant performance setup and transitions with smooth crossfade between patterns,
- 🎲 **Randomization system** - Randomize each parameter type for creative exploration.

### 🎼 Parametric Control of your Tidal Cycles code line / pattern

- ⏱️ **Timing** - Speed, duration, and legato,
- 📁 **Sound Selection** - Quick navigation through sound libraries with folder and buffer selection,
- 🎨 **Effects Database** - Filter and effects to both sound and patterns with multiple parameter controls,
- 🌐 **Spatial Control** - Channel Positioning and orbit management for immersive soundscapes.

---

# 💿 Installation

---

## 📋 Prerequisites

Before installing *Live 4 Bubbles*, ensure you have the following installed:

- **[SuperCollider](https://supercollider.github.io/)** (tested on 3.14) - Algorithmic & Audio synthesis platform,
- **[SuperDirtMixer Quark](https://github.com/thgrund/SuperDirtMixer)** - *Live 4 Bubbles* is built on top of SuperDirtMixer. Follow the instructions to install all [its requirements](https://github.com/thgrund/SuperDirtMixer?tab=readme-ov-file#requirements), including **[SuperDirt Quark](https://github.com/musikinformatik/SuperDirt)** (SuperCollider synthesizer for Tidal),
- **[Tidal Cycles](https://tidalcycles.org/)** (tested on 1.9.5 & ghc 9.6.7) - Live coding pattern language,
- **[Pulsar](https://pulsar-edit.dev/)** - Text editor for live coding,
- **[pulsar-tidalcycles](https://web.pulsar-edit.dev/packages/tidalcycles)** - Pulsar package for Tidal Cycles integration (You can use my variation of [pulsar-tidalcycles](https://github.com/Xon77/pulsar-tidalcycles) which adds the ability to select lines from different splits, in addition to the original feature of selecting from different tabs. You can also find or add the code changed via [this link](https://github.com/tidalcycles/pulsar-tidalcycles/compare/master...Xon77:pulsar-tidalcycles:master)),
- [SuperCollider sc3-plugins](https://supercollider.github.io/sc3-plugins) (Optional) for extended synthesis capabilities.

## 📥 Step-by-Step Installation

### 1️⃣ Download *Live 4 Bubbles*

Download the *Live 4 Bubbles* repository to your local machine. You can also clone with GIT commands in your terminal:

```bash
git clone https://github.com/Xon77/Live4Bubbles.git
```

### 2️⃣ Install *Live 4 Bubbles* in SuperCollider Extensions

Copy the [`L4B_SC/SuperDirtMixerX/`](L4B_SC/SuperDirtMixerX/) folder into your SuperCollider extensions folder:

- **macOS**: `~/Library/Application Support/SuperCollider/Extensions/`,
- **Linux**: `~/.local/share/SuperCollider/Extensions/`,
- **Windows**: `%USERPROFILE%\AppData\Local\SuperCollider\Extensions\`.

### 3️⃣ Setup your Sound Database

Your sound folder must be structured following the [Live 4 Life sound database structure](https://github.com/Xon77/L4LSoundsDataBase/tree/main/SoundFolder), except that folder names should use underscores instead of spaces (e.g., `DB_MBass` instead of `DB MBass`).

Example structure:
```
YourSoundFolder/
├── DB_Kick/
│   ├── sound1.wav
│   └── sound2.wav
├── DB_Snare/
├── DB_Bass/
└── ...
```


### 4️⃣ Install SuperCollider Quarks (extensions) for some MIDI Controllers (Optional)

If you plan to use the MIDI controllers mentioned below, install the required Quarks in SuperCollider:

#### 🎚️ For [Xone K2 or K3](https://www.allen-heath.com/hardware/xone-series/xonek3/):
- Install the [`Modality-toolkit`](https://github.com/ModalityTeam/Modality-toolkit) Quark, by evaluating in SuperCollider:
```supercollider
Quarks.install("Modality-toolkit");
```

- Copy the [`xoneK2.desc.scd`](L4B_SC/xoneK2.desc.scd) file into the `MKtlDescriptions` folder within the Modality Quark directory:
  - **macOS**: `~/Library/Application Support/SuperCollider/downloaded-quarks/Modality-toolkit/Modality/MKtlDescriptions/`,
  - **Linux**: `~/.local/share/SuperCollider/downloaded-quarks/Modality-toolkit/Modality/MKtlDescriptions/`,
  - **Windows**: `%USERPROFILE%\AppData\Local\SuperCollider\downloaded-quarks\Modality-toolkit\Modality\MKtlDescriptions\`.

#### 🎛️ For [MIDI Fighter Twister](https://www.midifighter.com/#Twister):
- Install the [`Twister`](https://github.com/scztt/Twister.quark) Quark and the [`Connection`](https://github.com/scztt/Connection.quark) Quark, by evaluating in SuperCollider:
```supercollider
Quarks.install("Twister");
Quarks.install("Connection");
```

You can connect up to 3 MIDI Fighters together. Name them: "MIDI Fighter Twister 1", "MIDI Fighter Twister 2", "MIDI Fighter Twister 3" in MIDI configuration.

If you want to edit the controller configuration files, view them, or copy and adapt them to other controllers, the files are:
- [`_Init Midi X2.scd`](L4B_SC/L4B_SC_Project/_Init%20Midi%20X2.scd) for [Xone K2 or K3](https://www.allen-heath.com/hardware/xone-series/xonek3/) from Allen & Heath,
- [`_Init Midi T2.scd`](L4B_SC/L4B_SC_Project/_Init%20Midi%20T2.scd) for [MIDI Fighter Twister](https://www.midifighter.com/#Twister),
- [`_Init Midi LPX.scd`](L4B_SC/L4B_SC_Project/_Init%20Midi%20LPX.scd) (in development) for [Launchpad X](https://novationmusic.com/products/launchpad-x) from Novation.

### 5️⃣ Pulsar Styling (Optional)

For a customized Pulsar appearance optimized for *Live 4 Bubbles*:

1. Navigate to folder [`L4B_Pulsar/`](L4B_Pulsar/),
2. Copy the styles from [`styles.less`](L4B_Pulsar/styles.less),
3. Paste them into your Pulsar stylesheet (Edit → Stylesheet).

---

# ⚙️ Setup

---

## 🚀 Quick Start

### 1️⃣ Start SuperCollider

Open SuperCollider and evaluate the initialization file:
- Navigate to folder [`L4B_SC/L4B_SC_Project/`](L4B_SC/L4B_SC_Project/),
- Open SC initialization file [`_0T_Init_TidalX.scd`](L4B_SC/L4B_SC_Project/_0T_Init_TidalX.scd),
- Edit the SC initialization file in [`L4B_SC/L4B_SC_Project/`](L4B_SC/L4B_SC_Project/) to match your system paths:
	- Update the sound folder path to point to your sound database,
	- Update any other paths as needed for your setup.
- Evaluate the section in parentheses between [lines 16 and 422](L4B_SC/L4B_SC_Project/_0T_Init_TidalX.scd#L16-L422) within the file (Cmd+Enter on macOS, Ctrl+Enter on Windows/Linux).

This will:
- Boot the SuperCollider server,
- Load SuperDirt and *Live 4 Bubbles*,
- Initialize the GUI & OSC,
- Setup all MIDI connections (if available).

### 2️⃣ Launch Pulsar

Create a four-track code execution partition by opening all Tidal Cycles files from the [`L4B_Tidal/`](L4B_Tidal/) folder in Pulsar:

1. [`L4B_Tidal/DJCode1.tidal`](L4B_Tidal/DJCode1.tidal) in tab 1,
2. [`L4B_Tidal/DJCode2.tidal`](L4B_Tidal/DJCode2.tidal) in tab 2,
3. [`L4B_Tidal/DJCode3.tidal`](L4B_Tidal/DJCode3.tidal) in tab 3,
4. [`L4B_Tidal/DJCode4.tidal`](L4B_Tidal/DJCode4.tidal) in tab 4,
5. [`L4B_Tidal/DJCodeG.tidal`](L4B_Tidal/DJCodeG.tidal) in tab 5 (for global control),
6. [`L4B_Tidal/ControlTidal.tidal`](L4B_Tidal/ControlTidal.tidal) in tab 6 (for initialization).

Other code execution workflows are possible. For example, you can execute code lines by selecting different splits and tabs. *(The images above represent four splits of the first tab.)*

📌 **Note:** This step is only necessary the first time. Once you save your Pulsar workspace, you can simply reopen Pulsar with the saved tab layout for subsequent sessions.

### 3️⃣ Initialize Control Variables in Tidal Cycles

In [`ControlTidal.tidal`](L4B_Tidal/ControlTidal.tidal), evaluate the following sections in order with Ctrl+Enter (Cmd+Enter on macOS):

1. [Lines 5 to 1107](L4B_Tidal/ControlTidal.tidal#L5-L1107) - Main control definitions and functions,
2. [Line 1110](L4B_Tidal/ControlTidal.tidal#L1110) - Initialization of controls and variables,
3. [Line 1113](L4B_Tidal/ControlTidal.tidal#L1113) - Update of definitions and functions of step 1,
4. [Lines 1118 to 1136](L4B_Tidal/ControlTidal.tidal#L1118-L1136) - Final initialization for crossfades.

This will boot Tidal Cycles and set up all control parameters for tempo, duration, sound folders, buffers, effects, and more. Once your project is configured, booting *Live 4 Bubbles* is simple: evaluate one code section in SuperCollider and four code sections in Pulsar.

🎉✨🎵 **You can begin to play with the GUI & controllers!** 🎵✨🎉

---

# 📖 Documentation

---

## 🎛️ Control Parameters

In addition to SuperDirtMixer controls (on volume, reverb, EQ, compression), each of the 8 tracks (tabs or splits) has access to the following 31 parameters:

### 🎵 Pattern structure
- 🔴 Track preset number (saves and recalls all parameters below),
- 🔵 Code line / pattern (of the tab in Pulsar),

### ⏱️ Timing
- 🟠 `d1` to `d8` - Tempo (speed control),
- 🟤 `1Leg` to `8Leg` - Duration (length variable),
- 🟤 `1LegS` to `8LegS` - Duration algorithm switch,
- ⚫ `1Xon` to `8Xon` - Orbit assignment.

### 〰️ Sound Selection
- 🔴 `1Fol` to `8Fol` - Sound Folder (directory containing audio samples) or Synthesis Type (with th B/S switch),
- 🟪 `1Buf` to `8Buf` - Sound File (within the sound folder),
- 🔴 `1FolN` to `8FolN` - Normalized folder index (0-1),
- 🟪 `1BufN` to `8BufN` - Normalized file index (0-1).

### ▶️ Playback
- 🟢 `1Rat` to `8Rat` - Playback rate/speed,
- ⚪ `1Ran` to `8Ran` - Context/line-dependent parameter ([djf filter](https://tidalcycles.org/docs/reference/audio_effects/#dj-filter) by default).

### 🎨 Sound Effects (2 chains per track)

- 🟢 `1fxs` to `8fxs` / `1fxs2` to `8fxs2` - **When** (timing algorithm type),
- 🟢 `1fxt` to `8fxt` / `1fxt2` to `8fxt2` - **When value** (value of the When algorithm),
- 🟢 `1fxx` to `8fxx` / `1fxx2` to `8fxx2` - **How** (superposition or not, with delays of various durations),
- 🟢 `1fxp` to `8fxp` / `1fxp2` to `8fxp2` - **What** (specific effect transformation),
- 🟢 `1fxv` to `8fxv` / `1fxv2` to `8fxv2` - **How much or What value** (value of the What algorithm).

### 🔄 Pattern Functions (2 chains per track)

- 🔵 `1fus` to `8fus` / `1fus2` to `8fus2` - **When** (timing algorithm type),
- 🔵 `1fut` to `8fut` / `1fut2` to `8fut2` - **When value** (value of the When algorithm),
- 🔵 `1fux` to `8fux` / `1fux2` to `8fux2` - **How** (superposition or not, with delays of various durations),
- 🔵 `1fup` to `8fup` / `1fup2` to `8fup2` - **What** (transformations: degradeBy, trunc, etc.),
- 🔵 `1fuv` to `8fuv` / `1fuv2` to `8fuv2` - **How much or What value** (value of the What algorithm).

### 🌐 Spatial
- 🟡 `1Spa` to `8Spa` - Spatial Channel positioning.

---

**See the [wiki](https://github.com/Xon77/Live4Bubbles/wiki) for more details on documentation** (under construction and constant updates).

**Additional documentation will be added in the coming weeks and months.**

---

# 📚 References

---

This project, briefly introduced during **Composite Montréal** in June 2025 through a 5-minute pitch, has been presented on **Saturday, October 18, 2025** as part of a three-hour public experimental workshop at [Eastern Bloc](https://www.easternbloc.ca), which included a short performance and a participatory exploration session with the audience. I warmly thank [Eastern Bloc](https://www.easternbloc.ca) and [CALQ](https://www.calq.gouv.qc.ca/en/) for their support in making this presentation possible.

---

# 🤝 Contribute &nbsp;&nbsp; [![ko-fi](https://ko-fi.com/img/githubbutton_sm.svg)](https://ko-fi.com/Live4Life) &nbsp;&nbsp; <a href="https://www.patreon.com/Live4Life"><img src="https://c5.patreon.com/external/logo/become_a_patron_button@2x.png" width="160"></a>

---

**Want to contribute?** Please get in touch with me to organize further development.

**Support this project through donations via [Ko-fi](https://ko-fi.com/live4life) (one-time) or [Patreon](https://www.patreon.com/live4life) (ongoing support and courses).**

---

# 🙏 Acknowledgements

---

As [mentioned in my previous project](https://github.com/Xon77/Live4Life?tab=readme-ov-file#acknowledgements) ***Live 4 Life***, I would have been unable to build this tool without the help of the **SuperCollider online and live coding community**, who always answered my questions and even provided me with examples of codes and classes.

### 🌟 Special Thanks

**Thomas Grund** - Without whom this project would not have come to life:
- For creating the [SuperDirtMixer](https://github.com/thgrund/SuperDirtMixer) Quark,
- For implementing the feature allowing the evaluation of Tidal code lines in Atom/Pulsar, which I had suggested on [GitHub](https://github.com/tidalcycles/pulsar-tidalcycles/issues/119).

---

This project was supported by the [**Conseil des arts et des lettres du Québec (CALQ)**](https://www.calq.gouv.qc.ca/en/).

<p align="center">
<a href="https://www.calq.gouv.qc.ca/en/">
<img src="Images/Calq_noir.jpg" alt="CALQ - Conseil des arts et des lettres du Québec" width="400">
</a>
</p>

---

# 📄 Licence

---

© 2025 - End of the world ∞ **Christophe Lengelé**

***Live 4 Bubbles*** is open source software: you can redistribute it and/or modify it under the terms of the **Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International license** (CC BY-NC-SA 4.0).

### ⚠️ Usage Restrictions

**Commercial Use:** You may not use this tool for commercial purposes.

**Free Party Spirit:** I wish it would be used in the spirit of [*Free Party*](https://en.wikipedia.org/wiki/Free_party). Unfortunately, *Free* does not mean free in this commercial world, but invites to contribute to the costs and labor according to one's ability to give. **I do not want this tool to be used, by any means, for personal profit.**

**Specific Restriction:** I do not want this tool to be used by or in [Société des Arts Technologiques](https://sat.qc.ca) without my consent, since this organization never helped me in diffusing my spatial creations in their dome despite my previous proposals. *(This restriction will be withdrawn only if I have the opportunity to play improvisations in their dome with an appropriate allowance. However, it will surely never happen, since I am no longer interested in sending them applications and I am looking for more open spaces. But, life is unpredictable.)*

### 📋 Disclaimer

This program is distributed in the hope that it will be useful, but **WITHOUT ANY WARRANTY**.

---

See the [License](/LICENCE.md) for more details.