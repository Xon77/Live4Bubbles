# Bienvenue sur ***Live 4 Bubbles !*** &nbsp;&nbsp; <span class="badge-licence"><a href="https://creativecommons.org/licenses/by-nc-sa/4.0/" title="Licence"><img src="https://licensebuttons.net/l/by-nc-sa/3.0/88x31.png" alt="Lien licence" /></a></span> &nbsp;&nbsp; <span class="badge-buymeacoffee"><a href="https://ko-fi.com/Live4Life" title="Faire un don via Buy Me A Coffee"><img src="https://img.shields.io/badge/buy%20me%20a%20coffee-donate-yellow.svg" alt="Bouton don Buy Me A Coffee" /></a></span> &nbsp;&nbsp; <span class="badge-patreon"><a href="https://patreon.com/Live4Life" title="Faire un don via Patreon"><img src="https://img.shields.io/badge/patreon-donate-yellow.svg" alt="Bouton don Patreon" /></a></span>

<div align="center">

**Languages / Langues:** [🇫🇷 Français](README_FR.md) | [🇬🇧 English](README.md)

</div>

<p align="center">
<img src="Images/live4bubbles_overview.gif" />
</p>

<div align="center">

| [**Vue d'ensemble**](#-vue-densemble) | [**Installation**](#-installation) | [**Configuration**](#%EF%B8%8F-configuration) | [**Documentation**](#-Documentation) | [**Références**](#-références) | [**Contribuer**](#-contribuer) | [**Remerciements**](#-remerciements) | [**Licence**](#-licence) |

</div>

---

# 📖 Vue d'ensemble

---

## 🫧 Qu'est-ce que *Live 4 Bubbles* ?

**Un système de performance pour contrôler des bases de données de Live Coding depuis [Tidal Cycles](https://tidalcycles.org/) et [Hydra](https://hydra.ojack.xyz/) dans l'éditeur de texte [Pulsar](https://pulsar-edit.dev/) avec l'interface graphique et les contrôleurs [SuperCollider](https://supercollider.github.io/).**

*C'est mon principal projet de vie actuel auquel je me consacre à développer et à performer avec ce système audiovisuel, pour autant d'années que possible jusqu'à la fin de ma vie, j'espère !*

## 🔮 Vision et Limites

*Live 4 Bubbles* est en partie une extension et une version modifiée basée sur [SuperDirtMixer](https://github.com/thgrund/SuperDirtMixer) de Thomas Grund. Mais il va bien au-delà d'un simple mixeur pour Tidal Cycles. Il transforme votre configuration de live coding en un **système de performance avec contrôle sur vos patterns rythmiques, sonores et spatiaux** de fichiers audio et de synthèse SuperCollider en temps réel.

⚠️ Bien que *Live 4 Bubbles* puisse être lancé rapidement une fois installé, le processus d'installation implique d'éditer certains fichiers de configuration et de configurer de nombreuses dépendances et programmes open-source pour maximiser les possibilités de contrôle sur la synthèse sonore et la manipulation de patterns.

Comme le montrent les images ci-dessus représentant une configuration à quatre pistes, ce projet est également conçu pour être utilisé conjointement avec [Fizzy Hydra](https://github.com/Xon77/FizzyHydra) si vous souhaitez ajouter des visuels audio-réactifs et un système VJ contrôlé notamment par les données de volume et le déclenchement d'événements par piste.

## ✨ Fonctionnalités principales

### 🎚️ Système de contrôle multi-pistes

- 🎹 **Contrôle dynamique d'exécution de code** - Jusqu'à **8 pistes indépendantes** avec des dizaines de paramètres chacune,
- 🎶 **Déclencher et contrôler les patterns Tidal Cycles** - Depuis l'interface SuperCollider et les contrôleurs MIDI pour basculer entre différents patterns de code à la volée,
- 💾 **Système de préréglages** - Configuration de performance instantanée et transitions avec crossfade fluide entre les patterns,
- 🎲 **Système de randomisation** - Randomiser chaque type de paramètre pour l'exploration créative.

### 🎼 Contrôle paramétrique de votre ligne de code / pattern Tidal Cycles

- ⏱️ **Timing** - Vitesse, durée et legato,
- 〰️ **Sélection de sons** - Navigation rapide dans les bibliothèques de sons avec sélection de dossier et de buffer,
- 🎨 **Base de données d'effets** - Filtres et effets sur les sons et les patterns avec contrôles de paramètres multiples,
- 🌐 **Contrôle spatial** - Positionnement des canaux et gestion des orbits pour des paysages sonores immersifs.

---

# 💿 Installation

---

## 📋 Prérequis

Avant d'installer *Live 4 Bubbles*, assurez-vous d'avoir installé les éléments suivants :

- **[SuperCollider](https://supercollider.github.io/)** (testé sur 3.14) - Plateforme de synthèse algorithmique et audio,
- **[SuperDirtMixer Quark](https://github.com/thgrund/SuperDirtMixer)** - *Live 4 Bubbles* est construit au-dessus de SuperDirtMixer. Suivez les instructions pour installer tous [ses prérequis](https://github.com/thgrund/SuperDirtMixer?tab=readme-ov-file#requirements), incluant **[SuperDirt Quark](https://github.com/musikinformatik/SuperDirt)** (synthétiseur SuperCollider pour Tidal),
- **[Tidal Cycles](https://tidalcycles.org/)** (testé sur 1.9.5 & ghc 9.6.7) - Langage de patterns de live coding,
- **[Pulsar](https://pulsar-edit.dev/)** - Éditeur de texte pour le live coding,
- **[pulsar-tidalcycles](https://web.pulsar-edit.dev/packages/tidalcycles)** - Package Pulsar pour l'intégration de Tidal Cycles (Vous pouvez utiliser ma variation de [pulsar-tidalcycles](https://github.com/Xon77/pulsar-tidalcycles) qui ajoute la possibilité de sélectionner des lignes depuis différents splits, en plus de la fonctionnalité originale de sélection depuis différents onglets. Vous pouvez également trouver ou ajouter le code modifié via [ce lien](https://github.com/tidalcycles/pulsar-tidalcycles/compare/master...Xon77:pulsar-tidalcycles:master)),
- [SuperCollider sc3-plugins](https://supercollider.github.io/sc3-plugins) (Optionnel) pour des capacités de synthèse étendues.

## 📥 Installation étape par étape

### 1️⃣ Télécharger *Live 4 Bubbles*

Téléchargez le dépôt *Live 4 Bubbles* sur votre machine locale. Vous pouvez également cloner avec GIT dans votre terminal :

```bash
git clone https://github.com/Xon77/Live4Bubbles.git
```

### 2️⃣ Installer *Live 4 Bubbles* dans les Extensions SuperCollider

Copiez le dossier [`L4B_SC/SuperDirtMixerX/`](L4B_SC/SuperDirtMixerX/) dans votre dossier d'extensions SuperCollider :

- **macOS** : `~/Library/Application Support/SuperCollider/Extensions/`,
- **Linux** : `~/.local/share/SuperCollider/Extensions/`,
- **Windows** : `%USERPROFILE%\AppData\Local\SuperCollider\Extensions\`.

### 3️⃣ Configurer les paramètres Pulsar Tidal Cycles

Dans le gestionnaire de packages de Pulsar, configurez les paramètres de Tidal Cycles :

1. Ouvrez Pulsar et allez dans **Package** → **Open Package Manager**,
2. Trouvez le package **tidalcycles** et cliquez sur **Settings**,
3. Activez **OSC eval** dans les paramètres pour évaluer le code via des messages OSC,
4. Dans **Boot Tidal Path**, définissez le chemin vers le fichier BootTidal.hs personnalisé inclus dans ce dépôt :
   - Utilisez le chemin absolu vers [`L4B_Tidal/BootTidal/BootTidal.hs`](L4B_Tidal/BootTidal/BootTidal.hs)
   - Exemple : `/Users/VotreNomUtilisateur/Live4Bubbles/L4B_Tidal/BootTidal/BootTidal.hs`

### 4️⃣ Éditer les chemins de scripts BootTidal.hs

Éditez le fichier [`BootTidal.hs`](L4B_Tidal/BootTidal/BootTidal.hs) pour mettre à jour vos chemins de scripts à la fin du fichier ([lignes 308-316](L4B_Tidal/BootTidal/BootTidal.hs#L308-L316)), si vous souhaitez ajouter des synthétiseurs et générateurs SuperCollider supplémentaires :

1. Remplacez `/Users/xon/Desktop/Live_Coding/Tidal/ExtraSynths/` par votre chemin absolu vers [`L4B_Tidal/BootTidal/`](L4B_Tidal/BootTidal/)
2. Mettez à jour ces 4 lignes de script :
   - `:script /chemin/vers/` [`FM-Synths-params.hs`](L4B_Tidal/BootTidal/FM-Synths-params.hs)
   - `:script /chemin/vers/` [`Mi-UGens-params.hs`](L4B_Tidal/BootTidal/Mi-UGens-params.hs)
   - `:script /chemin/vers/` [`ExtraSynths-params.hs`](L4B_Tidal/BootTidal/ExtraSynths-params.hs)
   - `:script /chemin/vers/` [`G_Setup_250225.tidal`](L4B_Tidal/BootTidal/G_Setup_250225.tidal)
3. Exemple de chemin correct : `:script /Users/VotreNomUtilisateur/Live4Bubbles/L4B_Tidal/BootTidal/FM-Synths-params.hs`

## 🛠️ Configurations optionnelles

### 1️⃣ Installer les Quarks SuperCollider (extensions) pour certains contrôleurs MIDI

Si vous prévoyez d'utiliser les contrôleurs MIDI mentionnés ci-dessous, installez les Quarks requis dans SuperCollider :

#### 🎚️ Pour [Xone K2 ou K3](https://www.allen-heath.com/hardware/xone-series/xonek3/) :
- Installez le Quark [`Modality-toolkit`](https://github.com/ModalityTeam/Modality-toolkit), en évaluant dans SuperCollider :
```supercollider
Quarks.install("Modality-toolkit");
```

- Copiez le fichier [`xoneK2.desc.scd`](L4B_SC/xoneK2.desc.scd) dans le dossier `MKtlDescriptions` du répertoire du Quark Modality :
  - **macOS** : `~/Library/Application Support/SuperCollider/downloaded-quarks/Modality-toolkit/Modality/MKtlDescriptions/`,
  - **Linux** : `~/.local/share/SuperCollider/downloaded-quarks/Modality-toolkit/Modality/MKtlDescriptions/`,
  - **Windows** : `%USERPROFILE%\AppData\Local\SuperCollider\downloaded-quarks\Modality-toolkit\Modality\MKtlDescriptions\`.

#### 🎛️ Pour [MIDI Fighter Twister](https://www.midifighter.com/#Twister) :
- Installez le Quark [`Twister`](https://github.com/scztt/Twister.quark) et le Quark [`Connection`](https://github.com/scztt/Connection.quark), en évaluant dans SuperCollider :
```supercollider
Quarks.install("Twister");
Quarks.install("Connection");
```

Vous pouvez connecter jusqu'à 3 MIDI Fighters ensemble. Nommez-les : "MIDI Fighter Twister 1", "MIDI Fighter Twister 2", "MIDI Fighter Twister 3" dans la configuration MIDI.

Si vous souhaitez éditer les fichiers de configuration des contrôleurs, les visualiser, ou les copier et les adapter à d'autres contrôleurs, les fichiers sont :
- [`_Init Midi X2.scd`](L4B_SC/L4B_SC_Project/_Init%20Midi%20X2.scd) pour [Xone K2 ou K3](https://www.allen-heath.com/hardware/xone-series/xonek3/) d'Allen & Heath,
- [`_Init Midi T2.scd`](L4B_SC/L4B_SC_Project/_Init%20Midi%20T2.scd) pour [MIDI Fighter Twister](https://www.midifighter.com/#Twister),
- [`_Init Midi LPX.scd`](L4B_SC/L4B_SC_Project/_Init%20Midi%20LPX.scd) (en développement) pour [Launchpad X](https://novationmusic.com/products/launchpad-x) de Novation.

### 2️⃣ Style Pulsar

Pour une apparence Pulsar personnalisée optimisée pour *Live 4 Bubbles* :

1. Naviguez vers le dossier [`L4B_Pulsar/`](L4B_Pulsar/),
2. Copiez les styles depuis [`styles.less`](L4B_Pulsar/styles.less),
3. Collez-les dans votre feuille de style Pulsar (Edit → Stylesheet).

### 3️⃣ Configuration système avancée

Les paramètres de configuration suivants dans [`_0T_Init_TidalX.scd`](L4B_SC/L4B_SC_Project/_0T_Init_TidalX.scd) sont définis par défaut mais peuvent être modifiés si nécessaire :

**Configuration système :**
- [Ligne 18](L4B_SC/L4B_SC_Project/_0T_Init_TidalX.scd#L18) : `~tidalHydra = 1;` - Envoyer des données OSC à [Fizzy Hydra](https://github.com/Xon77/FizzyHydra) (données RMS/Événements par canal)
- [Ligne 19](L4B_SC/L4B_SC_Project/_0T_Init_TidalX.scd#L19) : `~tidalOutputChannels = 2;` - Nombre de canaux audio de sortie
- [Ligne 20](L4B_SC/L4B_SC_Project/_0T_Init_TidalX.scd#L20) : `~tidalNbOfLoudspeakers = 2;` - Nombre de haut-parleurs
- [Ligne 24](L4B_SC/L4B_SC_Project/_0T_Init_TidalX.scd#L24) : `~tidalNbOfOrbits = 4;` - Nombre de pistes/orbits

**GUI & Catégories sonores :**
- Paramètres de taille de l'interface graphique et 5 catégories sonores pour l'organisation des dossiers

**Exécution du code :**
- [Ligne 75](L4B_SC/L4B_SC_Project/_0T_Init_TidalX.scd#L75) : `~tidalSeqLineEndR = (476/2).asInteger;` - Nombre maximum de lignes de code dans Pulsar (peut également être modifié dans les fichiers de partition Tidal)

**Page de contrôle global :**
- [Ligne 84](L4B_SC/L4B_SC_Project/_0T_Init_TidalX.scd#L84) : `~tidalMainControlPane`, `~tidalMainControlTab`, `~tidalMainControlInterpolationA`, etc. - Contrôles pour l'interpolation, BPM et fonctions globales

**Base de données d'effets et fonctions** (peut également être modifié dans les fichiers de contrôle Haskell) :
- [Ligne 91](L4B_SC/L4B_SC_Project/_0T_Init_TidalX.scd#L91) : `~tidalnumEffects = 122;` - Nombre d'effets disponibles
- [Ligne 92](L4B_SC/L4B_SC_Project/_0T_Init_TidalX.scd#L92) : `~tidalnumFunctions = 264;` - Nombre de fonctions de pattern
- [Ligne 93](L4B_SC/L4B_SC_Project/_0T_Init_TidalX.scd#L93) : `~tidalnumSwitchX = 24;` - Nombre de types superposition/juxtaposition
- [Ligne 94](L4B_SC/L4B_SC_Project/_0T_Init_TidalX.scd#L94) : `~tidalnumWhenS = 39;` - Nombre d'algorithmes de timing
- [Ligne 95](L4B_SC/L4B_SC_Project/_0T_Init_TidalX.scd#L95) : `~tidalnumDurAlgs = 44;` - Nombre d'algorithmes de durée
- [Ligne 96](L4B_SC/L4B_SC_Project/_0T_Init_TidalX.scd#L96) : `~tidalnumSpaAlgs = 29;` - Nombre d'algorithmes spatiaux/de canaux

---

# ⚙️ Configuration

---

## 📁 Configurer votre base de données de sons

Votre dossier de sons doit être structuré suivant la [structure de base de données de sons Live 4 Life](https://github.com/Xon77/L4LSoundsDataBase/tree/main/SoundFolder), sauf que les noms de dossiers doivent utiliser des underscores au lieu d'espaces (par ex., `DB_MBass` au lieu de `DB MBass`).

Exemple de structure :
```
VotreDossierDeSons/
├── DB_Kick/
│   ├── sound1.wav
│   └── sound2.wav
├── DB_Snare/
├── DB_Bass/
└── ...
```

**Configurer le chemin du dossier de sons dans SuperCollider :**

1. Naviguez vers le dossier [`L4B_SC/L4B_SC_Project/`](L4B_SC/L4B_SC_Project/),
2. Ouvrez le fichier d'initialisation SC [`_0T_Init_TidalX.scd`](L4B_SC/L4B_SC_Project/_0T_Init_TidalX.scd),
3. **⚠️ REQUIS : Éditez la [ligne 55](L4B_SC/L4B_SC_Project/_0T_Init_TidalX.scd#L55)** pour définir le chemin du dossier de sons vers votre base de données de sons.

---

## 📝 Configurer votre partition de code dans Pulsar

Créez une partition d'exécution de code à quatre pistes en ouvrant tous les fichiers Tidal Cycles du dossier [`L4B_Tidal/`](L4B_Tidal/) dans Pulsar :

1. [`L4B_Tidal/DJCode1.tidal`](L4B_Tidal/DJCode1.tidal) dans l'onglet 1,
2. [`L4B_Tidal/DJCode2.tidal`](L4B_Tidal/DJCode2.tidal) dans l'onglet 2,
3. [`L4B_Tidal/DJCode3.tidal`](L4B_Tidal/DJCode3.tidal) dans l'onglet 3,
4. [`L4B_Tidal/DJCode4.tidal`](L4B_Tidal/DJCode4.tidal) dans l'onglet 4,
5. [`L4B_Tidal/DJCodeG.tidal`](L4B_Tidal/DJCodeG.tidal) dans l'onglet 5 (pour le contrôle global),
6. [`L4B_Tidal/ControlTidal.tidal`](L4B_Tidal/ControlTidal.tidal) dans l'onglet 6 (pour l'initialisation).

D'autres flux de travail d'exécution de code sont possibles. Par exemple, vous pouvez exécuter des lignes de code en sélectionnant différents splits et onglets. *(Les images ci-dessus représentent quatre splits du premier onglet.)*

📌 **Note :** Les fichiers DJCode fournis ici sont des modèles d'exemple car je me concentre actuellement sur un modèle à quatre pistes. Cependant, il est possible d'ajouter d'autres pages de contrôle. Vous pouvez utiliser 2-3 opérations de rechercher/remplacer en lot pour changer le nom des variables. Voir les indications à la fin du fichier [DJCode2.tidal (lignes 486-489)](L4B_Tidal/DJCode2.tidal#L486-L489) pour des exemples.

📌 **Note :** Cette étape n'est nécessaire que la première fois. Une fois que vous avez enregistré votre espace de travail Pulsar, vous pouvez simplement rouvrir Pulsar avec la disposition d'onglets enregistrée pour les sessions suivantes.

---

## 🚀 Démarrage rapide

### 1️⃣ Démarrer SuperCollider

Naviguez vers le dossier [`L4B_SC/L4B_SC_Project/`](L4B_SC/L4B_SC_Project/) et ouvrez le fichier d'initialisation SuperCollider [`_0T_Init_TidalX.scd`](L4B_SC/L4B_SC_Project/_0T_Init_TidalX.scd), puis évaluez la section entre parenthèses entre les [lignes 16 et 422](L4B_SC/L4B_SC_Project/_0T_Init_TidalX.scd#L16-L422) dans le fichier (Cmd+Entrée sur macOS, Ctrl+Entrée sur Windows/Linux).

Cela va :
- Démarrer le serveur SuperCollider,
- Charger SuperDirt et *Live 4 Bubbles*,
- Initialiser l'interface graphique et OSC,
- Configurer toutes les connexions MIDI (si disponibles).

### 2️⃣ Lancer Pulsar et initialiser les variables de contrôle dans Tidal Cycles

Naviguez vers le dossier [`L4B_Tidal/`](L4B_Tidal/) et ouvrez [`ControlTidal.tidal`](L4B_Tidal/ControlTidal.tidal), puis évaluez les sections suivantes dans l'ordre avec Ctrl+Entrée (Cmd+Entrée sur macOS) :

1. [Lignes 5 à 1107](L4B_Tidal/ControlTidal.tidal#L5-L1107) - Définitions et fonctions de contrôle principales,
2. [Ligne 1110](L4B_Tidal/ControlTidal.tidal#L1110) - Initialisation des contrôles et variables,
3. [Ligne 1113](L4B_Tidal/ControlTidal.tidal#L1113) - Mise à jour des définitions et fonctions de l'étape 1,
4. [Lignes 1118 à 1136](L4B_Tidal/ControlTidal.tidal#L1118-L1136) - Initialisation finale pour les crossfades.

Cela va démarrer Tidal Cycles et configurer tous les paramètres de contrôle pour le tempo, la durée, les dossiers de sons, les buffers, les effets, et plus encore. Une fois votre projet configuré, démarrer *Live 4 Bubbles* est simple : évaluez une section de code dans SuperCollider et quatre sections de code dans Pulsar.

🎉✨🎵 **Vous pouvez commencer à jouer avec l'interface graphique et les contrôleurs !** 🎵✨🎉

---

# 📖 Documentation

---

## 🎛️ Paramètres de contrôle

En plus des contrôles SuperDirtMixer (sur le volume, le panoramique, la réverbération, l'égaliseur et la compression), chacune des 8 pistes (onglets ou splits) a accès aux 31 paramètres suivants :

### 🎵 Structure de pattern
- 🔴 Numéro de préréglage de piste (sauvegarde et rappelle tous les paramètres ci-dessous),
- 🔵 Ligne de code / pattern (de l'onglet dans Pulsar),

### ⏱️ Timing
- 🟠 `d1` à `d8` - Tempo (contrôle de vitesse),
- 🟤 `1Leg` à `8Leg` - Durée (variable de longueur),
- 🟤 `1LegS` à `8LegS` - Commutateur d'algorithme de durée,
- ⚫ `1Xon` à `8Xon` - Assignation d'orbit / piste (permet de couper les sons lorsqu'un nouvel événement est déclenché parmi plusieurs patterns sur le même orbit/piste avec des algorithmes de durée spécifiques).

### 〰️ Sélection de sons
- 🔴 `1Fol` à `8Fol` - Dossier de sons (répertoire contenant les échantillons audio) ou Type de synthèse (avec le commutateur B/S - Note : certaines lignes de code/patterns ne sont pas adaptées à la synthèse et peuvent générer des notes trop élevées ou des sons désagréables, comme la [ligne 38 dans DJCode1](L4B_Tidal/DJCode1.tidal#L38). Assurez-vous que votre code est approprié lors de l'utilisation ou du passage en mode synthèse),
- 🟪 `1Buf` à `8Buf` - Fichier son (dans le dossier de sons),
- 🔴 `1FolN` à `8FolN` - Index de dossier normalisé (0-1),
- 🟪 `1BufN` à `8BufN` - Index de fichier normalisé (0-1).

### ▶️ Lecture
- 🟢 `1Rat` à `8Rat` - Vitesse de lecture,
- ⚪ `1Ran` à `8Ran` - Paramètre dépendant du contexte/ligne (filtre [djf](https://tidalcycles.org/docs/reference/audio_effects/#dj-filter) par défaut).

### 🎨 Effets sonores (2 chaînes par piste)

- 🟢 `1fxs` à `8fxs` / `1fxs2` à `8fxs2` - **Quand** (type d'algorithme de timing),
- 🟢 `1fxt` à `8fxt` / `1fxt2` à `8fxt2` - **Valeur de Quand** (valeur de l'algorithme Quand),
- 🟢 `1fxx` à `8fxx` / `1fxx2` à `8fxx2` - **Comment** (superposition ou non, avec des délais de diverses durées),
- 🟢 `1fxp` à `8fxp` / `1fxp2` à `8fxp2` - **Quoi** (transformation d'effet spécifique),
- 🟢 `1fxv` à `8fxv` / `1fxv2` à `8fxv2` - **Combien ou Valeur de Quoi** (valeur de l'algorithme Quoi).

### 🔄 Fonctions de pattern (2 chaînes par piste)

- 🔵 `1fus` à `8fus` / `1fus2` à `8fus2` - **Quand** (type d'algorithme de timing),
- 🔵 `1fut` à `8fut` / `1fut2` à `8fut2` - **Valeur de Quand** (valeur de l'algorithme Quand),
- 🔵 `1fux` à `8fux` / `1fux2` à `8fux2` - **Comment** (superposition ou non, avec des délais de diverses durées),
- 🔵 `1fup` à `8fup` / `1fup2` à `8fup2` - **Quoi** (transformations : degradeBy, trunc, etc.),
- 🔵 `1fuv` à `8fuv` / `1fuv2` à `8fuv2` - **Combien ou Valeur de Quoi** (valeur de l'algorithme Quoi).

### 🌐 Spatial
- 🟡 `1Spa` à `8Spa` - Positionnement spatial de canal.

---

**Voir le [wiki](https://github.com/Xon77/Live4Bubbles/wiki) pour plus de détails sur la documentation** (en construction et mises à jour constantes).

**Une documentation supplémentaire sera ajoutée dans les semaines et mois à venir.**

---

# 📚 Références

---

Ce projet, brièvement présenté lors de **Composite Montréal** en juin 2025 à travers un pitch de 5 minutes, a été présenté le **samedi 18 octobre 2025** dans le cadre d'un atelier expérimental public de trois heures au [Eastern Bloc](https://www.easternbloc.ca), qui comprenait une courte performance et une session d'exploration participative avec le public. Je remercie chaleureusement [Eastern Bloc](https://www.easternbloc.ca) et le [CALQ](https://www.calq.gouv.qc.ca/en/) pour leur soutien dans la réalisation de cette présentation.

---

# 🤝 Contribuer &nbsp;&nbsp; [![ko-fi](https://ko-fi.com/img/githubbutton_sm.svg)](https://ko-fi.com/Live4Life) &nbsp;&nbsp; <a href="https://www.patreon.com/Live4Life"><img src="https://c5.patreon.com/external/logo/become_a_patron_button@2x.png" width="160"></a>

---

**Vous souhaitez contribuer ?** Veuillez me contacter pour organiser un développement ultérieur.

**Soutenez ce projet par des dons via [Ko-fi](https://ko-fi.com/live4life) (ponctuel) ou [Patreon](https://www.patreon.com/live4life) (soutien continu et cours).**

---

# 🙏 Remerciements

---

Comme [mentionné dans mon projet précédent](https://github.com/Xon77/Live4Life?tab=readme-ov-file#acknowledgements) ***Live 4 Life***, je n'aurais pas pu construire cet outil sans l'aide de la **communauté SuperCollider en ligne et de live coding**, qui a toujours répondu à mes questions et m'a même fourni des exemples de codes et de classes.

### 🌟 Remerciements spéciaux

**Thomas Grund** - Sans qui ce projet n'aurait pas vu le jour :
- Pour avoir créé le Quark [SuperDirtMixer](https://github.com/thgrund/SuperDirtMixer),
- Pour avoir implémenté la fonctionnalité permettant l'évaluation de lignes de code Tidal dans Atom/Pulsar, que j'avais suggérée sur [GitHub](https://github.com/tidalcycles/pulsar-tidalcycles/issues/119).

---

Ce projet a été soutenu par le [**Conseil des arts et des lettres du Québec (CALQ)**](https://www.calq.gouv.qc.ca/en/).

<p align="center">
<a href="https://www.calq.gouv.qc.ca/en/">
<img src="Images/Calq_noir.jpg" alt="CALQ - Conseil des arts et des lettres du Québec" width="400">
</a>
</p>

---

# 📄 Licence

---

© 2025 - Fin du monde ∞ **Christophe Lengelé**

***Live 4 Bubbles*** est un logiciel open source : vous pouvez le redistribuer et/ou le modifier selon les termes de la **licence Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International** (CC BY-NC-SA 4.0).

### ⚠️ Restrictions d'utilisation

**Usage commercial :** Vous ne pouvez pas utiliser cet outil à des fins commerciales.

**Esprit Free Party :** Je souhaite qu'il soit utilisé dans l'esprit de [*Free Party*](https://fr.wikipedia.org/wiki/Free_party). Malheureusement, *Free* ne signifie pas gratuit dans ce monde commercial, mais invite à contribuer aux coûts et au travail selon sa capacité à donner. **Je ne veux pas que cet outil soit utilisé, par quelque moyen que ce soit, pour un profit personnel.**

**Restriction spécifique :** Je ne veux pas que cet outil soit utilisé par ou dans la [Société des Arts Technologiques](https://sat.qc.ca) sans mon consentement, car cette organisation ne m'a jamais aidé à diffuser mes créations spatiales dans leur dôme malgré mes propositions précédentes. *(Cette restriction sera levée uniquement si j'ai l'opportunité de jouer des improvisations dans leur dôme avec une allocation appropriée. Cependant, cela n'arrivera sûrement jamais, car je ne suis plus intéressé à leur envoyer des candidatures et je cherche des espaces plus ouverts. Mais, la vie est imprévisible.)*

### 📋 Avertissement

Ce programme est distribué dans l'espoir qu'il soit utile, mais **SANS AUCUNE GARANTIE**.

---

Voir la [Licence](/LICENCE.md) pour plus de détails.
