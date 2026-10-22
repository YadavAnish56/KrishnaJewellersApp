# Krishna Jewellers

A native Android jewellery showroom app built with Kotlin and Jetpack Compose. Browse gold and silver collections, explore estimated prices, save favourite designs, and try customer enquiries and showroom management in a local demo.

| Project | Details |
| --- | --- |
| App version | 1.0.2 (version code 3) |
| Application ID | `com.krishnajewellers.app` |
| Minimum Android | Android 7.0 / API 24 |
| Compile / target SDK | API 34 |
| Interface | Jetpack Compose and Material 3 |
| Storage | SQLite and Preferences DataStore |

## Features

- **Showroom home:** featured collections, product imagery, categories, and metal rates.
- **Explore and product details:** browse jewellery and inspect weight, purity, making charges, and estimated prices.
- **Price breakdown:** metal value, percentage/per-gram/fixed making charges, and a configured 3% GST estimate using `BigDecimal` calculations.
- **Saved designs:** keep favourites and liked products on the device.
- **Enquiries:** local conversation screens with product and price attachments.
- **Custom designs:** submit and manage design requests in the demo.
- **Staff portal:** manage products, visibility, metal rates, enquiries, and custom requests.
- **Onboarding and profiles:** sample customer login and guest browsing.

## What runs locally

This repository is an **offline demo**. SQLite holds the catalogue, rates, favourites, conversations, and custom requests; DataStore holds profile and session preferences. A fresh installation seeds sample data automatically. No backend, Firebase project, API key, or database server is needed to run the core demo.

Customer login and the staff passcode are demo UI flows, not production authentication. Enquiries and staff updates stay on the same installation: they are not delivered to another phone or a live showroom. Rates are sample/manual values, not a live market feed. Prices are estimates rather than checkout or invoicing.

## Get started

### 1. Clone the repository

```bash
git clone https://github.com/YadavAnish56/KrishnaJewellersApp.git
cd KrishnaJewellersApp
```

### 2. Open in Android Studio

1. Open the cloned repository root, containing `settings.gradle.kts`.
2. Install **Android SDK Platform 34** and the Android SDK build tools when prompted.
3. Select a Gradle JDK compatible with the checked-in Gradle/Android plugins; JDK 17 or Android Studio's bundled JDK 21 can be used. The Kotlin/Java compilation target is 17. Avoid defaulting to an unrelated system JDK such as Java 25.
4. Let Gradle sync finish. The wrapper downloads Gradle 8.14 and dependencies on the first run, so internet access is required for initial setup.
5. Select the `app` run configuration and an emulator or connected Android device running API 24 or later, then click **Run**.

Android Studio normally creates `local.properties` with your SDK path. This file is deliberately ignored because the path differs on each computer. If setting it manually, use your own path:

```properties
# Windows example; replace YOUR_USER with your actual Windows username.
sdk.dir=C:/Users/YOUR_USER/AppData/Local/Android/Sdk
```

### 3. Build from the command line

Set `JAVA_HOME` to your JDK installation and make sure the Android SDK is configured. For example, in Windows PowerShell with Android Studio installed at its default location:

```powershell
$env:JAVA_HOME = 'C:\Program Files\Android\Android Studio\jbr'
.\gradlew.bat assembleDebug
.\gradlew.bat testDebugUnitTest
```

On macOS/Linux:

```bash
chmod +x gradlew
./gradlew assembleDebug
./gradlew testDebugUnitTest
```

The debug APK is generated at `app/build/outputs/apk/debug/app-debug.apk`. With a device connected and USB debugging enabled, use `gradlew.bat installDebug` on Windows or `./gradlew installDebug` on macOS/Linux.

## Try the demo

1. Complete onboarding and use the prefilled demo customer account, or continue as a guest.
2. Browse products, open a design, inspect its price estimate, and save a favourite.
3. Start a product enquiry or submit a custom design request.
4. Open **Profile > Showroom Staff Portal** and use demo passcode `7777` to explore management tools. The source also accepts `1947` and `1234`; these are public demo values.
5. Update a rate or product and return to the customer screens to see the local changes.

Data normally persists across app restarts. Clearing app storage or uninstalling removes local data, although Android backup/restore can restore data when enabled. Git clone/pull transfers source code, not app data from an installed phone. The current database `onUpgrade` implementation drops and reseeds tables; replace it with proper migrations before increasing the database version for real users.

## Project structure

```text
app/
  build.gradle.kts           App configuration, dependencies, optional signing
  src/main/
    AndroidManifest.xml     Application and activity declarations
    java/com/krishnajewellers/app/
      MainActivity.kt       Android entry point
      KrishnaJewellersApp.kt
      data/
        local/              SQLite schema, sample data, preferences
        model/              Products, rates, requests, chats, price calculations
        repository/         Persistence and observable app data
      viewmodel/            Screen state and actions
      ui/
        components/         Shared Compose components
        navigation/         Routes and bottom navigation
        screens/            Customer and staff screens
        theme/              Colours, typography, shapes
      utils/                Drawable helpers
    res/                    Product imagery, icons, strings, Android themes
  src/test/                 Price and rate unit tests
gradle/wrapper/             Checked-in Gradle wrapper
```

To change sample products, start with `data/local/SeedData.kt`. To change calculations, inspect `data/model/PriceBreakdown.kt`, `MetalRates.kt`, and `Product.kt`. To change screens, edit `ui/screens/`; shared visual styles are in `ui/theme/` and `ui/components/`.

## Collaborating with a friend

The repository is public, so your friend can clone it immediately. To let them push branches directly, the repository owner must invite their GitHub account under **Settings > Collaborators**. Otherwise they can fork the repository and submit a pull request.

Start each change from an updated `main` branch:

```bash
git switch main
git pull --ff-only origin main
git switch -c feature/your-change
# Edit the app, run it, and run the unit tests.
git add path/to/changed/files
git commit -m "Describe the change"
git push -u origin feature/your-change
```

Open a pull request into `main` on GitHub. After it is merged, both developers can run `git switch main` followed by `git pull --ff-only origin main`. Commit or stash local changes before switching branches or pulling.

## Signing and private files

A fresh clone builds debug APKs using the standard local Android debug signing key. The original private signing key, signing passwords, SDK paths, APKs, caches, and build directories are excluded from Git.

For an authorised signed build:

1. Obtain the intended signing key privately from its owner, or create your own for a separate distribution.
2. Copy `keystore.properties.example` to `keystore.properties` in the repository root.
3. Fill in the key path, alias, and passwords. Use forward slashes in paths.
4. Run `gradlew.bat assembleRelease` (Windows) or `./gradlew assembleRelease` (macOS/Linux).

When `keystore.properties` exists, both debug and release builds use that configured key to support development updates of existing installations. Without it, debug uses the default debug key and release is unsigned. Never commit signing keys or `keystore.properties`.

An APK signed with another developer's debug key cannot update an existing installation signed by the original key. Use the original signing configuration privately to preserve update compatibility. Uninstalling the old app allows installation with a different key but removes local app data.

## Verification and troubleshooting

- Run `testDebugUnitTest` for the existing four tests covering gold/silver rate conversion and percentage, per-gram, and fixed making-charge calculations.
- Run `assembleDebug` to check compilation and packaging. These commands do not replace visual or physical-device checks.
- **SDK location not found:** open Android Studio's SDK settings and regenerate `local.properties` for your computer.
- **Wrong Java version:** select the correct Gradle JDK in Android Studio or set `JAVA_HOME` before running the wrapper.
- **Dependency download failed:** check connectivity to Google Maven, Maven Central, and the Gradle distribution service, then sync again.
- **Signature mismatch on install:** use the same private signing key as the installed app; see the signing section before uninstalling.

## Before production use

Production deployment needs server-backed authentication and authorisation, shared storage and message delivery, verified rate sources, non-destructive database migrations, and an appropriate privacy/data policy. Review the rights to bundled product imagery before redistribution. No open-source licence has been assigned in this repository; agree on reuse and distribution terms with the owner.
